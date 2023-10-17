package com.gestionalumnosescuela1.gestionAlumnos.entityDao;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaDao extends JpaRepository<Respuesta, Integer>{

}
