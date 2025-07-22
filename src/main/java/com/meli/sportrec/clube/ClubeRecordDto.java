package com.meli.sportrec.clube;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record ClubeRecordDto(@NotBlank @Length(min = 2, max = 100) String clubeNome,
                             @Pattern(regexp = "AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO",
                                     message = "Estado inválido") String estado,
                             @NotNull @PastOrPresent @JsonFormat(pattern = "dd/MM/yyyy" ) LocalDate dataCriacao,
                             boolean ativo) {

}
