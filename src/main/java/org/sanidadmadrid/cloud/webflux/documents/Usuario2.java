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

@Getter
@Setter
@Document(collection="usuarios")
public class Usuario2 {
	

	@Id
	private String id;
	
	//private List<Contacto> contacto;
	
	
	@Field("idcontacto")
	@DocumentReference(lookup="{'_id': ?#{#target}}")
	private Contacto contacto;
	
	
	private String nombre, apellido1;


	@Override
	public String toString() {
		return "Usuario [id=" + id + ", contacto=" + contacto + ", nombre=" + nombre + ", apellido1=" + apellido1 + "]";
	}




	public Contacto getContacto() {
		return contacto;
	}


	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}


	
	
	

}
