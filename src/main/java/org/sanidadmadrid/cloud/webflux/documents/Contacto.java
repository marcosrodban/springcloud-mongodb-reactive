package org.sanidadmadrid.cloud.webflux.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="contacto")
public class Contacto {
	
	@Id
	private String id;
	
	@Field("email")
	private String myemail;
	
	private String telefono,nombre;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
		
	public String getMyemail() {
		return myemail;
	}

	public void setMyemail(String myemail) {
		this.myemail = myemail;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Contacto [id=" + id + ", email=" + myemail + ", telefono=" + telefono + ", nombre=" + nombre + "]";
	}
	
	
	
	

}
