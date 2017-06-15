package br.com.torcedor.api.client;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.torcedor.api.model.Campanha;


/**
 * CampanhaServiceClient
 */
@Component
public class CampanhaServiceClientImpl implements CampanhaServiceClient {

    
    public Collection<Campanha> listAll() {
        return new ArrayList<>();
    }


    public String findCampanha(String id) {
        return null;
    }
    
}