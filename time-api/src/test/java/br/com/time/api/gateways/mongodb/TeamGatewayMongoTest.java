package br.com.time.api.gateways.mongodb;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.mongodb.repositories.TeamRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.MockitoAnnotations.initMocks;

class TeamGatewayMongoTest {

  @InjectMocks private TeamGatewayMongo teamGateway;

  @Mock private TeamRepository teamRepository;

  @Before
  void setup() {
    initMocks(this);
  }

  @Test
  void givenTeam_whenSave_success() {
    String teamID = "1234";

    Mockito.when(teamRepository.save(Mockito.any(Team.class)))
        .thenReturn(Mono.just(new Team(teamID, "Team")));

    Mono<Team> teamMono = teamGateway.save(new Team(teamID, "Team"));

    StepVerifier.create(teamMono)
        .assertNext(team -> Assert.assertEquals(teamID, team.getId()))
        .expectComplete()
        .verify();
  }
}
