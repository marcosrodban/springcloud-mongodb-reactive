package org.sanidadmadrid.cloud.webflux.configuration;

import java.io.IOException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@Configuration
public class ObserverConfig {
    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
    
    
    // Monitoring beans
    @Bean
    ObservationRegistry observationRegistry() {
      return ObservationRegistry.create();
    }
    
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public RestTemplateBuilder restTemplateBuilder() {
		return new RestTemplateBuilder(customRestTemplateCustomizer());
	}

	
	
	@Bean
	public RestTemplateCustomizer customRestTemplateCustomizer() {
	    RestTemplateCustomizer rtc =  new  RestTemplateCustomizer() {
	    	    @Override
	    	    public void customize(RestTemplate restTemplate) {
	    	        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor () {
	    	        	
	    	        	
	    	            @Override
	    	            public ClientHttpResponse intercept(
	    	              HttpRequest request, byte[] body, 
	    	              ClientHttpRequestExecution execution) throws IOException {
	    	         
	    	                //logRequestDetails(request);
	    	                return execution.execute(request, body);
	    	            }
	    	        }
	    	        		
	    	        		
	    	        		
	    	        		);
	    	    }
	    };
	    return rtc;
	}
	


}