package br.com.time.api.gateways.mongodb.repositories;

import br.com.time.api.domain.Team;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends ReactiveMongoRepository<Team, String> {
}
