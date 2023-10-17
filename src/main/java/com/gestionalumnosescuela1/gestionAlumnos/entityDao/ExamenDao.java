package com.gestionalumnosescuela1.gestionAlumnos.entityDao;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamenDao extends JpaRepository<Examen, Integer>{

}
