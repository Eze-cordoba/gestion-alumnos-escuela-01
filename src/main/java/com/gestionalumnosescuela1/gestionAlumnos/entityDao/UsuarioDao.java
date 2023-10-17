package com.gestionalumnosescuela1.gestionAlumnos.entityDao;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);
	
}
