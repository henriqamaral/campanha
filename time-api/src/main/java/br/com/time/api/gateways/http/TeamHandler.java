package br.com.time.api.gateways.http;

import br.com.time.api.gateways.http.jsons.TeamResource;
import br.com.time.api.usecases.GetAllTeams;
import br.com.time.api.usecases.GetTeam;
import br.com.time.api.usecases.SaveTeam;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
public class TeamHandler {

  private final GetAllTeams getAllTeams;
  private final GetTeam getTeam;
  private final SaveTeam saveTeam;

  public Mono<ServerResponse> get(ServerRequest serverRequest) {

    return getTeam
        .execute(serverRequest.pathVariable("id"))
        .flatMap(team -> ok().body(Mono.just(new TeamResource(team)), TeamResource.class))
        .switchIfEmpty(notFound().build());
  }

  public Mono<ServerResponse> getAll(final ServerRequest serverRequest) {
    final Flux<TeamResource> teams = getAllTeams.execute().map(TeamResource::new);

    return ok().body(teams, TeamResource.class);
  }

  public Mono<ServerResponse> post(final ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(TeamResource.class)
        .flatMap(teamResource -> saveTeam.execute(teamResource.toDomain()))
        .flatMap(
            team ->
                ServerResponse.created(URI.create("/teams/" + team.getId()))
                    .body(Mono.just(new TeamResource(team)), TeamResource.class));
  }
}
