package com.example.restservice;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.restservice.util.Constants;
import com.example.restservice.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		// greeting?name=User
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping(value = "users/{user}", method = RequestMethod.GET)
	public String getSingleUser(@PathVariable String user) {
		// api/users/2
		logger.info("BEGIN - getSingleUser");
		long start = System.currentTimeMillis();
		String ResponseJSON = null;

		Client client = ClientBuilder.newClient();
		Response response = client.target(Constants.URL).path("users/").path(user).request(MediaType.APPLICATION_JSON)
				.get(Response.class);

		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			// SINGLE USER NOT FOUND
			logger.error(Response.Status.NOT_FOUND.toString() + " : " + Response.Status.NOT_FOUND.getStatusCode());
			ResponseJSON = String.valueOf(Response.Status.NOT_FOUND.getStatusCode());
		} else {
			ResponseJSON = response.readEntity(String.class);
			logger.debug(ResponseJSON);
		}

		// INICIO - Ejemplo para recupera los valores del JSON
		ObjectMapper mapper = new ObjectMapper();
		JsonNode nodeRoot = null;
		try {
			nodeRoot = mapper.readTree(ResponseJSON);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonNode nodoData = nodeRoot.path("data");
		String idValue = nodoData.get("id").asText();
		logger.debug(idValue);
		// FIN - Ejemplo para recupera los valores del JSON

		client.close();
		long finish = System.currentTimeMillis();
		logger.info("END - getSingleUser " + Util.getFormatElapsedTime(finish - start));

		return ResponseJSON;
	}

	@RequestMapping(value = "usersRestTemplate/{user}", method = RequestMethod.GET)
	public String getSingleUserRestTemplate(@PathVariable String user) {
		logger.info("BEGIN - getSingleUserRestTemplate");
		long start = System.currentTimeMillis();
		ResponseEntity<String> result = null;

		try {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders(); // es importante añadir los headers ya que sin esto nos da un 403 Forbidden
			headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			result = restTemplate.exchange(Constants.ENDPOINT.concat(user), HttpMethod.GET, entity, String.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result.getStatusCodeValue() != 200) {
			logger.error("Status request: " + result.getStatusCodeValue());
		} else {
			logger.debug(result.getBody());
		}

		long finish = System.currentTimeMillis();
		logger.info("END - getSingleUserRestTemplate " + Util.getFormatElapsedTime(finish - start));
		return result.getBody();
	}

	@GetMapping("/users")
	public String getListUsers(@RequestParam String page) {
		// api/users?page=2
		logger.info("BEGIN - getListUsers");
		long start = System.currentTimeMillis();

		Client client = ClientBuilder.newClient();
		String json = client.target(Constants.URL).path("users").queryParam("page", page)
				.request(MediaType.APPLICATION_JSON).get(String.class);

		logger.debug(json);
		client.close();
		long finish = System.currentTimeMillis();
		logger.info("END - getListUsers " + Util.getFormatElapsedTime(finish - start));
		return json;
	}

}
