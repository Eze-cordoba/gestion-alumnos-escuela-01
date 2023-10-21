package com.gestionalumnosescuela1.gestionAlumnos.service;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Examen;

import com.gestionalumnosescuela1.gestionAlumnos.entityDao.ExamenDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public class ExamenServiceTest {

    @Mock
    ExamenDao dao;

    @InjectMocks
    ExamenService service;

    Examen examenA;
    Examen examenB;
    Examen examenC;

    @BeforeEach
    void before() {
        /* Inicializar usuarios y guardarlos en la base de datos antes de cada prueba */
        examenA = Examen.builder()
                .id(1)
                .titulo("tituloA")
                .build();

        examenB = Examen.builder()
                .id(2)
                .titulo("tituloB")
                .build();


        examenC = Examen.builder()
                .id(3)
                .titulo("tituloC")
                .build();

    }



    @Test
    void TestGuardarUsuario() {

        given(dao.save(examenA)).willReturn(examenA);

        Examen examenGuardado = service.crearExamen(examenA);

        assertThat(examenGuardado).isNotNull();
        assertThat(examenGuardado.getTitulo()).isEqualTo("tituloA");

    }

    @Test
    void testFindUsuarioById(){

        given(dao.findById(examenB.getId()) ).willReturn(Optional.of(examenB));

        Examen examenEncontrado = service.findExamen(examenB.getId());

        assertThat(examenEncontrado).isNotNull();
        assertThat(examenEncontrado.getTitulo()).isEqualTo("tituloB");


    }

    @Test
    void testActualizarUsuario(){

        given(dao.save(examenC)).willReturn(examenC);
        examenC.setTitulo("tituloC2");


        Examen usuarioActualizado = service.updateExamen(examenC);


        assertThat(usuarioActualizado.getTitulo()).isEqualTo("tituloC2");

    }

    @Test
    void testEliminarUsuarioById(){

        service.eliminarExamen(examenC.getId());
        verify(dao).deleteById(examenC.getId());

    }


}
