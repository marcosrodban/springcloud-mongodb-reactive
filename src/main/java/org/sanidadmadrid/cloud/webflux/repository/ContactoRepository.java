package org.sanidadmadrid.cloud.webflux.repository;

import org.sanidadmadrid.cloud.webflux.documents.Contacto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Repository
public interface ContactoRepository extends ReactiveMongoRepository<Contacto,String> {



    Mono<Contacto> findAllByTelefonoOrNombre(String telefonoOrNombre);

//	public Flux<Contacto> findAll() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Mono<Contacto> findById(String id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Mono<Contacto> insert(Contacto contacto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Mono<Contacto> update(Contacto contacto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Mono<Contacto> save(Contacto dbcontacto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Mono<Contacto> delete(Contacto contacto) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
