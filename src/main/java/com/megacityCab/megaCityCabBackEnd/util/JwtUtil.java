package com.megacityCab.megaCityCabBackEnd.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY ;

    public String extractUser (String token) throws ExpiredJwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractType (String token) throws ExpiredJwtException {
        return (String) extractAllClaims(token).get("type");
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException{
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) throws ExpiredJwtException{
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, String userType ){
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", userType);
        return creteToken(claims,userDetails.getUsername());
    }

    public String generateTokenRoles(UserDetails userDetails, List<String> userType ){
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", userType);
        return creteToken(claims,userDetails.getUsername());
    }

    private String creteToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // 1 minute
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }




    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUser(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
