package com.meli.sportrec.clube;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClubeControllerUnitTest {
    @Mock
    private ClubeService clubeService;

    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeController clubeController;

    private ClubeRecordDto clubeRecordDto;
    private ClubeModel clubeModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);


        clubeRecordDto = new ClubeRecordDto("Fluminense", "RJ", LocalDate.of(1902, 7, 1), true);

        //model está como construtor porque tem os getters e setters
        clubeModel = new ClubeModel();
        clubeModel.setId(1L);
        clubeModel.setClubeNome("Fluminense");
        clubeModel.setEstado("RJ");
        clubeModel.setAtivo(true);
    }

    @Test
    void deveCriarClubeComSucesso() {
        when(clubeService.salvarClube(clubeRecordDto)).thenReturn(clubeModel);

        ResponseEntity<ClubeModel> response = clubeController.saveClube(clubeRecordDto);


        //verifica se os dois status sao iguais
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        //verifica se o clubemodel tem a mesma resposta do body que foi pro response
        assertEquals(clubeModel, response.getBody());
        //verifica se o metodo de salvar clube foi chamado uma vez com o service
        verify(clubeService, times(1)).salvarClube(clubeRecordDto);
    }
    @Test
    void deveBuscarTodosClubesComSucesso() {

        List<ClubeModel> listaClubes = Arrays.asList(clubeModel);
        Page<ClubeModel> pageClubes = new PageImpl<>(listaClubes);

        Pageable pageable = PageRequest.of(0, 10);


        when(clubeService.buscarClubesComFiltros(null, null, null, pageable)).thenReturn(pageClubes);

        ResponseEntity<Page<ClubeModel>> response = clubeController.getAll(null, null, null, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageClubes, response.getBody());
        assertEquals(1, response.getBody().getTotalElements());

        verify(clubeService, times(1)).buscarClubesComFiltros(null, null, null, pageable);
    }

    @Test
    void deveBuscarClubesComFiltros() {
        String clubeNome = "Fluminense";
        String estado = "RJ";
        Boolean ativo = Boolean.TRUE;
        Pageable pageable = PageRequest.of(0, 10);

        List<ClubeModel> listaClubesFiltrados = Arrays.asList(clubeModel);
        Page<ClubeModel> pageClubesFiltrados = new PageImpl<>(listaClubesFiltrados);

        when(clubeService.buscarClubesComFiltros(clubeNome, estado, ativo, pageable)).thenReturn(pageClubesFiltrados);

        ResponseEntity<Page<ClubeModel>> response = clubeController.getAll(clubeNome,estado, ativo, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageClubesFiltrados, response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(clubeService, times(1)).buscarClubesComFiltros(clubeNome, estado, ativo, pageable);
    }

    @Test
    void deveBuscarClubePorIdComSucesso() {
        Long clubeId = 1L;


        when(clubeService.buscarClubePorId(clubeId))
                .thenReturn(Optional.of(clubeModel));

        ResponseEntity<Object> response = clubeController.getOne(clubeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clubeModel, response.getBody());

        verify(clubeService, times(1)).buscarClubePorId(clubeId);
    }

    @Test
    void deveRetornarErroQuandoClubeNaoForEncontrado() {

        Long clubeIdInexistente = 999L;

        when(clubeService.buscarClubePorId(clubeIdInexistente))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clubeController.getOne(clubeIdInexistente)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Clube não encontrado", exception.getReason());

        verify(clubeService, times(1)).buscarClubePorId(clubeIdInexistente);
    }

    @Test
    void deveAtualizarClubeComSucesso() {
        Long clubeId = 1L;

        ClubeRecordDto clubeAtualizado = new ClubeRecordDto(
                "Fluminense Atualizado",
                "RJ",
                LocalDate.of(2023, 2, 20),
                true
        );

        ClubeModel clubeModelAtualizado = new ClubeModel();
        clubeModelAtualizado.setId(1L);
        clubeModelAtualizado.setClubeNome("Fluminense Atualizado");
        clubeModelAtualizado.setEstado("RJ");
        clubeModelAtualizado.setAtivo(true);

        when(clubeService.atualizarClube(clubeId, clubeAtualizado))
                .thenReturn(clubeModelAtualizado);

        ResponseEntity<Object> response = clubeController.updateClube(clubeId, clubeAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clubeModelAtualizado, response.getBody());
        verify(clubeService, times(1)).atualizarClube(clubeId, clubeAtualizado);
    }


}
