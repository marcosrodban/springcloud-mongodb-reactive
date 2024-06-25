package org.sanidadmadrid.cloud.webflux.controller;

import org.sanidadmadrid.cloud.taskevents.events.TaskEvent;
import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.sanidadmadrid.cloud.webflux.services.impl.TaskEventsHandler;
import org.sanidadmadrid.cloud.webflux.services.impl.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mongodb")
public class UsuariosController {
	
	@Autowired
	private UsuariosService usuariosService;
	
	@Autowired
	private TaskEventsHandler taskEventsHandler;
	
	
	@PostMapping("/usuario")
	public Mono<ResponseEntity<Usuario>> usuario(@RequestBody Usuario usuario) {
		return usuariosService.usuario(usuario);
	}
	
	@GetMapping("/usuarios")
	public Flux<Usuario> listaUsuarios() {
		Flux<Usuario> theReturn = usuariosService.listUsuarios();
		theReturn.log("generating data....").doOnNext(data -> System.out.print(data));
		return theReturn;
	}
	
	
	@PostMapping("/handletaskevent")
	public ResponseEntity<TaskEvent> handleTaskEvent(@RequestBody TaskEvent taskEvent) {
		return new ResponseEntity<TaskEvent>(taskEventsHandler.handleEvent(taskEvent), HttpStatus.OK);
	}
	
}
