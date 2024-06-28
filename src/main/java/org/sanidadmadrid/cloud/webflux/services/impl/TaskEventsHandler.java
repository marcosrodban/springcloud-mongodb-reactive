package org.sanidadmadrid.cloud.webflux.services.impl;

import org.sanidadmadrid.cloud.taskevents.events.EventTreatmentResult;
import org.sanidadmadrid.cloud.taskevents.events.TaskEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class TaskEventsHandler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TaskEventsHandler.class);

	public TaskEvent handleEvent(TaskEvent theTask) {
		String idTaskEvent = theTask.getIdTaskEventOrigin();
		EventTreatmentResult etr = theTask.getTratamientoEvento().getEventTreatmentResult();


		if (etr == EventTreatmentResult.OK) {
			LOGGER.info(String.format("Notificamos que el tratamiento del evento ha sido exitoso:[%s]",etr));
		} else {

		}

		return theTask;

	}
}
