package br.com.time.api.gateways;

import br.com.time.api.domain.Team;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamGateway {

  Mono<Team> save(Team team);

  Mono<Team> get(String id);

  Flux<Team> getAll();

}
