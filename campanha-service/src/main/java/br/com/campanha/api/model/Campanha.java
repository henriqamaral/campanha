package br.com.campanha.api.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Campanha
 */
@JsonInclude(value = Include.NON_NULL)
@Document(collection = "campanhas")
public class Campanha {
    
    @Id private String id;
    @NotNull
    private String nome;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date inicio;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date fim;
    @NotNull
    private String idTime;

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Date getInicio() {
        return this.inicio;
    }
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return this.fim;
    }
    public void setFim(Date fim) {
        this.fim = fim;
    }

    public String getIdTime() {
        return this.idTime;
    }
    public void setIdTime(String idTime) {
        this.idTime = idTime;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
		builder.append("Campanha [");
        if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
        if (nome != null) {
			builder.append("nome=");
			builder.append(nome);
			builder.append(", ");
		}
        if (inicio != null) {
			builder.append("inicio=");
			builder.append(inicio);
			builder.append(", ");
		}
        if (fim != null) {
			builder.append("fim=");
			builder.append(fim);
            builder.append(", ");
		}
        if (idTime != null) {
			builder.append("idTime=");
			builder.append(idTime);
            builder.append(", ");
		}
        
        builder.append("]");
        return builder.toString();
    }
}