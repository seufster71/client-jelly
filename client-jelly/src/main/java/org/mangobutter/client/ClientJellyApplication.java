package org.mangobutter.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ClientJellyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientJellyApplication.class, args);
	}
}
