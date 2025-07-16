package com.meli.sportrec.estadio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Entity
@Table(name = "TB_Estadios")
public class EstadioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Length(min = 3, max = 50)
    private String EstadioNome;

    public String getEstadioNome() {
        return EstadioNome;
    }

    public void setEstadioNome(String estadioNome) {
        EstadioNome = estadioNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
