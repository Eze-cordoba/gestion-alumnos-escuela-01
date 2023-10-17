package com.gestionalumnosescuela1.gestionAlumnos.entityDao;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreguntaDao extends JpaRepository<Pregunta, Integer>{

}
