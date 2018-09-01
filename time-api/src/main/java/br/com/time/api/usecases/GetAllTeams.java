package br.com.time.api.usecases;

import br.com.time.api.domain.Team;
import br.com.time.api.gateways.TeamGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Component
public class GetAllTeams {

  private final TeamGateway teamGateway;

  public Flux<Team> execute() {
    return teamGateway.getAll();
  }
}
