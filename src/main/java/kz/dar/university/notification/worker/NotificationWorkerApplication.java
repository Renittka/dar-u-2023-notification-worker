package kz.dar.university.notification.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NotificationWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationWorkerApplication.class, args);
	}

}
