package com.sensitivewords.assessment;

import org.springframework.boot.SpringApplication;

public class TestAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.from(AssessmentApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
