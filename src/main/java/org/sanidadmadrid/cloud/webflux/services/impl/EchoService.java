package org.sanidadmadrid.cloud.webflux.services.impl;

import org.springframework.stereotype.Service;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EchoService {
	
    @Observed(
            name = "user.name",
            contextualName = "echoSevice-->",
            lowCardinalityKeyValues = {"keyValue1", "echo","keyValue2","echo2"}
    )
	public String echo(String echo) {
		log.info("Eco desde el EchoService:{}",echo);
		return "Return: " + echo;
		
	}

}
