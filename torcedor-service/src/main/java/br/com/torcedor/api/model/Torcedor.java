package br.com.torcedor.api.model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Torcedor
 */
@JsonInclude(value = Include.NON_NULL)
@Document(collection = "torcedores")
public class Torcedor {
    
    @Id private String id;
    @NotNull
    private String nome;
    @Indexed
    @NotNull
    @Email
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    private Date dtNascimento;
    @NotNull
    private String idTimeCoracao;
    //Id das Campanhas
    private Collection<String> campanhas;

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

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDtNascimento() {
        return this.dtNascimento;
    }
    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getIdTimeCoracao() {
        return this.idTimeCoracao;
    }
    public void setIdTimeCoracao(String idTimeCoracao) {
        this.idTimeCoracao = idTimeCoracao;
    }

    public Collection<String> getCampanhas() {
        return this.campanhas;
    }
    public void setCampanhas(Collection<String> campanhas) {
        this.campanhas = campanhas;
    }

public void addCampanha(String campanha) {
        if(this.campanhas == null) {
            this.campanhas = new ArrayList<String>();
        }
        this.campanhas.add(campanha);
    }
    
}