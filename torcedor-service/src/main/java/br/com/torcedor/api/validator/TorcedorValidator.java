package br.com.torcedor.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import br.com.torcedor.api.client.CampanhaServiceClient;
import br.com.torcedor.api.client.TimeServiceClient;
import br.com.torcedor.api.model.Torcedor;
import br.com.torcedor.api.repository.TorcedorRepository;
import feign.FeignException;

/**
 * TorcedorValidator
 */
@Component
public class TorcedorValidator {

    @Autowired
    TorcedorRepository torcedorRepository;

    @Autowired
    private TimeServiceClient timeServiceClient;
    @Autowired
    private CampanhaServiceClient campanhaServiceClient;
    public void validaCriacao(Torcedor t, Errors errors) {

       
        try {
            String time = timeServiceClient.findTime(t.getIdTimeCoracao());
            if(time == null) {
                errors.rejectValue("idTimeCoracao", null, "Time não existe");
            }
        } catch (FeignException f) {
            if(f.status() == 404) {
                errors.rejectValue("idTimeCoracao", null, "Time não existe");
            }
        }
    }

    public void validaAssociacao(String idTorcedor, String idCampanha, Errors errors) {
        //verifica se a campanha existe
        try {
            String campanha = campanhaServiceClient.findCampanha(idCampanha);
            if(campanha == null) {
                errors.rejectValue("campanha", null, "Campanha não existe");
            }
        } catch (FeignException f) {
            if(f.status() == 404) {
                errors.rejectValue("campanha", null, "Campanha não existe");
            }
        }
    }
    
}