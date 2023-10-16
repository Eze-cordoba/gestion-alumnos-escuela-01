package com.gestionalumnosescuela1.gestionAlumnos.entityDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamenDTO {

	    private int id;
 
	//    @OneToOne
	   // @JoinColumn(name = "materia_id")
	    //private Materia materia;  //cuando llamo a materia materia va a llamar a alumnos y bucle. 

	    private String titulo;

	    //private MateriaDTO materia;
	  
	    private List<PreguntaDTO> preguntas = new ArrayList<>();

	
	
	
}
