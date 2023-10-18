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
    void before() {
        /* Inicializar materias y guardarlas en la base de datos antes de cada prueba */
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
    void testFindMateriasPorSiglas() {
        /* Buscar materias con el nombre "MATEMATICA" en su contenido */
        List<Materia> materias = dao.findByNameContaining("MATEMATICA");

        /* Verificar que la lista contenga una única materia y que su nombre sea "MATEMATICA" */
        assertThat(materias.size()).isEqualTo(1);
        assertThat(materias.get(0).getName()).isEqualTo("MATEMATICA");
    }

    @Test
    void testguardarMateria() {
        /* Crear y guardar una nueva materia en la base de datos */
        Materia materia = Materia.builder()
                .name("PROGRAMACION")
                .build();
        Materia materiaGuardada = dao.save(materia);

        /* Verificar que la materia guardada no sea nula y que su ID sea mayor que 0 */
        assertThat(materiaGuardada).isNotNull();
        assertThat(materiaGuardada.getId()).isGreaterThan(0);
    }

    @Test
    void testActualizarMateria() {
        /* Crear y guardar una nueva materia en la base de datos */
        Materia materia = Materia.builder()
                .name("PROGRAMACION")
                .build();
        Materia materiaBD = dao.save(materia);

        /* Actualizar el nombre de la materia */
        materiaBD.setName("LABORATORIO");
        Materia materiaActualizada = dao.save(materiaBD);

        /* Verificar que el nombre de la materia guardada coincida con el nombre actualizado */
        assertThat(materiaActualizada.getName()).isEqualTo("LABORATORIO");
    }

    @Test
    void testEliminarExamen() {
        /* Eliminar la materiaA de la base de datos */
        dao.delete(materiaA);

        /* Intentar encontrar la materia eliminada por su ID */
        Optional<Materia> materia = dao.findById(materiaA.getId());

        /* Verificar que la materia no se encuentre en la base de datos */
        assertThat(materia).isEmpty();
    }

    @Test
    void testListarMaterias() {
        /* Obtener una lista de todas las materias en la base de datos */
        List<Materia> materias = dao.findAll();

        /* Verificar que la lista no sea nula y que contenga la cantidad esperada de materias (11 en este caso) */
        assertThat(materias).isNotNull();
        assertThat(materias.size()).isEqualTo(11);
    }

    @Test
    void testFindById() {
        /* Buscar la materiaA por su ID en la base de datos */
        Materia materiaBd = dao.findById(materiaA.getId()).get();

        /* Verificar que la materia encontrada no sea nula y que su nombre coincida con el nombre original de materiaA */
        assertThat(materiaBd).isNotNull();
        assertThat(materiaBd.getName()).isEqualTo("MATEMATICA");
    }
}

