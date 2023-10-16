package com.gestionalumnosescuela1.gestionAlumnos.service;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Pregunta;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.PreguntaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PreguntaService {

	@Autowired
	private PreguntaDao preguntaDao;

	public List<Pregunta> findAll() {

		try {

			return preguntaDao.findAll();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo listar las preguntas");
		}

	}

	public Pregunta findPregunta(int id) {

		try {

			return preguntaDao.findById(id).orElse(null);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo encontrar la pregunta con el id "+ id);
		}

	}

	public Pregunta crearPregunta(Pregunta pregunta) {
	
		try {
		
		return preguntaDao.save(pregunta);
		
	} catch (Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear la pregunta");
	}
		
	}

	public void eliminarPregunta(int id) {

		try {

			preguntaDao.deleteById(id);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar la pregunta con el id " + id);
		}

	}

	public Pregunta updatePregunta(Pregunta pregunta) {

		try {

			return preguntaDao.save(pregunta);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar la pregunta");
		}

	}

}
