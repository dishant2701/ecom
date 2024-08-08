package com.Ecomm.Ecomm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcommApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EcommApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		System.out.println("Ecomm API running"
				+ (SERVER_PORT != 0 ? (" on PORT " + SERVER_PORT) : "") + " 🚀");
	}

	private static int SERVER_PORT;

	@Value("${server.port}")
	public void setServerPort(int port) {
		SERVER_PORT = port;
	}

}
