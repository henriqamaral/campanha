package br.com.time.api.usecases;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.TeamGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class GetTeam {

  private final TeamGateway teamGateway;

  public Mono<Team> execute(final String id) {
    return teamGateway.get(id);
  }
}
