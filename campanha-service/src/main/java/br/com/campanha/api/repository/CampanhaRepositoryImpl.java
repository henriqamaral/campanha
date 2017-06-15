package br.com.campanha.api.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import br.com.campanha.api.model.Campanha;

/**
 * CampanhaRepositoryImpl
 */
public class CampanhaRepositoryImpl implements CampanhaRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;
    public Collection<Campanha> findByDataVigencia() {

         Query query = new Query();
         query.addCriteria(Criteria.where("fim").gte(new Date()));
         query.with(new Sort(Sort.Direction.ASC, Arrays.asList("nome", "idTime", "fim")));

         return mongoTemplate.find(query, Campanha.class);
        
    }

    public Collection<Campanha> findInVigencia(Date fimVigencia, Date inicioVigencia) {
        Query query = new Query();
        query.addCriteria(Criteria.where("inicio").gte(inicioVigencia).and("fim").lte(fimVigencia));
        return mongoTemplate.find(query, Campanha.class);
    }

}