package com.megacityCab.megaCityCabBackEnd.service.impl;

import com.megacityCab.megaCityCabBackEnd.dto.PaymentDto;
import com.megacityCab.megaCityCabBackEnd.entity.Payment;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentDateResult;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentDetails;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentMonthResult;
import com.megacityCab.megaCityCabBackEnd.entity.custom.CustomPaymentResult;
import com.megacityCab.megaCityCabBackEnd.repo.PaymentRepo;
import com.megacityCab.megaCityCabBackEnd.service.PaymentService;
import com.megacityCab.megaCityCabBackEnd.util.response.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import net.sf.jasperreports.engine.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    ModelMapper modelMapper;

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Autowired
    DataSource dataSource;

    @Override
    public StripeResponse createPayment(PaymentDto paymentDto, String type) {
        if (!type.equals("User")) {
            throw new BadCredentialsException("dont have permission");
        }

        Stripe.apiKey = secretKey;
        try {
            // Build Product Data
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(String.valueOf(paymentDto.getVehicleId()))
                            .build();

            // Build Price Data
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("lkr")
                            .setUnitAmount((long) (paymentDto.getAmount() * 100))
                            .setProductData(productData)
                            .build();

            // Build Line Item
            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build();

            // Build Session Parameters
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:3000/success")
                            .setCancelUrl("http://localhost:3000/cancel")
                            .addAllLineItem(Collections.singletonList(lineItem))
                            .build();

            Session session = Session.create(params);
            return new StripeResponse(
                    "SUCCESS",
                    "Payment session created successfully.",
                    session.getId(),
                    session.getUrl(),
                    savePayment(paymentDto)
            );



        } catch (StripeException e) {
            return new StripeResponse(
                    "ERROR",
                    "Failed to create payment session: " + e.getMessage(),
                    null,
                    null,
                    null
            );
        }
    }

    @Override
    public Payment savePayment(PaymentDto paymentDto) {
        Payment map = modelMapper.map(paymentDto, Payment.class);
        return paymentRepo.save(map);
    }

    @Override
    public List<CustomPaymentResult> getAllPayments(String type) {
        if (!type.equals("Admin")) {
            throw new BadCredentialsException("dont have permission");
        }
        return  paymentRepo.getPayments();
    }

    @Override
    public List<CustomPaymentDateResult> getPaymentByThisWeekDay(String type) {
        System.out.println("getPaymentByThisWeekDay: TYPE: "+type);
        if (!type.equals("Admin")) {
            throw new BadCredentialsException("dont have permission");
        }
        return paymentRepo.getPaymentByThisWeekDay();
    }

    @Override
    public List<CustomPaymentMonthResult> getPaymentByThisMonth(String type) {
        if (!type.equals("Admin")) {
            throw new BadCredentialsException("dont have permission");
        }
        return paymentRepo.getPaymentByThisMonth();
    }

    @Override
    public CustomPaymentDetails getPaymentDetailsByPaymentId(Long paymentId, String type, String reportFormat) {
        if (!type.equals("User")) {
            throw new BadCredentialsException("dont have permission");
        }
        return paymentRepo.getPaymentDetailsByPaymentId(paymentId);
    }

    @Override
    public byte[] returnExportReport(long paymentId, String reportFormat,String type) {
        if (!type.equals("User")) {
            throw new BadCredentialsException("dont have permission");
        }
        try {
            // Load the Jasper report file from resources
            InputStream reportStream = new ClassPathResource("megacitycabNewReport.jrxml").getInputStream();
            if (reportStream == null) {
                throw new FileNotFoundException("Report file not found in the classpath");
            }

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Create a parameter map and pass paymentId
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Java");
            parameters.put("paymentId", paymentId);  // Pass the paymentId parameter

            // Get a connection from the data source
            try (Connection c = dataSource.getConnection()) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, c);

                if (reportFormat.equalsIgnoreCase("pdf")) {
                    return JasperExportManager.exportReportToPdf(jasperPrint);
                }

            } catch (Exception e) {
                throw new RuntimeException("Error generating report", e);
            }

        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException("Error generating report", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new byte[0];
    }

    @Override
    public String getPaymentStatusById(long paymentId, String type) {
        if (!type.equals("User")) {
            throw new BadCredentialsException("dont have permission");
        }
        return  paymentRepo.getPaymentStatusById(paymentId);
    }
}

