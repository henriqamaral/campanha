package br.com.time.api.controller;

import br.com.time.api.exception.ValidateException;
import br.com.time.api.model.Time;
import br.com.time.api.service.TimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("teams")
@Api(value = "Teams", description = "Rest API for team management")
public class TimeController {

  private final Logger logger = LoggerFactory.getLogger(TimeController.class);

  @Autowired TimeService service;

  @SuppressWarnings("rawtypes")
  @PutMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> alterar(@Valid @RequestBody Time c) {
    try {
      service.alterar(c);
    } catch (Exception e) {
      logger.error(this.getClass().getSimpleName() + "#alterar", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity(HttpStatus.OK);
  }

  @ExceptionHandler(ValidateException.class)
  public void handle(HttpServletResponse res) throws IOException {
    res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erro validacao");
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get all teams", response = Time.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Teams found")})
  public Collection<Time> listar() {
    return service.listar();
  }

  @SuppressWarnings("rawtypes")
  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> post(@Valid @RequestBody Time c, BindingResult result) {
    try {
      if (!result.hasErrors()) {
        service.criar(c);
      } else {
        throw new ValidateException("Erro validacao", result);
      }

    } catch (ValidateException e) {
      throw e;
    } catch (Exception e) {
      logger.error(this.getClass().getSimpleName() + "#inserir", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(c, HttpStatus.OK);
  }

  @SuppressWarnings("rawtypes")
  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> recuperar(@PathVariable(value = "id") String id) {
    try {
      return service
          .recuperar(id)
          .map(time -> new ResponseEntity<>(time, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      logger.error(this.getClass().getSimpleName() + "#buscar", e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
