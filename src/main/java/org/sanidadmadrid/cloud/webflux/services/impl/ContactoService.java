package org.sanidadmadrid.cloud.webflux.services.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.sanidadmadrid.cloud.webflux.documents.Contacto;
import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.sanidadmadrid.cloud.webflux.repository.ContactoRepository;
import org.sanidadmadrid.cloud.webflux.repository.ContactoTemplateRepository;
import org.sanidadmadrid.cloud.webflux.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContactoService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ContactoService.class);

	@Autowired
	private ContactoTemplateRepository contactoTemplateRepository;
	
	private ReactiveMongoTemplate mongoTemplateContacto;

	public ContactoService(ReactiveMongoTemplate mongoTemplateContacto, ContactoRepository contactoRepository,UsuarioRepository usuarioRepository) {
		this.contactoRepository = contactoRepository;
		this.mongoTemplateContacto = this.mongoTemplateContacto;
		this.usuarioRepository = usuarioRepository;

	}

	// @Autowired

	private ContactoRepository contactoRepository;
	
	private UsuarioRepository usuarioRepository;

	public Flux<Contacto> listContactos() {
		return contactoTemplateRepository.findAll();

	}

	public Mono<ResponseEntity<Contacto>> contacto(String id) {
		return contactoTemplateRepository.findById(id)
				.map(contacto -> new ResponseEntity<Contacto>(contacto, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<Contacto>(HttpStatus.NOT_FOUND));

	}

	public Flux<Contacto> contactoNombreEmail(Contacto c) {

		return contactoTemplateRepository.findAll(c);

	}

	public Flux<Contacto> contactoEmail(String email) {
		Contacto c = new Contacto();
		c.setMyemail(email);
		return contactoRepository.findAll(Example.of(c));

	}

	public Flux<Contacto> findByParameter(String email) {
		Contacto c = new Contacto();
		c.setMyemail(email);
		return contactoRepository.findAll(Example.of(c));
	}

	public Mono<ResponseEntity<Contacto>> contacto(@RequestBody Contacto contacto) {
		return contactoRepository.insert(contacto)
				.map(contactoGuardado -> new ResponseEntity<Contacto>(contactoGuardado, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<Contacto>(HttpStatus.NOT_ACCEPTABLE));

	}

	public Mono<ResponseEntity<Contacto>> updateContacto(@RequestBody Contacto contacto) {
		return contactoRepository.findById(contacto.getId()).flatMap(dbcontacto -> {
			dbcontacto.setMyemail(contacto.getMyemail());
			dbcontacto.setNombre(contacto.getNombre());
			dbcontacto.setTelefono(contacto.getTelefono());
			return contactoRepository.save(dbcontacto);
		}).map(contactoGuardado -> new ResponseEntity<Contacto>(contactoGuardado, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<Contacto>(HttpStatus.NOT_ACCEPTABLE));

	}

	public Mono<Void> delete(@RequestBody Contacto contacto) {
		return contactoRepository.delete(contacto);

	}

	public Flux<Usuario> listaUsuariosStream() {
		return usuarioRepository.findWithTailableCursorBy().delayElements(Duration.ofSeconds(2));
	}

	public Flux<Usuario> listUsuarios() {
		return contactoTemplateRepository.findAllUsuarios();
	}
	
	
	



	public Mono<ResponseEntity<Usuario>> usuario(Usuario usuario) {
		return contactoTemplateRepository.saveUsuario(usuario)
				.map(contactoGuardado -> new ResponseEntity<Usuario>(contactoGuardado, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<Usuario>(HttpStatus.NOT_ACCEPTABLE));
	}

}
