package br.com.time.api.controller;

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

import br.com.time.api.Application;
import br.com.time.api.model.Time;
import br.com.time.api.service.TimeService;


import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TimeControllerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DataMongoTest
public class TimeControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
	private TimeController timeController;
    @Mock
	private TimeService service;
    

    private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(timeController)
            .build();
	}

    @Test
	public void shouldCreateNewTime() throws Exception {

		final Time t = new Time();
        t.setNome("Novo Time");
		String json = mapper.writeValueAsString(t);
		mockMvc.perform(post("/times").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

    @Test
	public void shouldFailWhenTimeIsNotValid() throws Exception {
		mockMvc.perform(post("/times"))
				.andExpect(status().isBadRequest());
	}

    @Test
	public void shouldGetTimeById() throws Exception {
        final Time t = new Time();
        t.setId("12345");
        when(service.recuperar(t.getId())).thenReturn(t);

        mockMvc.perform(get("/" + t.getId()))
				.andExpect(jsonPath("$.id").value(t.getId()))
				.andExpect(status().isOk());
    }

   
}