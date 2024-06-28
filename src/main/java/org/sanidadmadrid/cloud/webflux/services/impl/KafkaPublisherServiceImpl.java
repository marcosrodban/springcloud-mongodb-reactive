package org.sanidadmadrid.cloud.webflux.services.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;

import org.springframework.stereotype.Service;

@Service
public class KafkaPublisherServiceImpl<V> {

	private static final Logger logger = LoggerFactory.getLogger(KafkaPublisherServiceImpl.class);
	
	@Autowired
	private KafkaTemplate<String, V> kafkaTemplateSMSEvents;
	
	public CompletableFuture<SendResult<String, V>>  sendEvent(V genericEvent,String theTopic) {
		logger.info(String.format("#### -> Producing message -> %s", genericEvent));
		List <Header> headers = new ArrayList<>();
		headers.add(new RecordHeader(KafkaHeaders.TOPIC, theTopic.getBytes()));
		headers.add(new RecordHeader(KafkaHeaders.KEY, "baeldung.com".getBytes()));
		headers.add(new RecordHeader(KafkaHeaders.CORRELATION_ID, "baeldung.com".getBytes()));
		headers.add(new RecordHeader("someOtherHeader", "someValue".getBytes()) );
		ProducerRecord<String,V> pr = new ProducerRecord<String, V>(theTopic,null,null, genericEvent,headers);
		
		CompletableFuture<SendResult<String, V>> response = this.kafkaTemplateSMSEvents.send(pr);
		
		 try {
			
			response.whenComplete((resultado,ex)->{
			    if (ex == null) {
			    	logger.info(String.format("Evento Encolado correctamente: [%s] con el offset:[%s]", genericEvent,resultado.getRecordMetadata().offset()),ex);
		         } else {
		        	 logger.error(String.format("Error al mandar el evento: [%s]", genericEvent),ex);
		         
		         }
				
				
			});
			SendResult<String, V> result = response.get();
			ProducerRecord<String, V> producerRecord = result.getProducerRecord();
			RecordMetadata rm = result.getRecordMetadata();
			logger.info(String.format("RecordMetadata: [%s]", rm));
			logger.info(String.format("producerRecord: [%s]", producerRecord));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 logger.info(String.format("response ack: [%s]", response));
		return response;
	}
	


}
