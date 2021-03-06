package br.com.campanha.api.client;

import java.util.Collection;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * TimeServiceClient
 */
@FeignClient(name = "time-service", fallback = TimeServiceClientImpl.class )
public interface TimeServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/times/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String findTime(@PathVariable("id") String id);
    
}