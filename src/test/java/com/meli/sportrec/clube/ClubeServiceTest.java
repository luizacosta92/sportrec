package com.meli.sportrec.clube;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {
    @Mock
    private ClubeRepository clubeRepository;
    @InjectMocks
    private ClubeService clubeService;


    //cadastrar clube com nome com 1 caracter
    //cadsatrar clube com estado diferente do padrao
    //cadastrar clube com data no futuro
    //cadastrar clube que ja existe


    //buscar clube
    //listar todos os clubes//listar por filtro

    //dado, quando, então

    @Test  //cadastrar clube com todos validos ok - feito
    public void testGivenValidClubeWhenClubesIsCreatedThenSaved(){
        //ARRANGE

        ClubeRecordDto ClubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now(), true);

        when(clubeRepository.save(any(ClubeModel.class))).thenReturn(new ClubeModel());

        // ACT;
        ClubeModel resultado = clubeService.salvarClube(ClubeRecordDto);

        // ASSERT
        Assertions.assertNotNull(resultado);
    }

    @Test    //atualizar clube
    public void testGivenValidClubeWhenClubesIsUpdatedThenSaved(){
        ClubeRecordDto ClubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now(), false);
        when(clubeRepository.save(any(ClubeModel.class))).thenReturn(new ClubeModel());

        ClubeModel resultado = clubeService.salvarClube(ClubeRecordDto);

        Assertions.assertNotNull(resultado);
    }


    @Test   //inativar clube
    public void testGivenValidClubeWhenClubesIsDeletedThenSaved(){
        ClubeRecordDto clubeRecordDto = new ClubeRecordDto("Clube1", "GO", LocalDate.now(), true);

    }
}
