package com.gestionalumnosescuela1.gestionAlumnos.entityDTO;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Pregunta;
import lombok.Data;


@Data
public class RespuestaDTO {

	
    private int id;
   
    private Pregunta pregunta;

    private String contenido;

    private boolean esCorrecta;
	
	
}
