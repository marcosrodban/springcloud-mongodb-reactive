package org.sanidadmadrid.cloud.webflux.reactive;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySubscriber implements Consumer<Integer> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MySubscriber.class);	
	


	public void accept(Integer t) {
		LOGGER.info(String.format("NUEVO VALOR EN EL SUBSCRIBER:[%s]", t));
		
	}




}
