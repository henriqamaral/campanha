package br.com.torcedor.api.client;


import java.util.Collection;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.torcedor.api.model.Campanha;


/**
 * CampanhaServiceClient
 */
@FeignClient(name = "campanha-service", fallback = CampanhaServiceClientImpl.class)
public interface CampanhaServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/campanhas", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Collection<Campanha> listAll();

    @RequestMapping(method = RequestMethod.GET, value = "/campanhas/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String findCampanha(@PathVariable("id") String id);
    
}