package com.meli.sportrec.partida;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record PartidaRecordDto(
        @NotNull(message = "ID do clube mandante é obrigatório")
        Long clubeMandanteId,

        @NotNull(message = "ID do clube visitante é obrigatório")
        Long clubeVisitanteId,

        @NotNull(message = "ID do estádio é obrigatório")
        Long estadioId,

        @Min(value = 0, message = "Gols da casa não podem ser negativos")
        @NotNull(message = "Gols da casa são obrigatórios")
        Integer golsMandante,

        @Min(value = 0, message = "Gols do visitante não podem ser negativos")
        @NotNull(message = "Gols do visitante são obrigatórios")
        Integer golsVisitante,

        @NotNull(message = "Data e hora são obrigatórias")
        @PastOrPresent(message = "Data não pode ser no futuro") //formato de data 1901-07-23T10:30:00
        LocalDateTime dataHoraPartida
) {}

