package br.com.time.api.service;

import br.com.time.api.exception.ResourceNotFoundException;
import br.com.time.api.model.Time;
import br.com.time.api.repository.TimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TimeService {

  private final TimeRepository repo;

  public void alterar(final Time c) throws Exception {
    final Time time = repo.findById(c.getId())
        .map(oldTime -> new Time(oldTime, c))
        .orElseThrow(ResourceNotFoundException::new);

    repo.save(time);
  }

  public void criar(Time c) throws Exception {
    repo.save(c);
  }

  public Collection<Time> listar() {
    return repo.findAll();
  }

  public Optional<Time> recuperar(final String id) {
    return repo.findById(id);
  }
}
