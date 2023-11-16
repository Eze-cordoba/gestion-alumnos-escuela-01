package com.gestionalumnosescuela1.gestionAlumnos.entityDTO;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Materia;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Role;
import com.gestionalumnosescuela1.gestionAlumnos.entity.Usuario;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MateriaDTO {

	private Long id;
	
	private String name;
	
	private List<AlumnoDTO> alumnos=  new  ArrayList<AlumnoDTO>();
	
	private String teacherName;
	private ExamenDTO examen ;
	
	   public MateriaDTO(Materia materia) {
           this.name = materia.getName();
           this.id= materia.getId();
           this.teacherName = materia.getTeacherName();
      
        //  this.examen = materia.getExamen();
          //if (examen != null) {
        	  
        	//  examen.setId(materia.getExamen().getId());
        	 // examen.setMateria(null);
        	  
          //}
          
           for(Usuario alumno : materia.getAlumnos()  ) {
        	   
        	   if(alumno.getRole().equals(Role.ALUMNO)   ) { // si tienen rol admin no porque  son los profesores
        		   
        		     AlumnoDTO alumnoDTO = new AlumnoDTO();
                     alumnoDTO.setId(alumno.getId());
                     alumnoDTO.setFirstname(alumno.getFirstname());
                     alumnoDTO.setLastname(alumno.getLastname());
                     alumnoDTO.setEmail(alumno.getEmail());
                     alumnoDTO.setPassword(alumno.getPassword());
                     alumnos.add(alumnoDTO);
                     
        		   
        	   }
        	           
        	   
           }
           
       }

	public MateriaDTO() {
		
	}

	
	
}
