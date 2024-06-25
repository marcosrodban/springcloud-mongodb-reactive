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
public class UsuariosService {

		@Autowired
		private ContactoTemplateRepository contactoTemplateRepository;
		
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
			return contactoTemplateRepository.saveUsuario(usuario)
					.map(contactoGuardado -> new ResponseEntity<Usuario>(contactoGuardado, HttpStatus.OK))
					.defaultIfEmpty(new ResponseEntity<Usuario>(HttpStatus.NOT_ACCEPTABLE));
		}

	}

