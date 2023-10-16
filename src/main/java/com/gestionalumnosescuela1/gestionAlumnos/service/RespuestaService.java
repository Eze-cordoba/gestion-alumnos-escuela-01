package com.gestionalumnosescuela1.gestionAlumnos.service;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Respuesta;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.RespuestaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RespuestaService {

	@Autowired
	private RespuestaDao respuestaDao;

	public List<Respuesta> findAll() {

		try {

			return respuestaDao.findAll();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo listar las respuestas");
		}

	}

	public Respuesta findRespuesta(int id) {

		try {

			return respuestaDao.findById(id).orElse(null);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo encontrar la respuesta con el id "+ id );
		}

	}

	public Respuesta crearRespuesta(Respuesta respuesta) {

		try {

			return respuestaDao.save(respuesta);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear la respuesta" );
		}

	}

	public void eliminarRespuesta(int id) {

		try {

			respuestaDao.deleteById(id);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar la respuesta con el id " + id);
		}
	}

	public Respuesta updateRespuesta(Respuesta respuesta) {

		try {

			return respuestaDao.save(respuesta);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar la respuesta ");
		}

	}

}
