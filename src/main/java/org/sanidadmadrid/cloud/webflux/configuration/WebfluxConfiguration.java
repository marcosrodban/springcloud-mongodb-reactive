package org.sanidadmadrid.cloud.webflux.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFlux
@Configuration
public class WebfluxConfiguration  implements WebFluxConfigurer {
	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/public", "classpath:/static/");
	}
	
//	@Bean
//	public RouterFunction<ServerResponse> htmlRouter(
//	  @Value("classpath:/public/index.html") Resource html) {
//	    return route(GET("/"), request
//	      -> ok().contentType(MediaType.TEXT_HTML).syncBody(html)
//	    );
//	}
	
	
	// para servir contenido estatico con la anotación EnableWebFlux
	@Bean
	public RouterFunction<ServerResponse> imgRouter() {
	    return RouterFunctions
	      .resources("/public/**", new ClassPathResource("/"));
	}
}
