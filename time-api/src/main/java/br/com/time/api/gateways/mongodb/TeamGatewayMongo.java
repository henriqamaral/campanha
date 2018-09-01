package br.com.time.api.gateways.mongodb;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.TeamGateway;
import br.com.time.api.gateways.mongodb.repositories.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TeamGatewayMongo implements TeamGateway {

  private TeamRepository teamRepository;

  @Override
  public Mono<Team> save(final Team team) {
    return teamRepository.save(team);
  }

  @Override
  public Mono<Team> get(final String id) {
    return teamRepository.findById(id);
  }

  @Override
  public Flux<Team> getAll() {
    return teamRepository.findAll();
  }
}
