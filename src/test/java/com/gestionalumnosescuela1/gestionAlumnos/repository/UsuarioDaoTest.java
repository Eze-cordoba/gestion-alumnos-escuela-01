package com.gestionalumnosescuela1.gestionAlumnos.repository;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.UsuarioDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static  org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UsuarioDaoTest {

    @Autowired
    UsuarioDao dao;

    Usuario usuarioA;
    Usuario usuarioB;
    Usuario usuarioC;

    @BeforeEach
    void before(){

        usuarioA = Usuario.builder()
                .firstname("ez5")
                .lastname("smith")
                .email("ez5smith@hotmail.com")
                .build();
        dao.save(usuarioA);


        usuarioB = Usuario.builder()
                .firstname("ez6")
                .lastname("smith")
                .email("ez6smith@hotmail.com")
                .build();

        dao.save(usuarioB);

        usuarioC = Usuario.builder()
                .firstname("ez7")
                .lastname("smith")
                .email("ez7smith@hotmail.com")
                .build();

        dao.save(usuarioC);

    }

    @Test
    void testguardarUsuario (){

        Usuario usuario = Usuario.builder()
                .firstname("ez3")
                .lastname("smith")
                .email("ez3smith@hotmail.com")
                .build();

        Usuario usuarioGuardado = dao.save(usuario);

        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getId()).isGreaterThan(0);

    }


    @Test
    void testActualizarUsuario (){

        Usuario usuario = Usuario.builder()
                .firstname("ez3")
                .lastname("smith")
                .email("ez3smith@hotmail.com")
                .build();

        Usuario usuarioBD = dao.save(usuario);

        usuarioBD.setFirstname("ez4");
        usuarioBD.setEmail("ez4smith@hotmail.com");

        Usuario usuarioActualizado = dao.save(usuarioBD);

        assertThat(usuarioActualizado.getFirstname()).isEqualTo("ez4");
        assertThat(usuarioActualizado.getEmail()).isEqualTo("ez4smith@hotmail.com");


    }

    @Test
    void testEliminarUsuario(){

        dao.delete(usuarioA);

        Optional<Usuario> usuario = dao.findById(usuarioA.getId());

        assertThat(usuario).isEmpty();



    }


    @Test
    void testListarEmpleados (){


        List<Usuario> usuarios = dao.findAll();

        assertThat(usuarios).isNotNull();
        assertThat(usuarios.size()).isEqualTo(3);


    }


    @Test
    void testFindById(){

        Usuario  usuarioBd = dao.findById(usuarioA.getId()).get();

        assertThat(usuarioBd).isNotNull();
        assertThat(usuarioBd.getFirstname()).isEqualTo("ez5");



    }



}
