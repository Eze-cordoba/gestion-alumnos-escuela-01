package com.gestionalumnosescuela1.gestionAlumnos.service;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.MateriaDao;
import com.gestionalumnosescuela1.gestionAlumnos.entityDao.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private MateriaDao materiaDao;
	
	
	public List<Usuario>  findAll () {
	
		try {
	
	return	usuarioDao.findAll();
	} catch (Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo listar los usuarios");
	}

		
	}	
	
	
	public Usuario findUsuario(int id) {
	
		try {
			
		return usuarioDao.findById(id).orElse(null);
		
	} catch (Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo encontrar al usuario con el id" + id);
	}
		
	}

	
	public Usuario crearUsuario(Usuario usuario) {

		try {
		return usuarioDao.save(usuario);
	} catch (Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear al usuario");
	}
		
	}
	
	
  public void eliminarUsuario (int id) {
	 
	  try {
		
	  usuarioDao.deleteById(id);
	
	} catch (Exception e) {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar al usuario con el id"+ id);
	}
	  
  }

  
  public Usuario updateUsuario(Usuario usuario) {
	 
	  try {
		  
      return usuarioDao.save(usuario);
	  
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar al usuario");
		}
	  
  }
  
	
}
	
	

