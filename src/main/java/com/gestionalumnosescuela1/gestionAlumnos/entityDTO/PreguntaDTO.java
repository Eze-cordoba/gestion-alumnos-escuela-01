package com.gestionalumnosescuela1.gestionAlumnos.entityDTO;


import com.gestionalumnosescuela1.gestionAlumnos.entity.Examen;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PreguntaDTO {

	
	
    private int id;


 
    private Examen examen;

    private String enunciado;

    
    private List<RespuestaDTO> respuestas = new ArrayList<>();
	
	
}
