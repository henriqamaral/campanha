package br.com.campanha.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

import br.com.campanha.api.message.CampanhaMessage;

/**
 * Application
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableBinding(CampanhaMessage.class)
public class Application {
    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}

