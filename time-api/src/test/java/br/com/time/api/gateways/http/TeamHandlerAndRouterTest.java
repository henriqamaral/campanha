package br.com.time.api.gateways.http;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.http.jsons.TeamResource;
import br.com.time.api.gateways.mongodb.repositories.TeamRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamHandlerAndRouterTest {

  @Autowired TeamRepository teamRepository;
  @Autowired private WebTestClient webTestClient;

  @Test
  public void given_id_should_return_team() {

    final Team team = teamRepository.save(new Team("Team")).block();

    webTestClient
        .get()
        .uri("/teams/{id}", team.getId())
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isNotEmpty()
        .jsonPath("$.name")
        .isEqualTo("Team");
  }

  @Test
  public void given_invalid_id_should_not_return_team() {

    webTestClient
        .get()
        .uri("/teams/{id}", "abc")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  public void given_team_should_created() {

    TeamResource teamResource = new TeamResource("Team");

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
        .jsonPath("$.name")
        .isEqualTo("Team");
  }

  @Test
  public void should_return_all_teams() {

    final Team team = teamRepository.save(new Team("Team")).block();

    webTestClient
        .get()
        .uri("/teams")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBodyList(TeamResource.class);
  }
}
