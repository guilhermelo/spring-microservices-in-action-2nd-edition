package com.optimagrowth.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.messaging.Source;


// Diz que o serviço fará o bind a um broker e que enviará as mensagens a partir de um conjunto de canais definidos na classe Source
//@EnableBinding(Source.class)
@SpringBootApplication
@RefreshScope
public class OrganizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizationServiceApplication.class, args);
	}

}
