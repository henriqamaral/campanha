package br.com.torcedor.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.torcedor.api.Application;
import br.com.torcedor.api.client.TimeServiceClient;
import br.com.torcedor.api.model.Campanha;
import br.com.torcedor.api.model.Torcedor;
import br.com.torcedor.api.service.TorcedorService;
import br.com.torcedor.api.validator.TorcedorValidator;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;


/**
 * TimeControllerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DataMongoTest
public class TorcedorControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
	private TorcedorController controller;
    @Mock
	private TorcedorService service;
    @Mock
    private TorcedorValidator validator;
    
    private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .build();
	}

    @Test
	public void shouldCreateNewTime() throws Exception {

		final Torcedor t = new Torcedor();
        t.setNome("Torcedor");
        t.setDtNascimento(new Date());
        t.setEmail("torcedor@tmail.com");
        t.setIdTimeCoracao("12345");
		String json = mapper.writeValueAsString(t);
		mockMvc.perform(post("/torcedores").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

     @Test
	public void shouldFailWhenTimeIsNotValid() throws Exception {
		mockMvc.perform(post("/torcedores"))
				.andExpect(status().isBadRequest());
	}

    @Test
	public void shouldGetTimeById() throws Exception {
        final Torcedor t = new Torcedor();
        t.setId("12345");
        when(service.recuperar(t.getId())).thenReturn(t);

        mockMvc.perform(get("/" + t.getId()))
				.andExpect(jsonPath("$.id").value(t.getId()))
				.andExpect(status().isOk());
    }

    @Test
	public void shouldSuccesWhenAssociateTorcedorWithCampanha() throws Exception {
        final Torcedor t = new Torcedor();
        t.setId("12345");
        when(service.recuperar(t.getId())).thenReturn(t);

        mockMvc.perform(get("/" + t.getId() + "/associar/54321"))
				.andExpect(status().isOk());
    }
}