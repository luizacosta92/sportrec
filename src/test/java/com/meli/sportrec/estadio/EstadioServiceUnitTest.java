package com.meli.sportrec.estadio;

import com.meli.sportrec.exceptionhandler.EntityConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadioServiceUnitTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;

    private EstadioRecordDto estadioRecordDto;
    private EstadioModel estadioModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        estadioRecordDto = new EstadioRecordDto("Maracanã");
        estadioModel = new EstadioModel();
        estadioModel.setId(1L);
        estadioModel.setEstadioNome("Maracanã");
    }

    @Test //salvar clube com sucesso
    public void testGivenValidEstadioWhenSalvarEstadioThenReturnSavedEstadio() {
        // Given
        when(estadioRepository.existsByEstadioNomeIgnoreCase(anyString())).thenReturn(Boolean.FALSE);
        when(estadioRepository.save(any(EstadioModel.class))).thenReturn(estadioModel);

        // When
        EstadioModel resultado = estadioService.salvarEstadio(estadioRecordDto);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstadioNome()).isEqualTo("Maracanã");
        assertThat(resultado.getId()).isEqualTo(1L);

        verify(estadioRepository).existsByEstadioNomeIgnoreCase(anyString());
        verify(estadioRepository).save(any(EstadioModel.class));
    }

    @Test //salvar clube com conflito de estádio já existe
    public void testGivenExistingEstadioNomeWhenSalvarEstadioThenEntityConflictException() {
        when(estadioRepository.existsByEstadioNomeIgnoreCase(anyString())).thenReturn(Boolean.TRUE);


        EntityConflictException exception = assertThrows(EntityConflictException.class,
                () -> estadioService.salvarEstadio(estadioRecordDto));

        assertThat(exception.getMessage()).isEqualTo("Estádio Maracanã já existe");
        verify(estadioRepository).existsByEstadioNomeIgnoreCase(anyString());
        verify(estadioRepository, never()).save(any(EstadioModel.class));
    }

    @Test //buscar estadios por página
    public void testGivenValidEstadioWhenBuscarTodosEstadiosThenReturnPageEstadios() {

        Pageable pageable = PageRequest.of(0, 10);
        List<EstadioModel> estadios = List.of(estadioModel);
        Page<EstadioModel> pageEstadios = new PageImpl<>(estadios, pageable, 1);

        when(estadioRepository.findAllOrderByNome(any(Pageable.class))).thenReturn(pageEstadios);


        Page<EstadioModel> resultado = estadioService.buscarTodosEstadios(pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent().get(0).getEstadioNome()).isEqualTo("Maracanã");
        verify(estadioRepository).findAllOrderByNome(any(Pageable.class));
    }

    @Test
    public void testGivenValidPageableWhenBuscarTodosEstadiosThenReturnPageEstadios() {
        Pageable pageable = PageRequest.of(0, 10);
        List<EstadioModel> estadios = List.of(estadioModel);
        Page<EstadioModel> pageEstadios = new PageImpl<>(estadios, pageable, 1);

        when(estadioRepository.findAllOrderByNome(eq(pageable))).thenReturn(pageEstadios);

        Page<EstadioModel> resultado = estadioService.buscarTodosEstadios(pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1L); // Use 1L para Long
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getEstadioNome()).isEqualTo("Maracanã");

        verify(estadioRepository).findAllOrderByNome(eq(pageable));
    }

    @Test
    public void testGivenIdExistsWhenBuscarEstadioPorIdThenReturnEstadio() {
        Long id = 1L;
        when(estadioRepository.findById(eq(id))).thenReturn(Optional.of(estadioModel));

        Optional<EstadioModel> resultado = estadioService.buscarEstadioPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstadioNome()).isEqualTo("Maracanã");
        verify(estadioRepository).findById(id);

    }

    @Test
    public void testGivenIdNotExistsWhenBuscarEstadioPorIdThenReturnEntityNotFoundException() {
        Long id = 1L;
        when(estadioRepository.findById(eq(id))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> estadioService.buscarEstadioPorId(id));

        assertThat(exception.getMessage()).isEqualTo("Estádio não encontrado");
        verify(estadioRepository).findById(id);
    }

    @Test
    public void testGivenClubeExistsWhenAtualizarEstadioThenReturnUpdatedEstadio(){
        Long id = 1L;
        EstadioRecordDto atualizacaoEstadio = new EstadioRecordDto("Arena Maracanã");

        EstadioModel atualizacaoEstadioModel = new EstadioModel();
        atualizacaoEstadioModel.setId(id);
        atualizacaoEstadioModel.setEstadioNome("Arena Maracanã");


        when(estadioRepository.findById(eq(id))).thenReturn(Optional.of(estadioModel));
        when(estadioRepository.existsByEstadioNomeIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(Boolean.FALSE);
        when(estadioRepository.save(any(EstadioModel.class))).thenReturn(atualizacaoEstadioModel);

        EstadioModel resultado = estadioService.atualizarEstadio(id, atualizacaoEstadio);


        assertThat(atualizacaoEstadio).isNotNull();
        assertThat(resultado.getEstadioNome()).isEqualTo("Arena Maracanã");
        assertThat(resultado.getId()).isEqualTo(1L);

        verify(estadioRepository).findById(eq(id));
        verify(estadioRepository).existsByEstadioNomeIgnoreCaseAndIdNot(anyString(), anyLong());
        verify(estadioRepository).save(any(EstadioModel.class));

    }

    @Test
    public void testGivenNonExistingIdWhenAtualizarEstadioThenEntityNotFoundException() {

        Long id = 999L;
        when(estadioRepository.findById(eq(id))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> estadioService.atualizarEstadio(id, estadioRecordDto));

        assertThat(exception.getMessage()).isEqualTo("Estádio não encontrado");
        verify(estadioRepository).findById(eq(id));
        verify(estadioRepository, never()).save(any(EstadioModel.class));
    }

    @Test
    public void testGivenExistingEstadioNomeWhenAtualizarEstadioThenEntityConflictException() {

        Long id = 1L;
        EstadioRecordDto novosDados = new EstadioRecordDto("Arena Corinthians");

        when(estadioRepository.findById(eq(id))).thenReturn(Optional.of(estadioModel));
        when(estadioRepository.existsByEstadioNomeIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(Boolean.TRUE);

        EntityConflictException exception = assertThrows(EntityConflictException.class,
                () -> estadioService.atualizarEstadio(id, novosDados));

        assertThat(exception.getMessage()).isEqualTo("Estádio Arena Corinthians já existe");
        verify(estadioRepository).findById(eq(id));
        verify(estadioRepository).existsByEstadioNomeIgnoreCaseAndIdNot(anyString(), anyLong());
        verify(estadioRepository, never()).save(any(EstadioModel.class));
    }

}
