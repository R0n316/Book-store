package ru.alex.BookStoreApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class BookStoreAppApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreAppApplication.class, args);
	}
}
