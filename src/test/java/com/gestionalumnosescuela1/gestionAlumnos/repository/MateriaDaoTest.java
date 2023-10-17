package com.gestionalumnosescuela1.gestionAlumnos.repository;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Materia;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.MateriaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MateriaDaoTest {

    @Autowired
    MateriaDao dao;

    Materia materiaA;
    Materia materiaB;
    Materia materiaC;

    @BeforeEach
    void before(){

        materiaA = Materia.builder()
                .name("MATEMATICA")
                .build();
        dao.save(materiaA);


        materiaB = Materia.builder()
                .name("FISICA")
                .build();
        dao.save(materiaB);


        materiaC = Materia.builder()
                .name("INGLES")
                .build();
        dao.save(materiaC);

    }

    @Test
    void testFindMateriasPorSiglas(){

        List<Materia> materias = dao.findByNameContaining("MATEMATICA");

        assertThat(materias.size()).isEqualTo(1);
        assertThat(materias.get(0).getName()).isEqualTo("MATEMATICA");


    }


    @Test
    void testguardarUsuario (){

        Materia materia = Materia.builder()
                .name("PROGRAMACION")
                .build();

        Materia materiaGuardada = dao.save(materia);

        assertThat(materiaGuardada).isNotNull();
        assertThat(materiaGuardada.getId()).isGreaterThan(0);

    }


    @Test
    void testActualizarUsuario (){

        Materia materia = Materia.builder()
                .name("PROGRAMACION")
                .build();

        Materia materiaBD = dao.save(materia);

        materiaBD.setName("LABORATORIO");


        Materia materiaActualizada = dao.save(materiaBD);

        assertThat(materiaActualizada.getName()).isEqualTo("LABORATORIO");



    }

    @Test
    void testEliminarUsuario(){

        dao.delete(materiaA);

       Optional <Materia> materia = dao.findById(materiaA.getId());

        assertThat(materia).isEmpty();

    }


    @Test
    void testListarEmpleados (){


        List<Materia> materias = dao.findAll();

        assertThat(materias).isNotNull();
        assertThat(materias.size()).isEqualTo(11);


    }


    @Test
    void testFindById(){

        Materia  materiaBd = dao.findById(materiaA.getId()).get();

        assertThat(materiaBd).isNotNull();
        assertThat(materiaBd.getName()).isEqualTo("MATEMATICA");



    }



}
