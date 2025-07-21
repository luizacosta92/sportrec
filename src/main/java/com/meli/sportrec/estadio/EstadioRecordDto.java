package com.meli.sportrec.estadio;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EstadioRecordDto(@NotBlank @Length(min = 3, max = 50) String estadioNome) {
}
