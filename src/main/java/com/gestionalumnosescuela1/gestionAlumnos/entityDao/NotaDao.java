package com.gestionalumnosescuela1.gestionAlumnos.entityDao;



import com.gestionalumnosescuela1.gestionAlumnos.entity.Nota;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotaDao extends JpaRepository<Nota, Integer> {

	  List<Nota> findByAlumno(Usuario alumno);
	
}
