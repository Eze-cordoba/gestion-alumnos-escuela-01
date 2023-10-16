package com.gestionalumnosescuela1.gestionAlumnos.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gestionalumnosescuela1.gestionAlumnos.entity.*;
import com.gestionalumnosescuela1.gestionAlumnos.entityDTO.*;
import com.gestionalumnosescuela1.gestionAlumnos.security.auth.AuthenticationResponse;
import com.gestionalumnosescuela1.gestionAlumnos.security.auth.AuthenticationService;
import com.gestionalumnosescuela1.gestionAlumnos.security.auth.RegisterRequest;
import com.gestionalumnosescuela1.gestionAlumnos.security.auth.UpdateRequest;
import com.gestionalumnosescuela1.gestionAlumnos.security.config.LogoutService;
import com.gestionalumnosescuela1.gestionAlumnos.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/utn")
@RequiredArgsConstructor
public class controller {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private MateriaService materiaService;

	@Autowired
	private ExamenService examenService;

	@Autowired
	private PreguntaService preguntaService;
	@Autowired
	private RespuestaService respuestaService;
	@Autowired
	private NotaService notaService;

	@Autowired
	private final AuthenticationService service;

	@Autowired
	private final LogoutService logoutService;

	@GetMapping("/alumnos/listarAlumnos")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> listarAlumnos() {

		Map<String, Object> response = new HashMap<>();

		try {

			List<Usuario> usuarios = usuarioService.findAll();
			List<AlumnoDTO> alumnosDTO = new ArrayList<>();

			for (Usuario alumno : usuarios) {

				if (alumno.getRole().equals(Role.MANAGER)) { // LOS QUE TIENEN ROL ADMIN (PROFESORES ) NO LOS INSERTA EN
																// L // LISTA

					AlumnoDTO alumnoDTO = new AlumnoDTO(alumno);
					alumnoDTO.setRol("alumno");
					alumnosDTO.add(alumnoDTO);
				}

			}

			return ResponseEntity.ok(alumnosDTO);

		} catch (ResponseStatusException e) {
			response.put("message","error al listar alumnos");
			response.put("error", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(response);

		} catch (Exception e) {

			response.put("error", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

		}

	}

	@GetMapping("/alumnos/materiasDelProfesor/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> materiasDelProfesor(@PathVariable int id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Usuario usuario = usuarioService.findUsuario(id);

			List<MateriaDTO> materias = new ArrayList<>();

			for (Materia materia : usuario.getMaterias()) {

				MateriaDTO materiaDto = new MateriaDTO(materia);
				materias.add(materiaDto);
			}
			return ResponseEntity.ok(materias);

		} catch (ResponseStatusException e) {
			response.put("message","error al listar materias del profesor");
			response.put("error", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(response);

		} catch (Exception e) {

			response.put("error", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

		}

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("/alumnos/{id}")
	public ResponseEntity<?> verUsuario(@PathVariable int id) {

		Map<String, Object> response = new HashMap<>();
		try {

			Usuario usuario = usuarioService.findUsuario(id);

			if (usuario != null) {
				AlumnoDTO alumnoDTO = new AlumnoDTO(usuario);

				return new ResponseEntity<AlumnoDTO>(alumnoDTO, HttpStatus.OK);

			} else {

				return (ResponseEntity<?>) ResponseEntity.notFound();

			}

		} catch (ResponseStatusException e) {

			response.put("error", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(response);

		} catch (Exception e) {
			response.put("message","error al mostrar usuario");
			response.put("error", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

		}

	}

	@PostMapping("/alumnos/crearAlumno")
	public ResponseEntity<?> crearAlumno(@RequestBody RegisterRequest request) {
		// CADA ALUMNO QUE SE REGISTRE VA A TENER ROL MANAGER (DESPUES CAMBIAR NAME A
		// ROL ALUMNO)
		Map<String, Object> errorResponse = new HashMap<>();
		
		
		try {


			if (request.getRole().equals(Role.ADMIN)) {
				System.out.println("CREANDO PROFESOR ");
			} else {
				System.out.println("CREANDO ALUMNO");

			}

			// Role rol = Role.MANAGER;
			// request.setRole(rol);

			AuthenticationResponse authenticationResponse = service.register(request);

			// SI EL AUTHENTICATIONRESPONSE , EL ATRIBUTO ERROR TIENE ALGO HAY UN ERROR
			if (authenticationResponse.getError() != null) {
				// Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", authenticationResponse.getError()); // PONEMOS EL ERROR EN LA RESPUESTA
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // MANDAMOS EL ERROR
																									// Y
																									// EL CODGIO 500 DE
																									// INTERNAL SERVER
																									// ERROR

			} else {
				return ResponseEntity.ok(authenticationResponse);
			}

		} catch (ResponseStatusException e) {
          
			errorResponse.put("message","error al crear alumno");
			errorResponse.put("error", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);

		} catch (Exception e) {

			errorResponse.put("error", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

		}

		
		
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@DeleteMapping("/eliminarCuenta/{idUsuario}")
	public ResponseEntity<?> eliminarCuenta(@PathVariable int idUsuario) {

		System.out.println("LLEGA SFASF ASF ASFFAS AF S");
		Map<String, Object> errorResponse = new HashMap<>();
		try {

			Usuario alumno = usuarioService.findUsuario(idUsuario);

			List<Nota> notas = notaService.obtenerNotasPorAlumno(alumno);

			for (Nota nota : notas) {
				notaService.eliminarNota(nota.getId()); // Elimina cada nota
			}

			usuarioService.eliminarUsuario(idUsuario);

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);

		} catch (ResponseStatusException e) {

			errorResponse.put("error", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);

		} catch (Exception e) {
            
			errorResponse.put("message","error al eliminar cuenta");
			errorResponse.put("error", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

		}

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("/cerrarSesion")
	public ResponseEntity<Map<String, String>> cerrarSesion(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			// Cierre de sesión de Spring Security
			new SecurityContextLogoutHandler().logout(request, response, authentication);

			// Llama al servicio de cierre de sesión
			logoutService.logout(request, response, authentication);
		}

		// Devuelve un objeto JSON con el mensaje
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("message", "Sesión cerrada exitosamente");
		return ResponseEntity.ok(responseMap);
	}

	
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PutMapping("/editarAlumno/{id}")
	public ResponseEntity<AuthenticationResponse> editarAlumno(@PathVariable int id, @RequestBody Usuario usuario) {

		Map<String, Object> response = new HashMap<>();
		System.out.println(usuario);
		UpdateRequest request = UpdateRequest.builder().firstname(usuario.getFirstname())
				.lastname(usuario.getLastname()).email(usuario.getEmail()).password(usuario.getPassword()).build();

		System.out.println(request);
		try {

			AuthenticationResponse authenticationResponse = service.editarAlumno(id, request);
			return ResponseEntity.ok(authenticationResponse);

		} catch (Exception e) {
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PutMapping("/agregarMateria/{idAlumno}")
	public ResponseEntity<?> agregarMateriaAlumno(@PathVariable int idAlumno, @RequestBody String Materia)
			throws JsonMappingException, JsonProcessingException {

		Map<String, Object> response = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = objectMapper.readTree(Materia);

		// Obtener el valor de la propiedad "materia"
		String materiaValue = jsonNode.get("materia").get(0).asText();
		System.out.println(materiaValue);

		Usuario usuario = usuarioService.findUsuario(idAlumno);

		Materia materia = materiaService.encontrarMateriaPorNombre(materiaValue);

		// VAMOS A ITERAR PARA BUSCAR SI EL ALUMNO O PROFESOR YA ESTA INSCRIPTO EN ESA
		// MATERIA
		for (Materia materiaa : usuario.getMaterias()) {

			if (materiaa.getName().equalsIgnoreCase(materiaValue)) {

				if (usuario.getRole().equals(Role.ADMIN)) {
					response.put("error", "El profesor  ya esta inscripto  en la materia");
				} else {
					response.put("error", "El alumno ya esta inscripto en la materia");
				}

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}

		if (usuario.getRole().equals(Role.ADMIN)) {

			for (Usuario usuarioo : materia.getAlumnos()) {

				if (usuarioo.getRole().equals(Role.ADMIN)) {
					response.put("error", "Ya hay un profesor dictando esta materia");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}

			}

		}

		if (usuario.getRole().equals(Role.ADMIN)) { // it mean that the teacher is quien esta inscribiendose por lo
													// tanto el nombre del profesor tiene que estar en
													// materia.teachername
			materia.setTeacherName(usuario.getFirstname());
		} else {
		}

		usuario.getMaterias().add(materia);

		usuarioService.crearUsuario(usuario);

		response.put("message", "El alumno fue agregado correctamente a la materia");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@DeleteMapping("eliminarAlumno/{id}")
	public ResponseEntity<?> eliminarAlumno(@PathVariable int id) {

		Map<String, Object> response = new HashMap<>();

		usuarioService.eliminarUsuario(id);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@GetMapping("/listarMaterias")
	public List<MateriaDTO> listarMaterias() {

		List<Materia> materias = materiaService.findAll();
		List<MateriaDTO> materiasDTO = new ArrayList<>();

		for (Materia materia : materias) {
			MateriaDTO materiaDTO = new MateriaDTO(materia);
			materiasDTO.add(materiaDTO);
		}

		return materiasDTO;
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("/verMateria/{id}")
	public MateriaDTO verMateria(@PathVariable int id) {
		System.out.println("LLEGA A VER MATERIA");
		Materia materia = materiaService.findMateria(id);
		MateriaDTO materiaDTO = new MateriaDTO(materia);

		System.out.println("Y ASI SE VA : " + materiaDTO);
		return materiaDTO;

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PostMapping("/crearExamen/{idMateria}")
	public ResponseEntity<?> crearExamen(@PathVariable int idMateria, @RequestBody List<Pregunta> preguntas) {

		Map<String, Object> response = new HashMap<>();

		try {

			Materia materia = materiaService.findMateria(idMateria);

			if (materia.getExamen() != null) {

				response.put("error", "ya hay un examen creado para esta materia");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			List<Pregunta> preguntasPersistidas = new ArrayList<>();
			List<Respuesta> respuestasPersistidas = new ArrayList<>();

			// Crear y persistir el examen primero
			Examen examen = new Examen();
			examen.setTitulo("Título del examen");
			examen.setMateria(materia);
			Examen examenPersistido = examenService.crearExamen(examen);

			// Iterar sobre las preguntas y sus respuestas
			for (Pregunta pregunta : preguntas) {
				Pregunta preguntaPersistida = preguntaService.crearPregunta(pregunta);
				preguntaPersistida.setExamen(examenPersistido);
				preguntasPersistidas.add(preguntaPersistida);

				for (Respuesta respuesta : pregunta.getRespuestas()) {
					Respuesta respuestaPersistida = respuestaService.crearRespuesta(respuesta);
					respuestaPersistida.setPregunta(preguntaPersistida);
					respuestaService.updateRespuesta(respuestaPersistida);
				}
			}

			examenPersistido.setPreguntas(preguntasPersistidas);

			response.put("message", "examen creado con exito");

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

		} catch (Exception e) {
			response.put("message", "error al crear el examen");
			response.put("error", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("/mostrarExamen/{idExamen}")
	public ResponseEntity<?> mostrarExamen(@PathVariable int idExamen) { // CREA UN EXAMEN DTO Y PONELE LOS VALORES VOS
																			// MISMO MENOS LOS BOOLEAN NI LAS RELACIONES

		Map<String, Object> response = new HashMap<>();
		try {
			Examen examen = materiaService.findMateria(idExamen).getExamen();

			if (examen == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Examen no encontrado.");
			}

			// Puedes convertir el examen a un DTO si es necesario para evitar bucles
			ExamenDTO examenDTO = new ExamenDTO();

			for (Pregunta pregunta : examen.getPreguntas()) {
				PreguntaDTO preguntaDTO = new PreguntaDTO();
				preguntaDTO.setEnunciado(pregunta.getEnunciado());
				preguntaDTO.setId(pregunta.getId());

				for (Respuesta respuesta : pregunta.getRespuestas()) {
					RespuestaDTO respuestaDTO = new RespuestaDTO(); // Debes crear un nuevo objeto para cada respuesta
					respuestaDTO.setContenido(respuesta.getContenido());
					respuestaDTO.setId(respuesta.getId());

					preguntaDTO.getRespuestas().add(respuestaDTO);
				}

				examenDTO.getPreguntas().add(preguntaDTO);
			}

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(examenDTO);

		} catch (Exception e) {

			response.put("message", "error al mostrar el examen");
			response.put("error", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@PostMapping("/corregirExamen/{idExamen}/{idAlumno}")
	public ResponseEntity<?> corregirExamen(@RequestBody Examen examen, @PathVariable int idExamen,
			@PathVariable int idAlumno) {

		Usuario alumno = usuarioService.findUsuario(idAlumno);

		Examen examenBD = materiaService.findMateria(idExamen).getExamen();

		char notaFinal = calificarExamen(examenBD, examen);

		Nota nota = new Nota();
		nota.setAlumno(alumno);
		nota.setNota(notaFinal);
		nota.setMateria(examenBD.getMateria());

		// Guardar la nota en la base de datos usando notaService
		nota = notaService.crearNota(nota);

		System.out.println(nota.getNota());

		return null;

	}

	public char calificarExamen(Examen examenBD, Examen examenDelAlumno) {

		int contRespuestasCorrectas = 0;
		int contRespuestasTotales = 0;

		int contPreguntas = 0;
		int contRespuestas = 0;

		for (Pregunta pregunta : examenBD.getPreguntas()) {

			for (Respuesta respuesta : pregunta.getRespuestas()) {

				if (respuesta.isEsCorrecta() == examenDelAlumno.getPreguntas().get(contPreguntas).getRespuestas()
						.get(contRespuestas).isEsCorrecta()) {

					contRespuestasCorrectas = contRespuestasCorrectas + 1;

				}

				contRespuestasTotales = contRespuestasTotales + 1;
				contRespuestas = contRespuestas + 1;
			}

			contPreguntas = contPreguntas + 1;
			contRespuestas = 0; // se reinicia el numero de respuestas
		}

		double porcentaje = ((double) contRespuestasCorrectas / contRespuestasTotales) * 100;

		return obtenerCalificacion((int) porcentaje);
	}

	public static char obtenerCalificacion(int puntaje) {

		char calificacion;

		switch (puntaje / 20) {
		case 0:
		case 1:
			calificacion = 'F';
			break;
		case 2:
			calificacion = 'D';
			break;
		case 3:
			calificacion = 'C';
			break;
		case 4:
			calificacion = 'B';
			break;
		case 5:
			calificacion = 'A';
			break;
		default:
			calificacion = 'N'; // Valor por defecto si el puntaje está fuera del rango
			break;
		}

		return calificacion;
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("mostrarNotas/{alumnoId}")
	public Map<String, Character> mostrarNotas(@PathVariable int alumnoId) {
		Usuario alumno = usuarioService.findUsuario(alumnoId);

		if (alumno != null) {
			Map<String, Character> notas = new HashMap<>();

			// Supongamos que tienes un método para obtener todas las materias inscritas por
			// el alumno
			List<Materia> materias = alumno.getMaterias();

			for (Materia materia : materias) {

				for (Nota nota : materia.getNotas()) {

					if (nota.getAlumno().equals(alumno)) {

						char notaa = nota.getNota();
						notas.put(materia.getName(), notaa);
					}

				}

			}

			System.out.println(notas);

			return notas;

		} else {
			// En caso de que el alumno no exista, puedes devolver un mapa vacío o un
			// mensaje de error
			return Collections.emptyMap(); // o cualquier otra acción apropiada
		}
	}

}
