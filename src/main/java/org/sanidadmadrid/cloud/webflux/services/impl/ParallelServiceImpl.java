package org.sanidadmadrid.cloud.webflux.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.sanidadmadrid.cloud.webflux.documents.Contacto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParallelServiceImpl {
	
	
	private static Logger LOGGER = LoggerFactory.getLogger(ParallelServiceImpl.class);
	
	
	public void procesadoParalelo(){
		long tiempoini = System.currentTimeMillis();
		log.debug("comenzamos con el proceso: " );
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<CompletableFuture<Integer>> futures = numbers.stream()
                .map( number -> CompletableFuture.supplyAsync(() -> compute(number)) )
                .collect(Collectors.toList());
        // Combine all CompletableFuture objects into a single CompletableFuture
        log.debug("unimos todos los resultados....... " );
        
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // Wait for all computations to complete
        allFutures.join();
        // Aggregate the results
        int sum = futures.stream()
                .map(CompletableFuture::join)
                .reduce(0, Integer::sum);
        // Print the final result
        log.debug("Sum of numbers: " + sum+ " tiempo total:"+(System.currentTimeMillis() - tiempoini));
	}
	
	public void comparableFuture() {
		

		LOGGER.info(String.format("comenzamos el proceso"));
		
		CompletableFuture<List<Contacto>> future1  
		  = CompletableFuture.supplyAsync( ()-> consultarContactos("marcos"));
		
		CompletableFuture<List<Contacto>> future2  
		  = CompletableFuture.supplyAsync( ()-> consultarContactos("marta"));
		future1.whenComplete( (list,ex) -> {
			LOGGER.info(String.format("EJECUTAMOS EL whenComplete:[%s]", list));
			
		});
		

		
		
		LOGGER.info(String.format("creamos un cominedFuture"));
		//en este punto es donde se ejecutan las tareas en paralelo
		CompletableFuture<Void> combinedFuture 
		  = CompletableFuture.allOf(future1, future2);

		/*log.debug("vamos a hacer el geet: " );
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
		
		log.debug("get hecho: " );
		
		
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
			log.debug("combined: " +  combined);
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
        	log.debug("comenzamos con el numero: " + number);
            Thread.sleep(1000);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the result
        log.debug("terminamos con el numero: " + number+" ,resultado:"+number * number);
        return number * number;
    }

}
