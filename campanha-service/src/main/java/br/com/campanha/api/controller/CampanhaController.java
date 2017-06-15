package br.com.campanha.api.controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.campanha.api.exception.ValidateException;
import br.com.campanha.api.model.Campanha;
import br.com.campanha.api.service.CampanhaService;
import br.com.campanha.api.validator.CampanhaValidator;

/**
 * CampanhaController
 */
@RestController
public class CampanhaController {

    private final Logger logger = LoggerFactory.getLogger(CampanhaController.class);

    @Autowired
    CampanhaService service;
	@Autowired
	CampanhaValidator validator;

    @RequestMapping(method = RequestMethod.GET)
	public Collection<Campanha> listar() {
		return service.listar();
	}

    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> recuperar(@PathVariable(value = "id") String id) {
		try {
            Campanha c = service.recuperar(id);
            if(c == null) {
                return new ResponseEntity( HttpStatus.NOT_FOUND);
            }
			return new ResponseEntity<Campanha>(c, HttpStatus.OK);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#buscar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<?> inserir(@Valid @RequestBody Campanha c, BindingResult result) {
		try {
			validator.validaCriacao(c, result);
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
		
		return new ResponseEntity<Campanha>(c, HttpStatus.OK);
	}
	

	@SuppressWarnings("rawtypes")
	@RequestMapping( method = RequestMethod.PUT)
	public ResponseEntity<?> alterar(@Valid @RequestBody Campanha c) {
		try {
			service.alterar(c);
		}  catch (Exception e) {
			logger.error(this.getClass().getSimpleName() + "#alterar", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}

	@ExceptionHandler(ValidateException.class)
    public void handle(HttpServletResponse res) throws IOException {
        res.sendError(400, "Explicit exception handler works!");
    }

}