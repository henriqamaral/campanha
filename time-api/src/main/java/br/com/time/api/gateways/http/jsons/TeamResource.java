package br.com.time.api.gateways.http.jsons;

import br.com.time.api.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamResource {

  private String id;
  private String name;

  public TeamResource(final String name) {
    this.name = name;
  }

  public TeamResource(Team team) {
    this.id = team.getId();
    this.name = team.getName();
  }

  public Team toDomain() {
    return new Team(this.id, this.name);
  }
}
