package com.cloud.filoft;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cloud.filoft.service.AWSService;



@SpringBootApplication
@EntityScan("com.cloud.filoft.model")
@EnableJpaRepositories("com.cloud.filoft.repository")
@ComponentScan(basePackages = {  "com.cloud.filoft.controller", "com.cloud.filoft.service", "com.cloud.filoft.config"})
public class App extends SpringBootServletInitializer
{
	
	@Resource
	AWSService awsservice;
	
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
    	return app.sources(App.class);
    }
}
