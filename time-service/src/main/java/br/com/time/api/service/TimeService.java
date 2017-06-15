package br.com.time.api.service;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.time.api.model.Time;
import br.com.time.api.repository.TimeRepository;

/**
 * TimeService
 */
@Service
public class TimeService {

    @Autowired
    TimeRepository repo;

    public Collection<Time> listar() {
        return repo.findAll();
    }
    public void criar(Time c) {
        repo.save(c);
    }

    public Time recuperar(String id) {
        return repo.findOne(id);
    }

    public void alterar(Time c) throws Exception {
        Time old = repo.findOne(c.getId());
        if(old == null) {
            throw new Exception("Not Found");
        }
        old.setNome(c.getNome());
        //fazer chamada MQ para avisar ouvintes sobre alteracao da Time
        repo.save(old);
    }

    public void deletar(String id) {
        repo.delete(id);
    }


}