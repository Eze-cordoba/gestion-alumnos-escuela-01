package com.gestionalumnosescuela1.gestionAlumnos.service;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Nota;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.NotaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotaService {

	@Autowired
	private NotaDao notaDao;
	
	
	
public List<Nota>  findAll () {
		
		return	notaDao.findAll();
			
		}	
		
		public Nota findNota(int id) {
			return notaDao.findById(id).orElse(null);
		}

		 public List<Nota> obtenerNotasPorAlumno(Usuario alumno) {
			
			return notaDao.findByAlumno(alumno);
			
			
		}
		
		 
		 
		 
		public Nota crearNota(Nota nota) {
		return 	notaDao.save(nota);
		
		}
		
	  public void eliminarNota (int id) {
		  
		  
		  notaDao.deleteById(id);
		  
	  }

	  public Nota updateNota(Nota nota) {
		  try {
			  return notaDao.save(nota);
		} catch (Exception e) {
		    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada con ID: " + nota.getId());
			//System.out.println(e.getMessage()  + e.getCause());
		} 		
		  
		  
	  }
	
}
