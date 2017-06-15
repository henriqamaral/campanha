package br.com.campanha.api.repository;

import java.util.Collection;
import java.util.Date;

import br.com.campanha.api.model.Campanha;

public interface CampanhaRepositoryCustom {

    Collection<Campanha> findByDataVigencia();
    //Retorna as campanhas que estao vigentes
    Collection<Campanha> findInVigencia(Date fimVigencia, Date inicioVigencia);

}
