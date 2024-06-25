package org.sanidadmadrid.cloud.webflux.services.impl;


import org.sanidadmadrid.cloud.taskevents.events.TaskEvent;
import org.springframework.stereotype.Service;

@Service
public class TaskEventsHandler {
	
	public TaskEvent handleEvent(TaskEvent theTask) {
		String idTaskEvent = theTask.getIdTaskEventOrigin();
		String estadoEvento = theTask.getTratamientoEvento().getEstado();
		if(estadoEvento.equals("OK")){
			
		}else {
			
		}
		
		
		return theTask;
		
	}
}
