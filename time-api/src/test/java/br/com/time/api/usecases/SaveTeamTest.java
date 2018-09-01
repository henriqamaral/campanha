package br.com.time.api.usecases;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.TeamGateway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.MockitoAnnotations.initMocks;

public class SaveTeamTest {

  @InjectMocks private SaveTeam saveTeam;

  @Mock private TeamGateway teamGateway;

  @Test
  public void givenTeam_whenSave_success() {
    String teamID = "1234";
    Mockito.when(teamGateway.save(Mockito.any(Team.class)))
        .thenReturn(Mono.just(new Team(teamID, "Team")));

    Mono<Team> teamMono = saveTeam.execute(new Team(teamID, "Team"));

    StepVerifier.create(teamMono)
        .assertNext(team -> Assert.assertEquals(teamID, team.getId()))
        .expectComplete()
        .verify();
  }

  @Before
  public void setup() {
    initMocks(this);
  }
}
