package br.com.campanha.api.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import br.com.campanha.api.message.CampanhaMessage;
import br.com.campanha.api.model.Campanha;
import br.com.campanha.api.repository.CampanhaRepository;

/**
 * CampanhaService
 */
@Service
public class CampanhaService {

    @Autowired
    CampanhaRepository repo;

    @Autowired
    private CampanhaMessage channel;

    //Só retorna campanhas vigentes
    // onde a data do fim eh maior que hoje
    public Collection<Campanha> listar() {
        return repo.findByDataVigencia();
    }
    public void criar(Campanha c) {
        //verificar se existe campanhas na data vigente
        //busca no banco
        verificaCampanhas(c);
        repo.save(c);
    }

    public Campanha recuperar(String id) {
        return repo.findOne(id);
    }

    public void alterar(Campanha c) throws Exception {
        Campanha old = repo.findOne(c.getId());
        if(old == null) {
            throw new Exception("Not Found");
        }
        old.setNome(c.getNome());
        old.setFim(c.getFim());
        //fazer chamada MQ para avisar ouvintes sobre alteracao da campanha
        repo.save(old);
    }

    public void deletar(String id) {
        repo.delete(id);
    }

    private void verificaCampanhas(Campanha c) {
        Collection<Campanha> campanhas = repo.findInVigencia(c.getFim(), c.getInicio());
        //altera as datas
        if(!campanhas.isEmpty()) {
            for(Campanha ca : campanhas) {
                Date fimDate = addDay(ca.getFim(), 1);
                //verifico se a data bate com alguma já criada
                fimDate =  verificaDatasIguais(campanhas, fimDate);
                if(fimDate.equals(c.getFim())) {
                  fimDate =  addDay(fimDate, 1);
                }
                ca.setFim(fimDate);
            }   
        }
        //Avisa alguem via MQ
        enviaCampanhasAlteradas(campanhas);
        repo.save(campanhas);
    }

    private Date verificaDatasIguais(Collection<Campanha> campanhas, final Date fimDate) {

        //limpo a lista com datas menores que a data fim
        campanhas = campanhas.stream().filter(c -> c.getFim().after(fimDate)).collect(Collectors.toList());
        if(!campanhas.isEmpty()) {
            //pego a maior data
            Campanha c = campanhas.stream().reduce((x, y) -> x.getFim().after(y.getFim()) ? x : y).get();
            //só atualiza se a data for diferente
            if(c != null && !c.getFim().equals(fimDate)) {
                Date newFimDate = addDay(c.getFim(), 1);
                return newFimDate;
            }
        }
        
        return fimDate;
    }

    private Date addDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    private void enviaCampanhasAlteradas(Collection<Campanha> campanhas) {
        
        campanhas.stream().forEach(c -> {
            Message<Campanha> m = MessageBuilder.withPayload(c).build();
            channel.output().send(m);
        });

    }
}