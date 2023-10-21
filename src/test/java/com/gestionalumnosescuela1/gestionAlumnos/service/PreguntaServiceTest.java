package com.gestionalumnosescuela1.gestionAlumnos.service;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Pregunta;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.PreguntaDao;
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
public class PreguntaServiceTest {


    @Mock
    PreguntaDao dao;

    @InjectMocks
    PreguntaService service;

    Pregunta preguntaA;
    Pregunta preguntaB;
    Pregunta preguntaC;

    @BeforeEach
    void before() {
        /* Inicializar usuarios y guardarlos en la base de datos antes de cada prueba */
        preguntaA = Pregunta.builder()
                .id(1)
                .enunciado("enunciadoA")
                .build();
        // dao.save(usuarioA);

        preguntaB = Pregunta.builder()
                .id(1)
                .enunciado("enunciadoB")
                .build();
        dao.save(preguntaB);

        preguntaC =Pregunta.builder()
                .id(1)
                .enunciado("enunciadoA")
                .build();;
        dao.save(preguntaC);
    }



    @Test
    void TestGuardarUsuario() {

        given(dao.save(preguntaA)).willReturn(preguntaA);

        Pregunta preguntaGuardada = service.crearPregunta(preguntaA);

        assertThat(preguntaGuardada).isNotNull();
        assertThat(preguntaGuardada.getEnunciado()).isEqualTo("enunciadoA");

    }

    @Test
    void testFindUsuarioById(){

        given(dao.findById(preguntaB.getId()) ).willReturn(Optional.of(preguntaB));

        Pregunta preguntaEncontrada = service.findPregunta(preguntaB.getId());

        assertThat(preguntaEncontrada).isNotNull();
        assertThat(preguntaEncontrada.getEnunciado()).isEqualTo("enunciadoB");


    }

    @Test
    void testActualizarUsuario(){

        given(dao.save(preguntaC)).willReturn(preguntaC);
        preguntaC.setEnunciado("enunciadoB2");

        Pregunta preguntaActualizada = service.updatePregunta(preguntaC);


        assertThat(preguntaActualizada.getEnunciado()).isEqualTo("enunciadoB2");

    }

    @Test
    void testEliminarUsuarioById(){

        service.eliminarPregunta(preguntaB.getId());
        verify(dao).deleteById(preguntaB.getId());

    }




}
