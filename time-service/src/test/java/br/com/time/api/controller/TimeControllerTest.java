package br.com.time.api.controller;

import br.com.time.api.Application;
import br.com.time.api.model.Time;
import br.com.time.api.service.TimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DataMongoTest
public class TimeControllerTest {

  private static final ObjectMapper mapper = new ObjectMapper();
  private MockMvc mockMvc;
  @Mock private TimeService service;
  @InjectMocks private TimeController timeController;

  @Before
  public void setup() {
    initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(timeController).build();
  }

  @Test
  public void shouldCreateNewTeam() throws Exception {

    final Time t = new Time("Novo Time");
    String json = mapper.writeValueAsString(t);
    mockMvc
        .perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldFailWhenTeamIsNotValid() throws Exception {
		final Time t = new Time("");
		String json = mapper.writeValueAsString(t);
		mockMvc
				.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
  }

  @Test
  public void shouldReturnInternalErrorWhenPostATeam
			() throws Exception {
		final Time t = new Time("Novo Time");
		String json = mapper.writeValueAsString(t);
		doThrow(new Exception()).when(service).criar(any(Time.class));
		mockMvc
				.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isInternalServerError());
	}

  @Test
  public void shouldGetTimeById() throws Exception {
    final Time t = new Time("Time", "12345" );
    when(service.recuperar(t.getId())).thenReturn(Optional.of(t));

    mockMvc
        .perform(get("/teams/{id}", t.getId()))
        .andExpect(jsonPath("$.id").value(t.getId()))
        .andExpect(status().isOk());
  }
}
