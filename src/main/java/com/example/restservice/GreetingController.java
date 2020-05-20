package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	//https://reqres.in/ Este método llama a esta API que simula datos.
	
	@GetMapping("/user")
	public void getUser(@RequestParam String id) {
//		new User(id);
		
		Client client = ClientBuilder.newClient();
		String json = client.target("https://reqres.in/api/")
		                    .path("users/").path(id)
		                    .request(MediaType.APPLICATION_JSON)
		                    .get(String.class);
		System.out.println(json);
	}
	
	
}
