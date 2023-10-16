package com.gestionalumnosescuela1.gestionAlumnos.service;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Examen;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.ExamenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class ExamenService {
	@Autowired
	private ExamenDao examenDao;
	
	public List<Examen>  findAll () {
	
		try {
		
		return	examenDao.findAll();
			
	} catch (Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo listar los examenes");
	}
		
		}	
		
		public Examen findExamen(int id) {
		
			try {
				
			return examenDao.findById(id).orElse(null);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo encontrar el examen con el id "+ id);
		}
			
		}

		public Examen crearExamen(Examen examen) {
	
			try {

			return 	examenDao.save(examen);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear el examen");
		}
			
		}
		
	  public void eliminarExamen (int id) {
		
		  try {
			
             examenDao.deleteById(id);
		  
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el examen con el id" + id);
		}
		  
	  }

	  public Examen updateExamen(Examen examen) {
		  try {
			  return examenDao.save(examen);
		} catch (Exception e) {
			System.out.println(e.getMessage()  + e.getCause());
		} 		
		  
		  return null;
	  }
	  
	
	
}
