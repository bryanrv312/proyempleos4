package net.brubio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 *class to configure external directory of images, that is, outside the static files
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Value("${jobsapp.path.imgs}")
	private String rutaImages;
	
	@Value("${jobsapp.path.cv}")
	private String rutaCv;
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) { 
		registry.addResourceHandler("/logos/**").addResourceLocations("file:c:/empleos/img-vacantes/");
		//registry.addResourceHandler("/logos/**").addResourceLocations(rutaImages);
		registry.addResourceHandler("/cv/**").addResourceLocations("file:" + rutaCv);
		}

}
