package br.com.torcedor.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.torcedor.api.model.Torcedor;



public interface TorcedorRepository extends MongoRepository<Torcedor, String> {

    Torcedor findByEmail(String email);
}
