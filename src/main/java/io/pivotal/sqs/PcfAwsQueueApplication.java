package io.pivotal.sqs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PcfAwsQueueApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PcfAwsQueueApplication.class, args);
	}
}
