package com.gestionalumnosescuela1.gestionAlumnos.service;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.UsuarioDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static  org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    UsuarioDao dao;

    @InjectMocks
    UsuarioService service;

    Usuario usuarioA;
    Usuario usuarioB;
    Usuario usuarioC;

    @BeforeEach
    void before() {
        /* Inicializar usuarios y guardarlos en la base de datos antes de cada prueba */
        usuarioA = Usuario.builder()
                .id(1)
                .firstname("ez5")
                .lastname("smith")
                .email("ez5smith@hotmail.com")
                .build();
        // dao.save(usuarioA);

        usuarioB = Usuario.builder()
                .id(2)
                .firstname("ez6")
                .lastname("smith")
                .email("ez6smith@hotmail.com")
                .build();
        dao.save(usuarioB);

        usuarioC = Usuario.builder()
                .id(3)
                .firstname("ez7")
                .lastname("smith")
                .email("ez7smith@hotmail.com")
                .build();
        dao.save(usuarioC);
    }



    @Test
    void TestGuardarUsuario() {

        given(dao.save(usuarioA)).willReturn(usuarioA);

        Usuario usuarioGuardado = service.crearUsuario(usuarioA);

        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getEmail()).isEqualTo("ez5smith@hotmail.com");

    }

    @Test
    void testFindUsuarioById(){

        given(dao.findById(usuarioB.getId()) ).willReturn(Optional.of(usuarioB));

        Usuario usuarioEncontrado = service.findUsuario(usuarioB.getId());

       assertThat(usuarioEncontrado).isNotNull();
       assertThat(usuarioEncontrado.getFirstname()).isEqualTo("ez6");


    }

    @Test
    void testActualizarUsuario(){

        given(dao.save(usuarioC)).willReturn(usuarioC);
        usuarioC.setEmail("ez9smith@gmail.com");
        usuarioC.setFirstname("ez9");

        Usuario usuarioActualizado = service.updateUsuario(usuarioC);

        assertThat(usuarioActualizado.getEmail()).isEqualTo("ez9smith@gmail.com");
        assertThat(usuarioActualizado.getFirstname()).isEqualTo("ez9");

    }

    @Test
    void testEliminarUsuarioById(){

        service.eliminarUsuario(usuarioB.getId());
        verify(dao).deleteById(usuarioB.getId());

    }


}
