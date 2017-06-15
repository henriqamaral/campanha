package br.com.time.api.controller;

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

import br.com.time.api.exception.ValidateException;
import br.com.time.api.model.Time;
import br.com.time.api.service.TimeService;

/**
 * TimeController
 */
@RestController
public class TimeController {

    private final Logger logger = LoggerFactory.getLogger(TimeController.class);

    @Autowired
    TimeService service;

    @RequestMapping(method = RequestMethod.GET)
	public Collection<Time> listar() {
		return service.listar();
	}

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> recuperar(@PathVariable(value = "id") String id) {
		try {
            Time t = service.recuperar(id);
            if(t == null) {
                return new ResponseEntity( HttpStatus.NOT_FOUND);
            }
			return new ResponseEntity<Time>(t, HttpStatus.OK);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#buscar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<?> inserir(@Valid @RequestBody Time c, BindingResult result) {
		try {
			//validator.validaCriacao(c, result);
			if(!result.hasErrors()) {
				service.criar(c);
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
		
		return new ResponseEntity<Time>(c, HttpStatus.OK);
	}
	

	@SuppressWarnings("rawtypes")
	@RequestMapping( method = RequestMethod.PUT)
	public ResponseEntity<?> alterar(@Valid @RequestBody Time c) {
		try {
			service.alterar(c);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#alterar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}

}