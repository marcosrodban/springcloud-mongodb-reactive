package org.sanidadmadrid.cloud.webflux.controller;


import org.sanidadmadrid.cloud.webflux.documents.Contacto;
import org.sanidadmadrid.cloud.webflux.services.impl.ContactoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.reactivestreams.client.MongoClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mongodb")
public class ContactoController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ContactoController.class);
	
	@Autowired
	private ContactoService contactoService;

	@Autowired
	private MongoClient mongoClientContacto;

	@GetMapping("/contactos")
	public Flux<Contacto> listContactos() {
		Flux<Contacto> theReturn = contactoService.listContactos();
		theReturn.log("generating data....").doOnNext(data -> System.out.print(data));
		return theReturn;
	}

	@GetMapping("/contactos/{id}")
	public Mono<ResponseEntity<Contacto>> contacto(@PathVariable String id) {
		return contactoService.contacto(id);

	}
	
	
	@GetMapping("/contactos/find")
	public Flux<Contacto> contactoEmail(@RequestParam String email,@RequestParam String nombre) {
		Contacto c = new Contacto();
		c.setMyemail(email);
		c.setNombre(nombre);
		return contactoService.contactoNombreEmail(c);

	}

	@PostMapping("/contactos")
	public Mono<ResponseEntity<Contacto>> contacto(@RequestBody Contacto contacto) {
		LOGGER.info(String.format("Nuevo Contacto:[%s]", contacto));
		return contactoService.contacto(contacto);

	}

	@PutMapping("/contactos")
	public Mono<ResponseEntity<Contacto>> updateContacto(@RequestBody Contacto contacto) {
		return contactoService.updateContacto(contacto);

	}

	@DeleteMapping("/contactos")
	public Mono<Void> delete(@RequestBody Contacto contacto) {
		return contactoService.delete(contacto);

	}

}
