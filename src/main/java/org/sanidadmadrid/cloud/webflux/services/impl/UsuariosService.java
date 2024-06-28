package org.sanidadmadrid.cloud.webflux.services.impl;

import java.time.Duration;

import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.sanidadmadrid.cloud.webflux.repository.ContactoTemplateRepository;
import org.sanidadmadrid.cloud.webflux.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuariosService {

	private static Logger LOGGER = LoggerFactory.getLogger(UsuariosService.class);

	@Autowired
	private ContactoTemplateRepository contactoTemplateRepository;
	
	@Autowired
	private TaskEventService tes;

	private ReactiveMongoTemplate mongoTemplateContacto;

	public UsuariosService(ReactiveMongoTemplate mongoTemplateContacto, UsuarioRepository usuarioRepository) {
		this.mongoTemplateContacto = this.mongoTemplateContacto;
		this.usuarioRepository = usuarioRepository;

	}

	// @Autowired

	private UsuarioRepository usuarioRepository;

	public Flux<Usuario> listaUsuariosStream() {
		return usuarioRepository.findWithTailableCursorBy().delayElements(Duration.ofSeconds(2));
	}

	public Flux<Usuario> listUsuarios() {
		return contactoTemplateRepository.findAllUsuarios();
	}

	public Mono<ResponseEntity<Usuario>> usuario(Usuario usuario) {
		Mono<ResponseEntity<Usuario>> rta = contactoTemplateRepository.saveUsuario(usuario)
				.map(contactoGuardado -> new ResponseEntity<Usuario>(contactoGuardado, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<Usuario>(HttpStatus.NOT_ACCEPTABLE));
		LOGGER.info(String.format("usuario creado:[%s], generamos los eventos asociados....", usuario));
		tes.generadorEventosAltaUsuario(usuario);
		return rta;
	}

}
