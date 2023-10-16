package com.gestionalumnosescuela1.gestionAlumnos.entityDao;



import com.gestionalumnosescuela1.gestionAlumnos.entity.Nota;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaDao extends JpaRepository<Nota, Integer> {

	  List<Nota> findByAlumno(Usuario alumno);
	
}
