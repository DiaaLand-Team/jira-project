package com.stage.diaaland;

import com.stage.diaaland.messaging.UserEventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(UserEventStream.class)
public class GraphServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphServiceApplication.class, args);
	}
}
