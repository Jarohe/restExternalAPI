package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.util.Constants;
import com.example.restservice.util.Util;


@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	@RequestMapping(
	        value = "users/{user}",
	        method = RequestMethod.GET
	)
	public String getUser(@PathVariable String user) {
		logger.info("BEGIN - getUser");
		long start = System.currentTimeMillis();

		Client client = ClientBuilder.newClient();
		String json = client.target(Constants.URL).path("users/").path(user).request(MediaType.APPLICATION_JSON)
				.get(String.class);
		logger.debug(json);
		client.close();
		long finish = System.currentTimeMillis();
		logger.info("END - getUser " + Util.getFormatElapsedTime(finish - start));
		return json;

	}
	
	@GetMapping("/users")
	public String getListUsers(@RequestParam String page) {
		logger.info("BEGIN - getListUsers");
		long start = System.currentTimeMillis();
		
		Client client = ClientBuilder.newClient();
		String json = client.target(Constants.URL)
				.path("users")
				.queryParam("page", page)
				.request(MediaType.APPLICATION_JSON)
				.get(String.class);

		logger.debug(json);
		client.close();
		long finish = System.currentTimeMillis();
		logger.info("END - getListUsers " + Util.getFormatElapsedTime(finish - start));
		return json;
	}
	
	
}
