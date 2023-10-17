package com.gestionalumnosescuela1.gestionAlumnos.service;

import com.gestionalumnosescuela1.gestionAlumnos.entity.Materia;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.MateriaDao;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MateriaService {

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MateriaDao materiaDao;

	public List<Materia> findAll() {

		try {
			List<Materia> materias = materiaDao.findAll();

			if (materias == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo encontrar las materias");
			}

			return materiaDao.findAll();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo listar las materias");
		}

	}

	public Materia findMateria(Long id) {
		try {

			return materiaDao.findById(id).orElse(null);

		} catch (Exception ex) {
			// Manejo de otras excepciones generales, si es necesario.
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se pudo encontrar la materia con el id :  " + id);
		}

	}

	public void eliminarMateria(long id) {

		try {
			materiaDao.deleteById(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se pudo eliminar  la materia con el id :  " + id);
		}

	}

	public Materia updateMateria(Materia materia) {

		try {
			return materiaDao.save(materia);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo actualizar   la materia");
		}

	}

	public Materia encontrarMateriaPorNombre(String nombre) {

		System.out.println("IDA : " + nombre);

		try {
			return materiaDao.findByNameContaining(nombre).get(0);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo encontrar    la materia");
		}

	}

}
