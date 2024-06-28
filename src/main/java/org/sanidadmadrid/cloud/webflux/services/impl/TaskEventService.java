package org.sanidadmadrid.cloud.webflux.services.impl;

import java.util.UUID;

import org.sanidadmadrid.cloud.taskevents.events.MailEvent;
import org.sanidadmadrid.cloud.taskevents.events.SMSEvent;
import org.sanidadmadrid.cloud.taskevents.events.TaskEvent;
import org.sanidadmadrid.cloud.taskevents.events.TaskEventHandler;
import org.sanidadmadrid.cloud.webflux.documents.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaskEventService {
	
	@Autowired
	private KafkaPublisherServiceImpl<SMSEvent> kakfaPublisherSMSService;
	
	@Autowired
	private KafkaPublisherServiceImpl<MailEvent> kakfaPublisherMailService;
	
	
	@Value("${aplicacion.kafka.topic-sms-name}")
	String topicSMS;
	
	@Value("${aplicacion.kafka.topic-mail-name}")
	String topicMail;
	
	private static Logger LOGGER = LoggerFactory.getLogger(TaskEventService.class);
	public void generadorEventosAltaUsuario(Usuario usuario) {
		String idEventoOrigen = UUID.randomUUID().toString();
		TaskEvent te = new TaskEvent();
		te.setTaskEventHandler(new TaskEventHandler("http://localhost:8899/mongodb/handletaskevent"));
		
		
		MailEvent me = new MailEvent();
		me.setIdOrigen(idEventoOrigen);
		me.setMail(String.format("BIENVENIDO USUARIO:[%s]", usuario));
		me.setTaskEvent(te);
		SMSEvent ne = new SMSEvent();
		ne.setIdOrigen(idEventoOrigen);
		ne.setSms("NOTIFICACION DE BIENVENIDA AL USUARIO:[%s]");
		ne.setTaskEvent(te);
		
		kakfaPublisherMailService.sendEvent(me,topicMail);
		kakfaPublisherSMSService.sendEvent(ne,topicSMS);
		
		
		
		LOGGER.info(String.format("usuario creado:[%s], generamos los eventos asociados....", usuario));
	}

}
