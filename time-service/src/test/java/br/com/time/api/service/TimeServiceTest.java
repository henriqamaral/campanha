package br.com.time.api.service;

import br.com.time.api.exception.ResourceNotFoundException;
import br.com.time.api.model.Time;
import br.com.time.api.repository.TimeRepository;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TimeServiceTest {

  @Mock TimeRepository repo;
  TimeService service;

  @Before
  public void setup() {
    initMocks(this);
    service = new TimeService(repo);
  }

  @Test
  public void shouldCreateTime() throws Exception{
    final Time t = new Time("Time 1");

    service.criar(t);

    verify(repo).save(t);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void shouldGetResourceNotFoundExceotionWhenNotFoundTeam() throws Exception {
  	when(repo.findById(anyString())).thenReturn(Optional.empty());

  	service.alterar(new Time("Time 1"));
	}

	@Test
	public void whenUpdateTeamShouldSave() throws Exception {
    final Time oldTeam = new Time("Time 1", "123");
    when(repo.findById(anyString())).thenReturn(Optional.of(oldTeam));

    final Time newTeam = new Time("Time 2", "123");

    service.alterar(newTeam);

    verify(repo).save(newTeam);
  }

	@Test
	public void getAllTeams() {
    when(repo.findAll()).thenReturn(Collections.singletonList(new Time("Team")));

    final Collection<Time> teams = service.listar();

    MatcherAssert.assertThat(teams, not(teams.isEmpty()));
  }

  @Test
  public void whenFindIdByAndExists() {
    when(repo.findById(anyString())).thenReturn(Optional.of(new Time("Team")));

    Optional<Time> team = service.recuperar("123");

    MatcherAssert.assertThat(team, not(team.isPresent()));
  }

  @Test
  public void whenFindIdByAndNotExists() {
    when(repo.findById(anyString())).thenReturn(Optional.empty());

    Optional<Time> team = service.recuperar("123");

    MatcherAssert.assertThat(team, is(not(team.isPresent())));
  }
}
