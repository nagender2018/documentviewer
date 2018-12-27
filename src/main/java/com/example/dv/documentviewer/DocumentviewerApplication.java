package com.example.dv.documentviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.dv")
public class DocumentviewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentviewerApplication.class, args);
	}

}

