package br.com.torcedor.api.service;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.torcedor.api.client.CampanhaServiceClient;
import br.com.torcedor.api.exception.ValidateException;
import br.com.torcedor.api.model.Campanha;
import br.com.torcedor.api.model.Torcedor;
import br.com.torcedor.api.repository.TorcedorRepository;

/**
 * TorcedorService
 */
@Service
public class TorcedorService {

    @Autowired
    TorcedorRepository repo;
    @Autowired
    CampanhaServiceClient campanhaServiceClient;

    public Collection<Torcedor> listar() {
        return repo.findAll();
    }
    public Collection<Campanha> criar(Torcedor c) {
        //verifica se existe o torcedor 
        Torcedor old = repo.findByEmail(c.getEmail());
        if(old != null) {
            //verifica se não tem campanha associadas
            if(old.getCampanhas() == null || old.getCampanhas().isEmpty()) {
                //retorna todas as campanhas
                Collection<Campanha> campanhas = campanhaServiceClient.listAll();
                return campanhas;
            } else {
                throw new ValidateException("Torcedor já cadastrado");
            }

        } else {
            repo.save(c);
        }

        return null;
        
    }

    public Torcedor recuperar(String id) {
        return repo.findOne(id);
    }

    public void alterar(Torcedor c) throws Exception {
        Torcedor old = repo.findOne(c.getId());
        if(old == null) {
            throw new Exception("Not Found");
        }
        old.setNome(c.getNome());
        //fazer chamada MQ para avisar ouvintes sobre alteracao da Torcedor
        repo.save(old);
    }

    public void deletar(String id) {
        repo.delete(id);
    }

    public void associar(String idTorcedor, String idCampanha) {
        Torcedor old = repo.findOne(idTorcedor);
        Assert.notNull(old, "Torcedo não cadastrado");

        //verifica se a campanha já está cadastrada
        if(old.getCampanhas() != null) {
            if(old.getCampanhas().contains(idCampanha)) {
                throw new ValidateException("Campanha já cadastrada");
            }
        } else { // cria nova lista
            old.setCampanhas(new ArrayList<>());
        }
        old.getCampanhas().add(idCampanha);
        repo.save(old);

    }


}