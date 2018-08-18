package br.com.time.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@JsonInclude(value = Include.NON_NULL)
@Document(collection = "times")
@Getter
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Time {

  @NotEmpty private String nome;
  @Setter @Id private String id;

  public Time(final String nome) {
    this.nome = nome;
  }

  public Time(final Time oldTime, final Time newTeam) {
    id = oldTime.getId();
    nome = newTeam.getNome();
  }
}
