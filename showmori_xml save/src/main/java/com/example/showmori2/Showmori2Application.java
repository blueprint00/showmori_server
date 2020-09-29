package com.example.showmori2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Showmori2Application extends SpringBootServletInitializer{

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Showmori2Application.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(Showmori2Application.class, args);
	}

}

