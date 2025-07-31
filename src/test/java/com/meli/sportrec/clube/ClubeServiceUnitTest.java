package com.meli.sportrec.clube;

import com.meli.sportrec.exceptionhandler.EntityConflictException;
import com.meli.sportrec.partida.PartidaModel;
import com.meli.sportrec.partida.PartidaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceUnitTest {
    @Mock
    private ClubeRepository clubeRepository;
    @Mock
    private ClubeValidator clubeValidator;
    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private ClubeService clubeService;
    private Pageable pageable;
    private Page<ClubeModel> pageMock;
    private List<ClubeModel> clubeModelList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //listar todos os clubes//listar por filtro

    //dado, quando, então

    @Test  //cadastrar clube com todos os campos validos ok - feito
    public void testGivenValidClubeWhenClubesIsCreatedThenSaved() {
        //DADO

        ClubeRecordDto ClubeRecordDto = new ClubeRecordDto(
                "Clube1", "GO", LocalDate.now(), true);

        when(clubeRepository.save(any(ClubeModel.class))).thenReturn(new ClubeModel());

        // QUANDO;
        ClubeModel resultado = clubeService.salvarClube(ClubeRecordDto);

        // ENTAO
        Assertions.assertNotNull(resultado);
    }

    @Test   //inativar clube
    public void testGivenValidClubeWhenClubesIsSoftDeletedThenSaved() {
        Long clubeId = 1L;
        //Cria um clube e diz que ele está ativo
        ClubeModel clubeExistente = new ClubeModel();
        clubeExistente.setId(clubeId);
        clubeExistente.setAtivo(true);

        //procura o clube pelo id
        when(clubeRepository.findById(clubeId)).thenReturn(Optional.of(clubeExistente));

        when(clubeRepository.save(any(ClubeModel.class))).thenReturn(clubeExistente);
        clubeService.inativarClube(clubeId);
        verify(clubeRepository, Mockito.times(1)).findById(clubeId);
    }

    @Test  //cadastrar clube com nome que já existe no mesmo estado e retornar excecao
    public void testGivenClubeExistsOnStateWhenClubesIsCreatedThenThrowException() {
        //DADO

        ClubeRecordDto clubeRecordDto1 = new ClubeRecordDto(
                "Clube 1", "GO", LocalDate.now(), true);

        //QUANDO
        when(clubeRepository.existsByClubeNomeIgnoreCaseAndEstado(
                eq(clubeRecordDto1.clubeNome()), eq(clubeRecordDto1.estado()))).thenReturn(Boolean.TRUE);

        // ENTAO
        assertThrows(
                EntityConflictException.class, () -> clubeService.salvarClube(clubeRecordDto1)
        );
    }

    @Test //buscar clube sem filtro
    public void testGivenNoFilterWhenBuscarClubesThenReturnAllClubes() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ClubeModel> clubesList = Arrays.asList(
                new ClubeModel(),
                new ClubeModel()
        );
        Page<ClubeModel> pageMock = new PageImpl<>(clubesList, pageable, 2);

        when(clubeRepository.findByFiltros(null, null, null, pageable))
                .thenReturn(pageMock);

        Page<ClubeModel> resultado = clubeService.buscarClubesComFiltros(null, null, null, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent());
        assertThat(resultado.getTotalElements()).isEqualTo(2);


        verify(clubeRepository).findByFiltros(null, null, null, pageable);

    }

    @Test //buscar clube com todos os filtros
    public void testGivenAllFilterWhenBuscarClubeThenReturnClube() {
        String clubeNome = "Clube1";
        String estado = "RJ";
        Boolean ativo = true;
        Pageable pageable = PageRequest.of(0, 10);

        ClubeModel clubeExistente = new ClubeModel();
        clubeExistente.setClubeNome("Clube1");
        clubeExistente.setEstado("RJ");
        clubeExistente.setAtivo(true);

        List<ClubeModel> clubesFiltrados = Arrays.asList(clubeExistente);
        Page<ClubeModel> pageFiltrada = new PageImpl<>(clubesFiltrados);

        when(clubeRepository.findByFiltros(clubeNome, estado, ativo, pageable))
                .thenReturn(pageFiltrada);

        Page<ClubeModel> resultado = clubeService.buscarClubesComFiltros(clubeNome, estado, ativo, pageable);


        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent().get(0).getClubeNome()).isEqualTo("Clube1");
        assertThat(resultado.getContent().get(0).getEstado()).isEqualTo("RJ");
        assertThat(resultado.getContent().get(0).getAtivo()).isTrue();

        verify(clubeRepository).findByFiltros(clubeNome, estado, ativo, pageable);
    }

    @Test //buscar clubes por estado
    public void testGivenEstadoFilterWhenBuscarClubesComFiltrosIsCalledThenReturnClubesByEstado() {
        String estado = "RJ";
        Pageable pageable = PageRequest.of(0, 10);

        ClubeModel botafogo = new ClubeModel();
        botafogo.setClubeNome("Botafogo");
        botafogo.setEstado("RJ");
        botafogo.setAtivo(true);

        ClubeModel fluminense = new ClubeModel();
        fluminense.setClubeNome("Fluminense");
        fluminense.setEstado("SP");
        fluminense.setAtivo(false);

        List<ClubeModel> clubesRJ = Arrays.asList();
        Page<ClubeModel> pageRJ = new PageImpl<>(clubesRJ, pageable, 2);

        when(clubeRepository.findByFiltros(null, estado, null, pageable))
                .thenReturn(pageRJ);

        // ACT
        Page<ClubeModel> resultado = clubeService.buscarClubesComFiltros(null, estado, null, pageable);

        // ASSERT
        boolean listaRJ = resultado.getContent()
                .stream()
                .allMatch(clube -> "RJ".equals(clube.getEstado()));

        Assertions.assertTrue(listaRJ);
    }

    @Test //testar busca com filtro por clube ativo
    public void testGivenAtivoFilterWhenBuscarClubesComFiltrosIsCalledThenReturnClubeByAtivo() {
        Pageable pageable = PageRequest.of(0, 10);
        Boolean ativo = true;

        when(clubeRepository.findByFiltros(null, null, true, pageable)).thenReturn(Page.empty());

        Page<ClubeModel> resultado = clubeService.buscarClubesComFiltros(null, null, ativo, pageable);

        assertThat(resultado).isNotNull();
        verify(clubeRepository).findByFiltros(null, null, true, pageable);
    }

    @Test //testar busca com filtro por clube inativo
    public void testGivenInativoFilterWhenBuscarClubesComFiltrosIsCalledThenReturnClubeByInativo() {
        Pageable pageable = PageRequest.of(0, 10);
        Boolean ativo = false;

        when(clubeRepository.findByFiltros(null, null, false, pageable)).thenReturn(Page.empty());

        Page<ClubeModel> resultado = clubeService.buscarClubesComFiltros(null, null, false, pageable);
        assertThat(resultado).isNotNull();
        verify(clubeRepository).findByFiltros(null, null, false, pageable);
    }

    @Test    //atualizar clube
    public void testGivenValidClubeWhenClubesIsUpdatedThenSaved() {
        Long clubeId = 1L;
        ClubeRecordDto clubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now(), false);
        ClubeModel clube = new ClubeModel();
        clube.setId(clubeId);
        PartidaModel partida = new PartidaModel();
        partida.setDataHoraPartida(LocalDateTime.now().plusDays(1));

        //atualiza o clube
        when(clubeRepository.findById(clubeId)).thenReturn(Optional.of(clube));
        when(clubeRepository.existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(
                anyString(), anyString(), eq(clubeId))).thenReturn(Boolean.FALSE);

        //busca a primeira partida
        when(partidaRepository.findById(clubeId)).thenReturn(Optional.of(partida));
        doNothing().when(clubeValidator).validate(eq(clube), eq(partida));

        when(clubeRepository.save(clube)).thenReturn(clube);

        ClubeModel resultado = clubeService.atualizarClube(clubeId, clubeRecordDto);

        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(clube);
        verify(clubeRepository).save(clube);
    }

    @Test //atualizar e clube nao é encontrado  e retorna excecao
    public void testGivenExistClubeWhenUpdateClubeIsNotFoundThenReturnEntityNotFoundException(){
        Long id = 1L;
        ClubeRecordDto clubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now(), false);

        when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clubeService.atualizarClube(id, clubeRecordDto));

        assertThat(exception.getMessage()).isEqualTo("Clube não encontrado");

        verify(clubeRepository).findById(id);
        verify(clubeRepository, never()).existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(anyString(), anyString(), anyLong());
    }

    @Test //atualizar e clube ja existe no estado e retorna excecao
    public void testGivenClubeNomeExistsNoEstadoWhenClubeUpdatedThenEntityConflictException(){
        Long id = 1L;
        ClubeRecordDto clubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now(), false);
        ClubeModel clubeExistente = new ClubeModel();
        clubeExistente.setId(id);
        clubeExistente.setClubeNome("Clube1");

        when(clubeRepository.findById(eq(id))).thenReturn(Optional.of(clubeExistente)); //primeiro clube existe

        when(clubeRepository.existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(
                eq(clubeRecordDto.clubeNome()),
                eq(clubeRecordDto.estado()),
                eq(id))).thenReturn(Boolean.TRUE);

        EntityConflictException exception = assertThrows(
                EntityConflictException.class, () -> clubeService.atualizarClube(id, clubeRecordDto));

        assertThat(exception.getMessage()).isEqualTo(
                "Clube " + clubeRecordDto.clubeNome() + " já existe no estado " + clubeRecordDto.estado());

        verify(clubeRepository).findById(id);
        verify(clubeRepository).existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(
                clubeRecordDto.clubeNome(), clubeRecordDto.estado(), id);
        verify(clubeRepository, never()).save(any(ClubeModel.class));
    }

    @Test //atualizar clube com data no futuro e retorna excecao
    public void testGivenExistClubeWhenUpdateDataCriacaoNoFuturoThenEntityConflictException(){
        Long id = 1L;
        ClubeRecordDto clubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now().plusDays(1), true);

        ClubeModel clubeExistente = new ClubeModel();
        when(clubeRepository.findById(eq(id))).thenReturn(Optional.of(clubeExistente));
        when(clubeRepository.existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(anyString(), anyString(), anyLong())).thenReturn(Boolean.FALSE);

        EntityConflictException exception = assertThrows(
                EntityConflictException.class, () -> clubeService.atualizarClube(id, clubeRecordDto));

        assertThat(exception.getMessage()).isEqualTo("Data de criação não pode ser no futuro");
    }

    @Test // atualizar clube quando validação de data primeira partida falha
    public void testGivenValidatorFailsWhenClubeIsUpdatedThenThrowException() {
        Long clubeId = 1L;
        ClubeRecordDto clubeRecordDto = new ClubeRecordDto(
                "Clube 1", "GO", LocalDate.now(), true);

        ClubeModel clubeExistente = new ClubeModel();
        PartidaModel partida = new PartidaModel();
        partida.setDataHoraPartida(LocalDateTime.now().minusDays(1));

        when(clubeRepository.findById(eq(clubeId))).thenReturn(Optional.of(clubeExistente));
        when(clubeRepository.existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(
                anyString(), anyString(), eq(clubeId))).thenReturn(Boolean.FALSE);

        when(partidaRepository.findById(eq(clubeId))).thenReturn(Optional.of(partida));

        doThrow(new EntityConflictException("Data de criação inválida para este clube"))
                .when(clubeValidator).validate(
                        eq(clubeExistente),
                        eq(partida)
                );

        EntityConflictException exception = assertThrows(
                EntityConflictException.class,
                () -> clubeService.atualizarClube(clubeId, clubeRecordDto));

        assertThat(exception.getMessage())
                .isEqualTo("Data de criação inválida para este clube");
    }

}