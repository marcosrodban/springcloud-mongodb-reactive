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
	
	public void procesadoParalelo(){
		long tiempoini = System.currentTimeMillis();
		System.out.println("comenzamos con el proceso: " );
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<CompletableFuture<Integer>> futures = numbers.stream()
                .map( number -> CompletableFuture.supplyAsync(() -> compute(number)) )
                .collect(Collectors.toList());
        // Combine all CompletableFuture objects into a single CompletableFuture
        System.out.println("unimos todos los resultados....... " );
        
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // Wait for all computations to complete
        allFutures.join();
        // Aggregate the results
        int sum = futures.stream()
                .map(CompletableFuture::join)
                .reduce(0, Integer::sum);
        // Print the final result
        System.out.println("Sum of numbers: " + sum+ " tiempo total:"+(System.currentTimeMillis() - tiempoini));
	}
	
	public void comparableFuture() {
		

		LOGGER.info(String.format("comenzamos el proceso"));
		
		CompletableFuture<List<Contacto>> future1  
		  = CompletableFuture.supplyAsync( ()-> consultarContactos("marcos"));
		
		CompletableFuture<List<Contacto>> future2  
		  = CompletableFuture.supplyAsync( ()-> consultarContactos("marta"));
		

		
		
		LOGGER.info(String.format("creamos un cominedFuture"));
		//en este punto es donde se ejecutan las tareas en paralelo
		CompletableFuture<Void> combinedFuture 
		  = CompletableFuture.allOf(future1, future2);

		/*System.out.println("vamos a hacer el geet: " );
		long tiempoini = System.currentTimeMillis();
		try {
			combinedFuture.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("get hecho: " );
		
		
*/
		
		 
		 LOGGER.info(String.format("Realizamos un combinedFuture.get()"));
		/*try {
			//en este punto se recoge el resultado y se espera si es necesario 
			//hasta que terminen ambas tareas
			
			//combinedFuture.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		 LOGGER.info(String.format("fin de combinedFuture.get()"));
		 LOGGER.info(String.format("vamoa a realizar un combinedFuture.join()"));
		 //con el join espera a que acaben las tareas para unirlas
		//combinedFuture.join();
		LOGGER.info(String.format("fin decombinedFuture.join()"));
		/*try {
			LOGGER.info(String.format("vamoa a realizar un future1.get():[%s]",future1.get()));
			LOGGER.info(String.format("vamoa a realizar un future2.get():[%s]",future2.get()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}*/

		
/*
		
		List<Contacto> combined;
		try {
			combined = Stream.of(future1.get(), future2.get())
					.flatMap(List::stream)
			        .collect(Collectors.toList());
			System.out.println("combined: " +  combined);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	*/	
		
		
	}
	
	
	private List<Contacto> consultarContactos(String nombre){
		  try {
			  LOGGER.info(String.format("buscamos contactos para :[%s]",nombre));
			Thread.sleep(5000);
			Contacto c = new Contacto();
			c.setNombre(nombre + Math.random());
			Contacto c1 = new Contacto();
			c1.setNombre(nombre + Math.random());
			LOGGER.info(String.format("contactos encontrados c1 :[%s] , c2:[%s]",c,c1));
			return Arrays.asList(c,c1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
		
	}


    // Example computation method
    private static int compute(int number) {
        // Simulate some time-consuming computation
        try {
        	System.out.println("comenzamos con el numero: " + number);
            Thread.sleep(1000);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the result
        System.out.println("terminamos con el numero: " + number+" ,resultado:"+number * number);
        return number * number;
    }

	public Mono<ResponseEntity<Usuario>> usuario(Usuario usuario) {
		return contactoTemplateRepository.saveUsuario(usuario)
				.map(contactoGuardado -> new ResponseEntity<Usuario>(contactoGuardado, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<Usuario>(HttpStatus.NOT_ACCEPTABLE));
	}

}
