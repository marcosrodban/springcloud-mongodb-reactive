package org.sanidadmadrid.cloud.webflux.configuration;



import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.sanidadmadrid.cloud.webflux.documents.Contacto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.config.EnableWebFlux;


@Configuration
public class MongoConfiguration extends AbstractReactiveMongoConfiguration  {
	
	@Autowired
	private  CustomMongoProperties customMongoProperties;



	@Primary
    @Bean(name="mongoClientContacto")
    public MongoClient reactiveMongoClient() {
       // return MongoClients.create("mongodb://root:pass@192.168.1.127:27017/");
        return MongoClients.create(createMongoClientSettings(customMongoProperties.getContacto()));
    }

	@Primary
    @Bean(name="mongoTemplateContacto")
    public ReactiveMongoTemplate reactiveMongoTemplate() {
       return new ReactiveMongoTemplate(reactiveMongoClient(), customMongoProperties.getContacto().getDatabase());
       //return MongoClients.create(createMongoClientSettings(customMongoProperties.getContacto()));
    }
    
    private MongoClientSettings createMongoClientSettings(MongoProperties mongoProperties){

        ConnectionString ConnectionString = new ConnectionString(mongoProperties.getUri());
        System.out.println(String.format("uri:[%s], database:[%s]", mongoProperties.getUri(),mongoProperties.getDatabase()));

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                                   .readConcern(ReadConcern.DEFAULT)
                                   .writeConcern(WriteConcern.MAJORITY)
                                   //.readPreference(ReadPreference.primary())
                                   .applyConnectionString(ConnectionString)
                                   .build();
        return mongoClientSettings;
     }

	@Override
	protected String getDatabaseName() {
		return "admin";
	}
	
	


//    @Bean
//    @ConditionalOnProperty(prefix = "job.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
//    public CommandLineRunner loadData(UserRepository repository) {
//        return (args) -> {
//            // save a couple of users
//            var users = Flux.just(
//                    new User("Gökhan", ThreadLocalRandom.current().nextInt(1, 100)),
//                    new User("Betül", ThreadLocalRandom.current().nextInt(1, 100)),
//                    new User("Zühtü", ThreadLocalRandom.current().nextInt(1, 100))
//            );
//            repository.saveAll(users).subscribe();
//        };
//    }
}