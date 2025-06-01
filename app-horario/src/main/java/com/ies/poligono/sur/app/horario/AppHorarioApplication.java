package com.ies.poligono.sur.app.horario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AppHorarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppHorarioApplication.class, args);
	}

}
