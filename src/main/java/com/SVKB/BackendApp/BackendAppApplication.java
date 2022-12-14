package com.SVKB.BackendApp;

import com.SVKB.BackendApp.DTOs.SvUserDTO;
import com.SVKB.BackendApp.model.SvUser;
import com.SVKB.BackendApp.repo.SvUserRepo;
import com.SVKB.BackendApp.service.SvUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories("com.*")
@ComponentScan(basePackages = { "com.*" })
@EntityScan("com.*")
public class BackendAppApplication {
	final SvUserRepo repo;

	public BackendAppApplication(SvUserRepo repo) {
		this.repo = repo;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendAppApplication.class, args);
	}

	@Bean
	CommandLineRunner createDefaultUser(SvUserService svUserService) {

		if (!repo.existsByUsername("administrator")) {
			SvUserDTO svUser = new SvUserDTO("IT_Default", "administrator", "ValleDelSol9150", "ROLE_IT_ADMIN");
			return args -> {
				svUserService.CreateUser(svUser);
			};
		} else {
			return args -> {
				String s = "Administrator Exists!";
				log.info(s);

			};
		}
	}
}
