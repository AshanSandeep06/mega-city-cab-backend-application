package com.megacityCab.megaCityCabBackEnd;

import com.megacityCab.megaCityCabBackEnd.entity.Roles;
import com.megacityCab.megaCityCabBackEnd.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class MegaCityCabBackEndApplication implements CommandLineRunner {

	@Autowired
	RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(MegaCityCabBackEndApplication.class, args);
		System.out.println("hello spring");
	}

	@Override
	public void run(String... args) throws Exception {
		List<Roles> all = roleRepo.findAll();
		if (all.isEmpty()){
			Roles userRolesOne = new Roles();
			userRolesOne.setRole("Admin");
			userRolesOne.setStatus("1");
			roleRepo.save(userRolesOne);
			Roles userRolesTwo = new Roles();
			userRolesTwo.setRole("User");
			userRolesTwo.setStatus("1");
			roleRepo.save(userRolesTwo);
		}
	}
}
