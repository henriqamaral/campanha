package br.com.campanha.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.campanha.api.Application;
import br.com.campanha.api.exception.ValidateException;
import br.com.campanha.api.model.Campanha;
import br.com.campanha.api.service.CampanhaService;
import br.com.campanha.api.validator.CampanhaValidator;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;


/**
 * CampanhaControllerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DataMongoTest
public class CampanhaControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
	private CampanhaController campanhaController;
    @Mock
	private CampanhaService campanhaService;
    @Mock
    private CampanhaValidator validator;

    private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(campanhaController)
            .build();
	}

    @Test
	public void shouldGetCampanhaById() throws Exception {
        final Campanha campanha = new Campanha();
        campanha.setId("12345");
        when(campanhaService.recuperar(campanha.getId())).thenReturn(campanha);

        mockMvc.perform(get("/" + campanha.getId()))
				.andExpect(jsonPath("$.id").value(campanha.getId()))
				.andExpect(status().isOk());
    }

    @Test
	public void shouldRegisterNewCampanha() throws Exception {
        final Campanha campanha = new Campanha();
        campanha.setFim(getFimDate());
        campanha.setInicio(new Date());
        campanha.setNome("Camp 1");
        campanha.setIdTime("123456");

        String json = mapper.writeValueAsString(campanha);
        mockMvc.perform(post("/campanhas").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
    }

    @Test
	public void shouldFailOnValidationSaveCampanha() throws Exception {
        final Campanha campanha = new Campanha();
        campanha.setNome("Camp 1");
        String json = mapper.writeValueAsString(campanha);
        mockMvc.perform(put("/campanhas").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
    }

    @Test
	public void shouldFailOnValidationTryingToRegisterNewCampanha() throws Exception {

		final Campanha campanha = new Campanha();
		campanha.setNome("Camp 1");

		String json = mapper.writeValueAsString(campanha);

		mockMvc.perform(post("/campanhas").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is4xxClientError());
	}


    private Date getFimDate() {
        Calendar c = Calendar.getInstance();
        c.add(10, Calendar.DAY_OF_MONTH);
        return c.getTime();
    }

}