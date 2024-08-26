package org.sanidadmadrid.cloud.webflux.handlers;

import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.common.KeyValue;

// Example of plugging in a custom handler that in this case will print a statement before and after all observations take place

@Slf4j
class ObserverHandler implements ObservationHandler<Observation.Context> {


    @Override
    public void onStart(Observation.Context context) {
        log.info("Before running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("After running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }

    private String getUserTypeFromContext(Observation.Context context) {
    	try {
            return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                    .filter(keyValue -> "userType".equals(keyValue.getKey()))
                    .map(KeyValue::getValue)
                    .findFirst()
                    .orElse("UNKNOWN");
    	}catch(Exception e) {
    		log.error("ERROR AL COGER LOS VALORES DE LA OBSERVACION:{}",context,e);
    		return null;
    	}

    }
}