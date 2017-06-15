package br.com.time.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.time.api.model.Time;



public interface TimeRepository extends MongoRepository<Time, String> {


}
