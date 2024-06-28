package org.sanidadmadrid.cloud.webflux.controller;


import org.sanidadmadrid.cloud.taskevents.events.TaskEvent;
import org.sanidadmadrid.cloud.webflux.services.impl.TaskEventsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/mongodb")
public class TaskEventController {
	private final Logger logger = LoggerFactory.getLogger(TaskEventController.class);
	
	@Autowired
	private TaskEventsHandler taskEventsHandler;
	
	
	@PostMapping("/handletaskevent")
	public ResponseEntity<TaskEvent> handleTaskEvent(@RequestBody TaskEvent taskEvent) {
		logger.info(String.format("Tratamos el eventTask:[%s] recibido por POST", taskEvent));
		return new ResponseEntity<TaskEvent>(taskEventsHandler.handleEvent(taskEvent), HttpStatus.OK);
	}
	
	

	
}