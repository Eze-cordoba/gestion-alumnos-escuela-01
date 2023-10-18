package com.gestionalumnosescuela1.gestionAlumnos.repository;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Examen;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Pregunta;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.ExamenDao;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.PreguntaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PreguntaDaoTest {


    @Autowired
    PreguntaDao dao;

    Pregunta preguntaA;
    Pregunta preguntaB;
    Pregunta preguntaC;

    @BeforeEach
    void before(){
// Inicializar las preguntas y guardarlas en la base de datos antes de cada prueba
        preguntaA = Pregunta.builder()
                .enunciado("enunciadoA")
                .build();

        dao.save(preguntaA);

        preguntaB = Pregunta.builder()
                .enunciado("enunciadoB")
                .build();

        dao.save(preguntaB);

        preguntaC = Pregunta.builder()
                .enunciado("enunciadoC")
                .build();

        dao.save(preguntaC);

    }

    @Test
    void testguardarExamen (){
        // Crear una nueva pregunta y guardarla en la base de datos
      Pregunta  preguntaD = Pregunta.builder()
                .enunciado("enunciadoD")
                .build();


        Pregunta preguntaGuardado = dao.save(preguntaD);

        /* Verificar que la pregunta guardada no sea nula y que su ID sea mayor que 0 */
        assertThat(preguntaGuardado).isNotNull();
        assertThat(preguntaGuardado.getId()).isGreaterThan(0);

    }


    @Test
    void testActualizarExamen (){
        // Crear una nueva pregunta y guardarla en la base de datos
        Pregunta  preguntaF = Pregunta.builder()
                .enunciado("enunciadoF")
                .build();


        Pregunta preguntaBD = dao.save(preguntaF);

        // Actualizar el enunciado de la pregunta
        preguntaBD.setEnunciado("enunciadoF2");
        Pregunta preguntaActualizada = dao.save(preguntaBD);
        /* Verificar que el enunciado de la pregunta guardada coincida con el enunciado actualizado */
        assertThat(preguntaActualizada.getEnunciado()).isEqualTo("enunciadoF2");


    }

    @Test
    void testEliminarExamen(){
        // Eliminar la preguntaA de la base de datos
        dao.delete(preguntaA);
        // Intentar encontrar la pregunta eliminada por su ID
        Optional<Pregunta> pregunta = dao.findById(preguntaA.getId());
        /* Verificar que la pregunta no se encuentre en la base de datos */
        assertThat(pregunta).isEmpty();



    }


    @Test
    void testListarExamenes (){


        List<Pregunta> preguntas = dao.findAll();

        assertThat(preguntas).isNotNull();
        assertThat(preguntas.size()).isEqualTo(3);


    }


    @Test
    void testFindById(){
        // Obtener una lista de todas las preguntas en la base de datos
        Pregunta  preguntaBd = dao.findById(preguntaA.getId()).get();
        /* Verificar que la lista no sea nula y que contenga la cantidad esperada de preguntas (3 en este caso) */
        assertThat(preguntaBd).isNotNull();
        assertThat(preguntaBd.getEnunciado()).isEqualTo("enunciadoA");



    }


}
