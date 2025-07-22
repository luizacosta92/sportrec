package com.meli.sportrec.exceptionhandler;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExeceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExeceptionHandler();
    }

    @Test // tratar erro de validação de campos
    public void testGivenValidationErrorsWhenHandleValidationExceptionThenReturnBadRequest() {
        // DADO - Simulamos erro de validação com múltiplos campos
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("clube", "nome", "não pode estar vazio");
        FieldError fieldError2 = new FieldError("clube", "estado", "deve ter 2 caracteres");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<ApiError> response = globalExceptionHandler.handleValidationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("nome: não pode estar vazio");
        assertThat(response.getBody().getMessage()).contains("estado: deve ter 2 caracteres");
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }

    @Test // tratar erro de entidade não encontrada
    public void testGivenEntityNotFoundExceptionWhenHandleEntityNotFoundThenReturnNotFound() {

        String mensagemErro = "Clube não encontrado";
        EntityNotFoundException ex = new EntityNotFoundException(mensagemErro);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleEntityNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo(mensagemErro);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test // tratar erro de entity conflict
    public void testGivenEntityConflictExceptionWhenHandleClubeConflictThenReturnConflict() {
        String mensagemErro = "Clube Flamengo j[a existe no estado RJ";
        EntityConflictException exception = new EntityConflictException(mensagemErro);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleClubeConflict(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo(mensagemErro);
        assertThat(response.getBody().getStatus()).isEqualTo(409);
    }

    @Test // tratar erro de exception
    public void testGivenGenericExceptionWhenHandleGeneralExceptionThenReturnInternalServerError() {
        Exception exception = new RuntimeException("Erro inesperado.");

        ResponseEntity<ApiError> response = globalExceptionHandler.handleGeneralException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).isEqualTo("Erro inesperado.");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

}
