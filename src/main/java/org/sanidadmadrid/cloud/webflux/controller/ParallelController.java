package org.sanidadmadrid.cloud.webflux.controller;


import org.sanidadmadrid.cloud.webflux.services.impl.ParallelServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/mongodb")
public class ParallelController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ParallelController.class);

	@Autowired
	private ParallelServiceImpl parallelService;



	
	@GetMapping("/parallelprocess")
	public void paralellprocess() {
		parallelService.comparableFuture();
	}
	

}
