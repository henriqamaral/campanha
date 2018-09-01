package br.com.time.api.gateways.http;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.http.jsons.TeamResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@RunWith(SpringRunner.class)
@WebFluxTest
@Import(TeamRouter.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamRouterTest {

  @MockBean private TeamHandler teamHandler;

  @Autowired private WebTestClient webTestClient;

  @Test
  public void givenTeam_should_return_id_with_status_created() {

    TeamResource teamResource = new TeamResource("Team");

    Mockito.when(teamHandler.post(Mockito.any(ServerRequest.class)))
        .thenReturn(
            ServerResponse.created(URI.create("/teams/1"))
                .body(Mono.just(new TeamResource(new Team("1", "Team"))), TeamResource.class));

    webTestClient
        .post()
        .uri("/teams")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.just(teamResource), TeamResource.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo("1");
  }
}
