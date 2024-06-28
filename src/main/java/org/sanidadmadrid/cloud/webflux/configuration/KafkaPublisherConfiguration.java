package org.sanidadmadrid.cloud.webflux.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaPublisherConfiguration<V> {

	//@Value("${io.reflectoring.kafka.bootstrap-servers}")
	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String bootstrapServers;


	
	
	@Bean
	public Map<String, Object> producerJsonConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "1");//-1,1,all
		return props;
	}

//	@Bean
//	public KafkaTemplate<String, Object> kafkaJsonTemplate() {
//		KafkaTemplate kt = new KafkaTemplate<String,Object>(producerJsonFactory());
////		kt.setProducerListener(new ProducerListener() {
////			
////			
////		});
//		return kt;
//	}
//	
	///////////////////////////////////////////////////////////
	
	@Bean
	public ProducerFactory<String, V> producerJsonObjectFactory() {
		return new DefaultKafkaProducerFactory<String,V>(producerJsonConfigs());
	}


	
	@Bean
	public KafkaTemplate<String, V> kafkaTemplateEvents() {
		return new KafkaTemplate<String, V>(producerJsonObjectFactory());
	}

}
