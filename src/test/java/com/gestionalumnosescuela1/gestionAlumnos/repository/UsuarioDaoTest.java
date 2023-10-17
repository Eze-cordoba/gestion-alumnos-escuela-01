package com.gestionalumnosescuela1.gestionAlumnos.repository;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.UsuarioDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static  org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UsuarioDaoTest {

    @Autowired
    UsuarioDao dao;

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

    void testUpdateUsuario (){


    }



}
