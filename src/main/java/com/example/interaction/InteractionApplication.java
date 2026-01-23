package com.example.interaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InteractionApplication {
	public static void main(String[] args) {
		SpringApplication.run(InteractionApplication.class, args);
        System.out.println("Growfy Auth Application started on http://localhost:8080");
    }
}
