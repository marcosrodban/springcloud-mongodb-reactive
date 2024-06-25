package org.sanidadmadrid.cloud.webflux.repository;

import java.util.List;

import org.sanidadmadrid.cloud.webflux.documents.Contacto;
import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ContactoTemplateRepository  {

	@Autowired
    private ReactiveMongoTemplate template;

    public Flux<Contacto> findByTitleContains(String title, Pageable page) {
    	var reg = ".*" + title + ".*";
    	Query query = Query.query(
    			Criteria.where("id").is("idA").and("beesInA.id").is("idB").regex(reg)
    			).with(page);
        return template.find(query, Contacto.class);
    }

    public Flux<Contacto> findAll() {
        return template.findAll(Contacto.class);
    }
    
    public Flux<Usuario> findAllUsuarios() {
        return template.findAll(Usuario.class);
    }
    
    public Flux<Contacto> findAll(Contacto contacto) {
    	Query query = Query.query(
    			Criteria.where("nombre").is(contacto.getNombre()).and("email").is(contacto.getMyemail())
    			);
        return template.find(query,Contacto.class);
    }

    public Mono<Contacto> save(Contacto Contacto) {
        return template.save(Contacto);
    }
    
    public Mono<Usuario> saveUsuario(Usuario usuario) {
        return template.save(usuario);
    }

    public Flux<Contacto> saveAll(List<Contacto> data) {
        return Flux.fromIterable(data)
        		.flatMap(template::save);
    }

    public Mono<Contacto> findById(String id) {
        return template.findById(id, Contacto.class);
    }

    public Mono<Long> deleteById(String id) {
        //return template.remove(Contacto.class).matching(query(where("id").is(id))).all().map(DeleteResult::getDeletedCount)
    	Query query = Query.query(Criteria.where("id").is(id));
        return template.remove(query, Contacto.class)
        		.map(com.mongodb.client.result.DeleteResult::getDeletedCount);
    }

    @Tailable
	public Flux<Usuario> findWithTailableCursorBy() {
		return template.findAll(Usuario.class);
	}




    


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
