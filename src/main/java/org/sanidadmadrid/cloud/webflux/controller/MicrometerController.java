package org.sanidadmadrid.cloud.webflux.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/micrometer")
public class MicrometerController {
	
	@Autowired
	private RestTemplate restTemplate;


	@GetMapping(value = "/micrometerend2", produces = MediaType.APPLICATION_JSON_VALUE)
	public String micrometer() {
		log.info("this is the end of the road {}", "ecooooo");
		return "this is the end of the road";
	}

	@GetMapping("/micrometer2")
	private String echo() {
		log.info(String.format("ha llegado una peticion de llamada redireccionamos.....{}", ""));
		try {

			// restSenderService.send("");
			// RestTemplateBuilder.build();
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			// String urlTemplate =
			// UriComponentsBuilder.fromHttpUrl("http://localhost:8898/micrometer/micrometercalled")
			String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://localhost:8899/micrometer/micrometercalled")
					.queryParam("msisdn", "{msisdn}").queryParam("email", "{email}")
					.queryParam("clientVersion", "{clientVersion}").queryParam("clientType", "{clientType}")
					.queryParam("issuerName", "{issuerName}").queryParam("applicationName", "{applicationName}")
					.encode().toUriString();

			Map<String, String> params = new HashMap<>();
			params.put("msisdn", "msisdn");
			params.put("email", "msisdn");
			params.put("clientVersion", "msisdn");
			params.put("clientType", "msisdn");
			params.put("issuerName", "msisdn");
			params.put("applicationName", "msisdn");

			
			return "estoy hasta los huevos";
			/*HttpEntity<String> response = restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, String.class,
					params);
			return response.getBody();*/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("ERROR TRATANDO LA LLAMADA{}", "", e);
			return "LLAMADA ERROR";
		}

	}


}
