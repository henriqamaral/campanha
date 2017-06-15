package br.com.campanha.api.validator;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import br.com.campanha.api.client.TimeServiceClient;
import br.com.campanha.api.model.Campanha;
import br.com.campanha.api.model.Time;
import feign.FeignException;

/**
 * CampanhaValidator
 */
@Component
public class CampanhaValidator {

    private final Logger logger = LoggerFactory.getLogger(CampanhaValidator.class);

    @Autowired
    private TimeServiceClient timeServiceClient;
    public void validaCriacao(Campanha c, Errors errors) {
        //valida se time existe
        try {
            String time = timeServiceClient.findTime(c.getIdTime());
            if(time == null) {
                errors.rejectValue("idTime", null, "Time não existe");
            }
        } catch (FeignException f) {
            if(f.status() == 404) {
                errors.rejectValue("idTime", null, "Time não existe");
            }
        }
        
        //valida datas
        Date now = new Date();
        if(c.getInicio() != null && c.getFim() != null && c.getInicio().after(c.getFim())) {
            errors.rejectValue("inicio", null, "Data Inicio fora do prazo.");
        }
        if(now.after(c.getFim())) {
            errors.rejectValue("fim", null, "Data Fim fora do prazo.");
        }
    }
}