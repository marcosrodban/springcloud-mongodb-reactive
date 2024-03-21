package org.sanidadmadrid.cloud.webflux.documents;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="usuarios")
public class Usuario {
	

	@Id
	private String id;
	
	
	
	//private List<Contacto> contacto;
	
	
	@Field("idcontacto")
	@DocumentReference(lookup="{'_id': ?#{#target}}")
	private Contacto contacto;
	
	
	private String nombre, apellido1;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}




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


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellido1() {
		return apellido1;
	}


	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	
	
	

}
