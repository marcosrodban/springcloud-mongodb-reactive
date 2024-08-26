package org.sanidadmadrid.cloud.webflux.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="contacto")
public class Contacto {
	
	@Id
	private String id;
	
	//@Field("email")
	private String email;
	
	private String myemail;
	
	private String telefono,nombre;
	
	
	

}
