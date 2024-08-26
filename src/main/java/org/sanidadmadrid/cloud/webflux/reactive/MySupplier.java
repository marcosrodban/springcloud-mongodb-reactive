package org.sanidadmadrid.cloud.webflux.reactive;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
public class MySupplier implements Supplier<String>{
	private static Logger LOGGER = LoggerFactory.getLogger(MySupplier.class);
	
	
	@NonNull
	private String semilla;
	private String[] texto = new String[] {"hola","mundo","cruel","aqui","estamos","y"};
	private int index = 0;
	private int iteracion = 0;
	

	@Override
	public String get() {
		
		
		String textoreturn = semilla + ": " + texto[index]+ iteracion;
		index++;
		index = index >5 ? 0 : index; 
		iteracion = index == 0 ? iteracion + 1 : iteracion;
		LOGGER.info(String.format("GENERAMOS TEXTO:[%s]", textoreturn));
		return textoreturn;
	}

}
