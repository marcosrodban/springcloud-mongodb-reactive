package org.sanidadmadrid.cloud.webflux.controller;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.sanidadmadrid.cloud.webflux.reactive.MySubscriber;
import org.sanidadmadrid.cloud.webflux.reactive.MySupplier;
import org.sanidadmadrid.cloud.webflux.services.impl.ContactoService;
import org.sanidadmadrid.cloud.webflux.services.impl.EchoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/mongodb")
public class StreamController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ContactoController.class);

	@Autowired
	private ContactoService contactoService;
	
	

	
	@GetMapping(value="/usuariosstream", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Usuario> listaUsuariosStream() {
	
		return contactoService.listaUsuariosStream();
	}
	
	@GetMapping(value="/usuariosstream2", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Usuario> listaUsuariosStream2() {
		 return Flux.interval(Duration.ofSeconds(5))
				 .flatMap(s -> contactoService.listUsuarios())
			      .log("Generado nuevo valor");
	}
	
	@GetMapping(value="/stream-num-random", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamNumRandom(){
		Random r = new Random();
		int low = 0;
		int high = 50;
		return Flux.fromStream(
				Stream.generate(() -> r.nextInt(high - low) + low)
				.map(s -> String.valueOf(s))
				.peek((msg) -> {
					LOGGER.info(msg);
				}))
				.map(s -> s)
				.delayElements(java.time.Duration.ofSeconds(1));
	}
	
	@GetMapping(value="/stream-texto", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> streamTexto(){
		MySupplier ms = new MySupplier("semilla");
		
		return Flux.fromStream(Stream.generate(ms))
				.map(dato ->ServerSentEvent.<String> builder()
				        .id(String.valueOf(dato))
				          .event("periodic-event")
				          .data( dato )
				          .build())
			.delayElements(java.time.Duration.ofSeconds(1));
	}
	
	
	
    @Observed(
            name = "user.name",
            contextualName = "child-->Grandchild",
            lowCardinalityKeyValues = {"userType", "userType2", "userType3"}
    )
	@GetMapping(value="/stream-zip", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> streamZip(){
		MySupplier ms = new MySupplier("semilla1");
		MySupplier ms2 = new MySupplier("semilla2");
		log.info("Probamos un flujo de stream con un zip final {}","hello");
		
		Flux<ServerSentEvent<String>> f1 =  Flux.fromStream(Stream.generate(ms))
			.delayElements(java.time.Duration.ofSeconds(1))
			.map(data -> ServerSentEvent.<String> builder()
		        .id(""+1)
		          .event("periodic-event")
		          .data( "f1: " + data )
		          .build());
		
		Flux<ServerSentEvent<String>> f2 =  Flux.fromStream(Stream.generate(ms2))
				.delayElements(java.time.Duration.ofSeconds(1))
				.map(data -> ServerSentEvent.<String> builder()
			        .id(""+1)
			          .event("periodic-event")
			          .data( "f2: " + data )
			          .build());	
		return Flux.zip(f1, f2, (f1data,f2data) -> {
			ServerSentEvent<String> sser = ServerSentEvent.<String> builder()
	        .id(""+1)
	          .event("periodic-event")
	          .data( "f1: " + f1data.data() + " f2: " +  f2data.data() )
	          .build();
			
			
			return sser;
		} );
	}
	@GetMapping(value="/stream-zip2", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamZip2(){
		MySupplier ms = new MySupplier("semilla1");
		MySupplier ms2 = new MySupplier("semilla2");
		Flux<ServerSentEvent<String>> f1 =  Flux.fromStream(Stream.generate(ms))
			.delayElements(java.time.Duration.ofSeconds(1))
			.map(data -> ServerSentEvent.<String> builder()
		        .id(""+1)
		          .event("periodic-event")
		          .data( "f1: " + data )
		          .build());
		
		Flux<ServerSentEvent<String>> f2 =  Flux.fromStream(Stream.generate(ms2))
				.delayElements(java.time.Duration.ofSeconds(1))
				.map(data -> ServerSentEvent.<String> builder()
			        .id(""+1)
			          .event("periodic-event")
			          .data( "f2: " + data )
			          .build());	
		return Flux.zip(f1, f2, (f1data,f2data) -> {
			return  "f1: " + f1data.data() + " f2: " +  f2data.data();
		} );
	}
	
	@GetMapping(value="/stream-buffer", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> streamBuffer(){
		MySupplier ms = new MySupplier("");
		// ms2 = new MySupplier("semilla2");
		Flux<ServerSentEvent<String>> f1 =  Flux.fromStream(Stream.generate(ms))
			.delayElements(java.time.Duration.ofSeconds(1))
			.buffer(Duration.ofSeconds(5))
			.map(list -> {
				String data = "";
				for(String datoList : list) {
					data = data +  "," + datoList;
				}
				return ServerSentEvent.<String> builder()
		        .id(""+1)
		          .event("periodic-event")
		          .data( "f1: " + data )
		          .build();	
			});
		
		return f1;
	}
	
	@GetMapping(value="/flatmap")
	public Flux<ServerSentEvent<String>> flatmap(){
		MySupplier ms = new MySupplier("");
		// ms2 = new MySupplier("semilla2");
		Flux<ServerSentEvent<String>> f1 = Flux
				.just("holala mundo como estas yo estoy yaya guay".split(" "))
				.delayElements(Duration.ofSeconds(2))
				.map(dato -> {
					LOGGER.info(String.format("HACEMOS UN map DEL ELEMENTO:[%s] -> [%s] ",dato,dato.toUpperCase()));
					return dato.toUpperCase();
				})
				.flatMap(dato -> {
					String[] split = dato.split("A");
					String datosHechosSpit = "";
					for(String unsplit : split) {
						datosHechosSpit+=","+unsplit;
					}
					LOGGER.info(String.format("dato:[%s] -> split:[%s]", dato,datosHechosSpit));
					
				return Flux.just(dato.split("A"));	
				})
				.map(dato -> ServerSentEvent.<String> builder()
					        .id(""+1)
					          .event("periodic-event")
					          .data( "f1: " + dato )
					          .build());
		return f1;
	}
	
	
	
	
	@GetMapping("/stream-sse")
	public Flux<ServerSentEvent<String>> streamEvents() {
	    return Flux.interval(Duration.ofSeconds(1))
	      .map(sequence -> ServerSentEvent.<String> builder()
	        .id(String.valueOf(sequence))
	          .event("periodic-event")
	          .data("SSE - " +  java.time.LocalDateTime.now() )
	          .build())
	      .log("Generado nuevo valor");
	}
	
	@GetMapping(value="/stream-range", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> streamEventsRange() {
		MySubscriber ms =  new MySubscriber();
	    Flux<Integer> flujo = Flux.range(0, 100)
	    		.delayElements(Duration.ofSeconds(1))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			
	    			LOGGER.info(String.format("hacemos el map : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    			
	    		})
	    		.doOnNext( d -> {
	    	    //	try {
	    				//Thread.sleep(3000);
	    		    	//LOGGER.info(String.format("DoOnNext : [%s]", d));
	    			//} catch (InterruptedException e) {
	    				//LOGGER.error(String.format("ERROR TRATANDO EL ELEMENTO  [%s]", d),e);
	    			//}

	    	    	
	    	    	
	    	    });
	    
	    		flujo.subscribe( a -> {
	    	    	try {
	    				Thread.sleep(3000);
	    		    	LOGGER.info(String.format("SUBSCRIBER CON RETARDO DE 3 seg. : [%s*%s] = [%s]", a,a,a*a));
	    			} catch (InterruptedException e) {
	    				LOGGER.error(String.format("ERROR TRATANDO EL ELEMENTO  [%s]", a),e);
	    			}
	    	    });
	    
	    		//flujo.subscribe(ms);
	    
	    	try {
				Thread.sleep(1);
				LOGGER.info(String.format("HACEMOS EL return...................."));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

	    return flujo.map(dato ->  ServerSentEvent.<String> builder()
		        .id(String.valueOf(dato))
		          .event("periodic-event")
		          .data("SSE - " +  java.time.LocalDateTime.now() + " dato: " + dato )
		          .build()); 
	    
	}
	
	@GetMapping(value="/stream-merge")
	public Flux<ServerSentEvent<String>> mergeStream() {
		MySupplier ms =  new MySupplier("supl1");
		MySupplier ms2 =  new MySupplier("supl2");
	    Flux<String> flujo1 = Flux.fromStream(Stream.generate(ms))
	    		.delayElements(Duration.ofSeconds(1))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo1 : [%s] -> [%s]", dato,dato.toUpperCase()));
	    			return dato.toUpperCase();
	    		});
	    Flux<String> flujo2 = Flux.fromStream(Stream.generate(ms2))
	    		.delayElements(Duration.ofSeconds(2))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo1 : [%s] -> [%s]", dato,dato.toUpperCase()));
	    			return dato.toUpperCase();
	    		});
	    
	    return Flux.merge(flujo1,flujo2).map(dato ->  {
			ServerSentEvent<String> sser = ServerSentEvent.<String> builder()
			        .id(""+1)
			          .event("periodic-event")
			          .data( "merge: " + dato )
			          .build();
					return sser;
				} );
	}
	
	
    @Observed(
            name = "user.name",
            contextualName = "child-->Grandchild",
            lowCardinalityKeyValues = {"userType", "userType2"}
    )
	@GetMapping(value="/collect")
	public Mono<ServerSentEvent<String>> collect(){
		MySupplier ms = new MySupplier("");
		// ms2 = new MySupplier("semilla2");
		Mono<ServerSentEvent<String>> f1 = Flux
				.just("holala mundo como estas yo estoy yaya guay".split(" "))
				.delayElements(Duration.ofSeconds(2))
				.map(dato -> {
					LOGGER.info(String.format("HACEMOS UN map DEL ELEMENTO:[%s] -> [%s] ",dato,dato.toUpperCase()));
					return dato.toUpperCase();
				})
				.collect( Collectors.toList() )
				.map(list -> {
					String data = "";
					for(String datoList : list) {
						data = data +  "," + datoList;
					}
					return ServerSentEvent.<String> builder()
					        .id(""+1)
					          .event("periodic-event")
					          .data( "f1: " + list )
					          .build();
				});
		LOGGER.info(String.format("SE HA REALIZADO EL COLLECT")) ;  
		return f1;
	}
	
	@GetMapping(value="/join")
	public Flux<ServerSentEvent<String>> join() {
		MySubscriber ms =  new MySubscriber();
	    Flux<Integer> flujo1 = Flux.range(0, 5)
	    		.delayElements(Duration.ofSeconds(1))
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo1 : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    		});
	    Flux<Integer> flujo2 = Flux.range(0, 5)
	    		.delayElements(Duration.ofSeconds(10))
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo2 : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    		});
	    Flux<ServerSentEvent<String>> flujozip = Flux.zip(flujo1,flujo2, (f1data,f2data) -> {
			ServerSentEvent<String> sser = ServerSentEvent.<String> builder()
	        .id(""+1)
	          .event("periodic-event")
	          .data( "f1: " + f1data + " f2: " +  f2data )
	          .build();
			return sser;
		} );
	    return flujozip;
	}
	
	
	@GetMapping(value="/merge")
	public Flux<ServerSentEvent<String>> merge() {
	    Flux<Integer> flujo1 = Flux.range(0, 5)
	    		.delayElements(Duration.ofSeconds(1))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo1 : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    		});
	    Flux<Integer> flujo2 = Flux.range(0, 5)
	    		.delayElements(Duration.ofSeconds(1))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo2 : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    		});
	    
	    return Flux.merge(flujo1,flujo2).map(dato ->  {
			ServerSentEvent<String> sser = ServerSentEvent.<String> builder()
			        .id(""+1)
			          .event("periodic-event")
			          .data( "merge: " + dato )
			          .build();
					return sser;
				} );
	}
	
	@GetMapping(value="/concat")
	public Flux<ServerSentEvent<String>> concat() {
	    Flux<Integer> flujo1 = Flux.range(0, 5)
	    		.delayElements(Duration.ofSeconds(1))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			
	    			LOGGER.info(String.format("hacemos el map flujo1 : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    			
	    		});
	    Flux<Integer> flujo2 = Flux.range(0, 5)
	    		.delayElements(Duration.ofSeconds(1))
	    		//.filter(d -> d%2 == 0)
	    		.map(dato -> {
	    			LOGGER.info(String.format("hacemos el map flujo2 : [%s] -> [%s]", dato,2*dato));
	    			return dato*2;
	    			
	    		});
	    return Flux.concat(flujo1,flujo2).map(dato ->  {
			ServerSentEvent<String> sser = ServerSentEvent.<String> builder()
			        .id(""+1)
			          .event("periodic-event")
			          .data( "merge: " + dato )
			          .build();
					return sser;
				} );
	}
	
	
	@GetMapping(value="/zipcollect")
	public Flux<String> zipcollect(){

		Mono<String> f1 =  Flux.range(0, 10)
			.delayElements(java.time.Duration.ofSeconds(1))
			.map(data -> {
				LOGGER.info(String.format("hacemos el map flujo1 : [%s] -> [%s]", data,data*2));
				return data*2;
			})
			.collect( Collectors.toList() )
			.map( list -> {
				String datoReturn = "";
				for(Integer dato : list) {
					datoReturn+= ","+dato;
				}
				LOGGER.info(String.format("juntamos todo en 1 flujo1 : [%s] ", datoReturn));
				return datoReturn;
			});		
		
		Mono<String> f2 =  Flux.range(0,3)
				.delayElements(java.time.Duration.ofSeconds(4))
				.map(data -> {
					LOGGER.info(String.format("hacemos el map flujo2 : [%s] -> [%s]", data,data*3));
					return data*3;
				})
				.collect( Collectors.toList() )
				.map( list -> {
					String datoReturn = "";
					for(Integer dato : list) {
						datoReturn+= ","+dato;
					}
					LOGGER.info(String.format("juntamos todo en 1 flujo2 : [%s] ", datoReturn));
					return datoReturn;
				});		

		return Flux.zip(f1, f2, (f1data,f2data) -> {
			LOGGER.info(String.format("realizamos el zip  flujo1 : [%s] , flujo2 [%s]", f1data,f2data));
			
			return  "f1: " + f1data + " f2: " +  f2data;
		} );
	}
	
	

	




}
