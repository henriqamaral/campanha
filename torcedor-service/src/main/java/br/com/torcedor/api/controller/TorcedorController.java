package br.com.torcedor.api.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.torcedor.api.exception.ValidateException;
import br.com.torcedor.api.model.Campanha;
import br.com.torcedor.api.model.Torcedor;
import br.com.torcedor.api.service.TorcedorService;
import br.com.torcedor.api.validator.TorcedorValidator;

/**
 * TorcedorController
 */
@RestController
public class TorcedorController {

    private final Logger logger = LoggerFactory.getLogger(TorcedorController.class);

    @Autowired
    TorcedorService service;
	@Autowired
	TorcedorValidator validator;

    @RequestMapping(method = RequestMethod.GET)
	public Collection<Torcedor> listar() {
		return service.listar();
	}

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> recuperar(@PathVariable(value = "id") String id) {
		try {
            Torcedor c = service.recuperar(id);
            if(c == null) {
                return new ResponseEntity( HttpStatus.NOT_FOUND);
            }
			return new ResponseEntity<Torcedor>(service.recuperar(id), HttpStatus.OK);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#buscar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<?> inserir(@Valid @RequestBody Torcedor o, BindingResult result) {
		try {
			validator.validaCriacao(o, result);
			if(!result.hasErrors()) {
				Collection<Campanha> ret = service.criar(o);
				if(ret != null) {
					return new ResponseEntity<Collection<Campanha>>(ret, HttpStatus.OK);
				} else {
					return new ResponseEntity<Torcedor>(o, HttpStatus.OK);
				}
				
			} else {
				throw new ValidateException("Erro validacao", result);
			}
			
		} catch (ValidateException e) {
			throw e;
		} 
        catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#inserir", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

	@SuppressWarnings("rawtypes")
	@RequestMapping( method = RequestMethod.PUT)
	public ResponseEntity<?> alterar(@Valid @RequestBody Torcedor c) {
		try {
			service.alterar(c);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#alterar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}/associar/{idCampanha}", method = RequestMethod.GET)
	//Associa Torcedor a uma campanha
	public ResponseEntity<?> associar(@PathVariable(value = "id") String id, 
			@PathVariable(value = "idCampanha") String idCampanha) {
		try {
			service.associar(id, idCampanha);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#associar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}

}