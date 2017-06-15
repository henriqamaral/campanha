package br.com.campanha.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.campanha.api.model.Campanha;



public interface CampanhaRepository extends MongoRepository<Campanha, String>, CampanhaRepositoryCustom {



}
