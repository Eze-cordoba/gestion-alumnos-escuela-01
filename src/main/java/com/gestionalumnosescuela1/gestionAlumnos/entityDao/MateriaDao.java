package com.gestionalumnosescuela1.gestionAlumnos.entityDao;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaDao extends JpaRepository<Materia, Integer>{

  List	<Materia> findByNameContaining(String nombre);
	
}
