package org.sanidadmadrid.cloud.webflux.documents;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Document(collection="usuarios")
public class Usuario {
	

	@Id
	private String id;
	
	
	
	//private List<Contacto> contacto;
	
//	
//	@Field("idcontacto")
//	@DocumentReference(lookup="{'_id': ?#{#target}}")
//	private Contacto contacto;
	
	//con esta anotación te crea sólo el objectId si no se pone te crea todo un objeto contacto
	@DocumentReference
	private Contacto contacto;
	
	private String nombre, apellido1;





	

}
