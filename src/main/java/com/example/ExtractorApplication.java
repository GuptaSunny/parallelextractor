package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;

import com.example.service.DataExtractorService;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.*")
public class ExtractorApplication implements ApplicationRunner {

	@Autowired
	private DataExtractorService dataExtractorService;

	public static void main(String... args) {
		SpringApplication.run(ExtractorApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("Spring boot standalone application is working...");
		Assert.notEmpty(args.getSourceArgs(), " No Args is present ");
		Assert.notNull(args.getOptionValues("url").get(0), "URL is null");
		dataExtractorService.process(args.getOptionValues("url").get(0));

	}
}
