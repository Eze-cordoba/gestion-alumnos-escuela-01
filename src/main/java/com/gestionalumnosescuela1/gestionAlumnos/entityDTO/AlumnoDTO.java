package com.gestionalumnosescuela1.gestionAlumnos.entityDTO;


import com.gestionalumnosescuela1.gestionAlumnos.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AlumnoDTO {

	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String rol;
	Map<String, Character> notas = new HashMap<>();
	
	
	private List<MateriaDTO> materias = new ArrayList<>();
	
	
	public AlumnoDTO(Usuario usuario) {

		this.id = usuario.getId();
		this.firstname = usuario.getFirstname();
		this.lastname = usuario.getLastname();
		this.email = usuario.getEmail();

		
		
		
		for (Materia materia : usuario.getMaterias()) {

		  
			//ACA  VOY A PONER POR CADA MATERIA , EL NOMBRE Y LA NOTA Y LA VOY ADD A EL MAP NOTAS;
			for (Nota nota : materia.getNotas()) {

				if (nota.getAlumno().equals(usuario)) {

					char notaa = nota.getNota();
					notas.put(materia.getName(), notaa);
				}

			}
			
			
			
			MateriaDTO materiaDTO = new MateriaDTO();
			materiaDTO.setId(materia.getId());
			materiaDTO.setName(materia.getName());
			materiaDTO.setTeacherName(materia.getTeacherName());

			if (materia.getExamen() != null) {
				System.out.println("ENTRA EN EXAMENNNNNNNNNN");
				ExamenDTO examen = new ExamenDTO();
				examen.setId(materia.getExamen().getId());
				// examen.setMateria(materiaDTO);
				examen.setTitulo(materia.getExamen().getTitulo());
				System.out.println("ENTRA EN EXAMENNNNNNNNNN22222222");

				for (Pregunta pregunta : materia.getExamen().getPreguntas()) {

					PreguntaDTO preguntaDto = new PreguntaDTO();
					preguntaDto.setEnunciado(pregunta.getEnunciado());
					preguntaDto.setId(pregunta.getId());

					for (Respuesta respuesta : pregunta.getRespuestas()) {
						RespuestaDTO respuestaDto = new RespuestaDTO();
						respuestaDto.setId(respuesta.getId());
						respuestaDto.setContenido(respuesta.getContenido());
						respuestaDto.setEsCorrecta(respuesta.isEsCorrecta());

						preguntaDto.getRespuestas().add(respuestaDto);

					}
					// termina de iterar y ponemos respuesta en pregunta

					examen.getPreguntas().add(preguntaDto);
				}

				System.out.println("ENTRA EN EXAMENNNNNNNNNN3333333333333");
				materiaDTO.setExamen(examen);
				System.out.println("ENTRA EN EXAMENNNNNNNNNN444444444444444444");
			}

			materias.add(materiaDTO);
		}

	}

	public AlumnoDTO() {

	}

}
