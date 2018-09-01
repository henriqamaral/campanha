package br.com.time.api.usecases;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.TeamGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SaveTeam {

  private final TeamGateway teamGateway;

  public Mono<Team> execute(final Team team) {
    return teamGateway.save(team);
  }
}
