package org.sanidadmadrid.cloud.webflux.repository;


import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;


import reactor.core.publisher.Flux;

@Repository
public interface UsuarioRepository extends ReactiveMongoRepository<Usuario,String> {




	@Tailable
    Flux<Usuario> findWithTailableCursorBy();

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
