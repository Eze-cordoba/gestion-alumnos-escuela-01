package com.gestionalumnosescuela1.gestionAlumnos.service;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Materia;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.MateriaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public class MateriaServiceTest {

    @Mock
    MateriaDao dao;

    @InjectMocks
    MateriaService service;

    Materia materiaA;
    Materia materiaB;
    Materia materiaC;

    List<Materia>materias  = new ArrayList<>();

    @BeforeEach
    void before() {
        /* Inicializar usuarios y guardarlos en la base de datos antes de cada prueba */
        materiaA = Materia.builder()
                .id(1L)
                .name("PROGRAMACION")
                .build();


        materias.add(materiaA);

        materiaB = Materia.builder()
                .id(2L)
                .name("INGLES")
                .build();

        materias.add(materiaB);


         materiaC = Materia.builder()
                .id(3L)
                .name("MATEMATICA")
                .build();


        materias.add(materiaC);

    }


    @Test
    void testFindMateriaById(){

        given(dao.findById(materiaA.getId()) ).willReturn(Optional.of(materiaA));

        Materia materia = service.findMateria(materiaA.getId());

        assertThat(materia).isNotNull();
        assertThat(materia.getName()).isEqualTo("PROGRAMACION");

    }


    @Test
    void testActualizarMateria(){

        given(dao.save(materiaC)).willReturn(materiaC);
        materiaC.setName("LABORATORIO");

        Materia materiaActualizado = service.updateMateria(materiaC);

        assertThat(materiaActualizado.getName()).isEqualTo("LABORATORIO");
    }


    @Test
    void testListarMateria(){

        given(dao.findAll()).willReturn(materias);


        List<Materia> materiasBD = service.findAll();

        assertEquals(3, materias.size());

    }



}
