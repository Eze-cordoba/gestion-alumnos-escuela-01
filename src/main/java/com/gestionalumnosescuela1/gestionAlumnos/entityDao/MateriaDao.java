package com.gestionalumnosescuela1.gestionAlumnos.entityDao;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MateriaDao extends JpaRepository<Materia, Long>{

  List	<Materia> findByNameContaining(String nombre);
	
}
