package com.gestionalumnosescuela1.gestionAlumnos.service;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Respuesta;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.RespuestaDao;
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
public class RespuestaServiceTest {

    @Mock
    RespuestaDao dao;

    @InjectMocks
    RespuestaService service;

    Respuesta respuestaA;
    Respuesta respuestaB;
    Respuesta respuestaC;

    @BeforeEach
    void before() {
        /* Inicializar usuarios y guardarlos en la base de datos antes de cada prueba */
       respuestaA = Respuesta.builder()
               .id(1)
               .contenido("respuesta1")
               .build();


        respuestaB = Respuesta.builder()
                .id(2)
                .contenido("respuesta2")
                .build();
        dao.save(respuestaB);

        respuestaC = Respuesta.builder()
                .id(3)
                .contenido("respuesta3")
                .build();
        dao.save(respuestaC);
    }



    @Test
    void testGuardarRespuesta() {

        given(dao.save(respuestaA)).willReturn(respuestaA);

        Respuesta respuestaGuardado = service.crearRespuesta(respuestaA);

        assertThat(respuestaGuardado).isNotNull();
    }

    @Test
    void testFindRespuestaById(){

        given(dao.findById(respuestaB.getId())).willReturn(Optional.of(respuestaB));

        Respuesta respuestaEncontrado = service.findRespuesta(respuestaB.getId());

        assertThat(respuestaEncontrado).isNotNull();
        assertThat(respuestaEncontrado.getContenido()).isEqualTo("respuesta2");


    }

    @Test
    void testActualizarRespuesta(){

        given(dao.save(respuestaC)).willReturn(respuestaC);
        respuestaC.setContenido("respuesta8");

        Respuesta respuestaActualizada = service.updateRespuesta(respuestaC);

        assertThat(respuestaActualizada.getContenido()).isEqualTo("respuesta8");
    }

    @Test
    void testEliminarRespuestaById(){

        service.eliminarRespuesta(respuestaC.getId());
        verify(dao).deleteById(respuestaC.getId());

    }



}
