package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	//https://reqres.in/ Este método llama a esta API que simula datos.
	@GetMapping("/user")
	public String getUser(@RequestParam String id) {
		logger.info("BEGIN - getUser");
//		new User(id);
		
		Client client = ClientBuilder.newClient();
		String json = client.target("https://reqres.in/api/")
		                    .path("users/").path(id)
		                    .request(MediaType.APPLICATION_JSON)
		                    .get(String.class);
		logger.debug(json);
		logger.info("END - getUser");
		return json;
		
		
	}
	
	
}
