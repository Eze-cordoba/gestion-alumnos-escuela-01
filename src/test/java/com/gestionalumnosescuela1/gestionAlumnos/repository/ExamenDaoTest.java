package com.gestionalumnosescuela1.gestionAlumnos.repository;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Examen;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.ExamenDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ExamenDaoTest {

    @Autowired
    ExamenDao dao;

    Examen examenA;
    Examen examenB;
    Examen examenC;

    @BeforeEach
    void before() {
        /* Inicializar exámenes y guardarlos en la base de datos antes de cada prueba */
        examenA = Examen.builder()
                .titulo("examenA")
                .build();
        dao.save(examenA);

        examenB = Examen.builder()
                .titulo("examenB")
                .build();
        dao.save(examenB);

        examenC = Examen.builder()
                .titulo("examenC")
                .build();
        dao.save(examenC);
    }

    @Test
    void testguardarExamen() {
        /* Crear y guardar un nuevo examen en la base de datos */
        Examen examenD = Examen.builder()
                .titulo("examenD")
                .build();
        Examen examenGuardado = dao.save(examenD);

        /* Verificar que el examen guardado no sea nulo y que su ID sea mayor que 0 */
        assertThat(examenGuardado).isNotNull();
        assertThat(examenGuardado.getId()).isGreaterThan(0);
    }

    @Test
    void testActualizarExamen() {
        /* Crear y guardar un nuevo examen en la base de datos */
        Examen examenD = Examen.builder()
                .titulo("examenD")
                .build();
        Examen examenBD = dao.save(examenD);

        /* Actualizar el título del examen */
        examenBD.setTitulo("examenD2");
        Examen examenActualizado = dao.save(examenBD);

        /* Verificar que el título del examen guardado coincida con el título actualizado */
        assertThat(examenActualizado.getTitulo()).isEqualTo("examenD2");
    }

    @Test
    void testEliminarExamen() {
        /* Eliminar el examenA de la base de datos */
        dao.delete(examenA);

        /* Intentar encontrar el examen eliminado por su ID */
        Optional<Examen> examen = dao.findById(examenA.getId());

        /* Verificar que el examen no se encuentre en la base de datos */
        assertThat(examen).isEmpty();
    }

    @Test
    void testListarExamenes() {
        /* Obtener una lista de todos los exámenes en la base de datos */
        List<Examen> examenes = dao.findAll();

        /* Verificar que la lista no sea nula y que contenga la cantidad esperada de exámenes (3 en este caso) */
        assertThat(examenes).isNotNull();
        assertThat(examenes.size()).isEqualTo(3);
    }

    @Test
    void testFindById() {
        /* Buscar el examenA por su ID en la base de datos */
        Examen examenBd = dao.findById(examenA.getId()).get();

        /* Verificar que el examen encontrado no sea nulo y que su título coincida con el título original de examenA */
        assertThat(examenBd).isNotNull();
        assertThat(examenBd.getTitulo()).isEqualTo("examenA");
    }
}
