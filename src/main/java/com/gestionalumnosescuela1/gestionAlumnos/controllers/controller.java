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
import java.util.stream.Collectors;

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


	/**
	 * Este método obtiene y lista a los alumnos y responde con una lista de objetos AlumnoDTO.
	 *
	 * @return ResponseEntity con la lista de AlumnoDTO en el cuerpo de la respuesta (código de estado 200 - OK) si tiene éxito.
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@GetMapping("/alumnos/listarAlumnos")
	@PreAuthorize("hasRole('PROFESOR')")
	public ResponseEntity<?> listarAlumnos() {

		Map<String, Object> response = new HashMap<>();

		try {

			List<Usuario> usuarios = usuarioService.findAll();
			//los usuarios con rol manager (alumnos) se les asignara el nombre del rol alumno
			List<AlumnoDTO> alumnosDTO = usuarios.stream()
					.filter(usuario -> usuario.getRole().equals(Role.ALUMNO))
					.map(alumno -> {
						AlumnoDTO alumnoDTO = new AlumnoDTO(alumno);
						alumnoDTO.setRol("alumno");
						return alumnoDTO;
					})
					.collect(Collectors.toList());


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

	/**
	 * Este método obtiene y lista las materias de un profesor identificado por su ID.
	 *
	 * @param id El ID del profesor del que se desean listar las materias.
	 * @return ResponseEntity con la lista de MateriaDTO en el cuerpo de la respuesta (código de estado 200 - OK) si tiene éxito.
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@GetMapping("/alumnos/materiasDelProfesor/{id}")
	@PreAuthorize("hasRole('PROFESOR')")
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

	/**
	 * Este método obtiene y muestra información detallada de un usuario por su ID.
	 *
	 * @param id El ID del usuario que se desea mostrar.
	 * @return ResponseEntity con un objeto AlumnoDTO en el cuerpo de la respuesta (código de estado 200 - OK) si el usuario existe.
	 *         En caso de que el usuario no exista, responde con código de estado 404 (No encontrado).
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
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


	/**
	 * Este método permite crear un nuevo alumno o profesor y asignarle un rol específico (MANAGER o ADMIN).
	 *
	 * @param request La solicitud de registro que contiene los datos del nuevo usuario.
	 * @return ResponseEntity con AuthenticationResponse en el cuerpo de la respuesta (código de estado 200 - OK) si el registro es exitoso.
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@PostMapping("/alumnos/crearAlumno")
	public ResponseEntity<?> crearAlumno(@RequestBody RegisterRequest request) {

		Map<String, Object> errorResponse = new HashMap<>();
		try {

			if (request.getRole().equals(Role.PROFESOR)) {
				System.out.println("CREANDO PROFESOR ");
			} else {
				System.out.println("CREANDO ALUMNO");

			}

			AuthenticationResponse authenticationResponse = service.register(request);

			// Si el AuthenticationResponse contiene un error, se maneja como un error interno del servidor
			if (authenticationResponse.getError() != null) {
				errorResponse.put("error", authenticationResponse.getError());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			} else {
				// Si no hay error, responder con el objeto AuthenticationResponse y código 200 (OK)
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

	/**
	 * Este método permite eliminar la cuenta de un usuario (alumno o profesor) identificado por su ID.
	 *
	 * @param idUsuario El ID del usuario cuya cuenta se desea eliminar.
	 * @return ResponseEntity con código de estado 202 (ACEPTADO) en caso de éxito al eliminar la cuenta.
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
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

	/**
	 * Este método permite cerrar la sesión del usuario actual.
	 *
	 * @param request   El objeto HttpServletRequest para la solicitud.
	 * @param response  El objeto HttpServletResponse para la respuesta.
	 * @return ResponseEntity con un objeto JSON que contiene un mensaje de confirmación (código de estado 200 - OK) cuando la sesión se cierra exitosamente.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
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

	/**
	 * Este método permite editar los datos de un alumno o profesor identificado por su ID.
	 *
	 * @param id      El ID del usuario (alumno o profesor) cuyos datos se desean editar.
	 * @param usuario Los nuevos datos del usuario proporcionados en el cuerpo de la solicitud.
	 * @return ResponseEntity con AuthenticationResponse en el cuerpo de la respuesta (código de estado 200 - OK) si la edición es exitosa.
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
	@PutMapping("/editarAlumno/{id}")
	public ResponseEntity<AuthenticationResponse> editarAlumno(@PathVariable int id, @RequestBody Usuario usuario) {

		Map<String, Object> response = new HashMap<>();
		System.out.println(usuario);
		UpdateRequest request = UpdateRequest.builder().firstname(usuario.getFirstname())
				.lastname(usuario.getLastname()).email(usuario.getEmail()).password(usuario.getPassword()).build();

		try {
			AuthenticationResponse authenticationResponse = service.editarAlumno(id, request);
			return ResponseEntity.ok(authenticationResponse);

		} catch (Exception e) {
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Este método permite agregar una materia a un alumno o profesor identificado por su ID.
	 *
	 * @param idAlumno El ID del usuario (alumno o profesor) al que se le agregará la materia.
	 * @param Materia  Los datos de la materia a agregar, proporcionados en el cuerpo de la solicitud como una cadena JSON.
	 * @return ResponseEntity con un objeto JSON que contiene un mensaje de éxito o error (código de estado 202 - ACEPTADO si es exitoso).
	 *         En caso de error, responde con un mensaje de error y el código de estado correspondiente.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
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

		// iteramos para ver si el alumno o profesor ya esta inscripto en la materia
		for (Materia materiaa : usuario.getMaterias()) {

			if (materiaa.getName().equalsIgnoreCase(materiaValue)) {

				if (usuario.getRole().equals(Role.PROFESOR)) {
					response.put("error", "El profesor  ya esta inscripto  en la materia");
				} else {
					response.put("error", "El alumno ya esta inscripto en la materia");
				}

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}

		//si es un profesor , iteramos para ver si ya hay otro profesor dictando la materia
		if (usuario.getRole().equals(Role.PROFESOR)) {

			for (Usuario usuarioo : materia.getAlumnos()) {

				if (usuarioo.getRole().equals(Role.PROFESOR)) {
					response.put("error", "Ya hay un profesor dictando esta materia");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}

			}

		}

		if (usuario.getRole().equals(Role.PROFESOR)) {
			// ponemos el nombre del profesor en la materia que dicta
			materia.setTeacherName(usuario.getFirstname());
		}

		usuario.getMaterias().add(materia);

		usuarioService.crearUsuario(usuario);

		response.put("message", "El alumno fue agregado correctamente a la materia");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);

	}

	/**
	 * Este método permite eliminar a un alumno o profesor identificado por su ID.
	 *
	 * @param id El ID del usuario (alumno o profesor) que se desea eliminar.
	 * @return ResponseEntity con un objeto JSON vacío (código de estado 200 - OK) para indicar que el usuario se ha eliminado correctamente.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
	@DeleteMapping("eliminarAlumno/{id}")
	public ResponseEntity<?> eliminarAlumno(@PathVariable int id) {

		Map<String, Object> response = new HashMap<>();

		usuarioService.eliminarUsuario(id);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/**
	 * Este método permite listar todas las materias disponibles y convertirlas en una lista de objetos MateriaDTO.
	 *
	 * @return Una lista de objetos MateriaDTO que representan las materias disponibles.
	 */
	@GetMapping("/listarMaterias")
	public List<MateriaDTO> listarMaterias() {

		List<Materia> materias = materiaService.findAll();
		List<MateriaDTO> materiasDTO = new ArrayList<>();

		for (Materia materia : materias) {
			MateriaDTO materiaDTO = new MateriaDTO(materia);
			materiasDTO.add(materiaDTO);
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(materiasDTO).getBody();

	}

	/**
	 * Este método permite ver los detalles de una materia específica a través de su ID.
	 *
	 * @param id El ID de la materia que se desea ver.
	 * @return Un objeto MateriaDTO que representa los detalles de la materia.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
	@GetMapping("/verMateria/{id}")
	public ResponseEntity<?> verMateria(@PathVariable Long id) {

		Materia materia = materiaService.findMateria(id);
		MateriaDTO materiaDTO = new MateriaDTO(materia);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(materiaDTO);

	}

	/**
	 * Este método permite crear un examen para una materia específica y agregar preguntas con sus respuestas al examen.
	 *
	 * @param idMateria El ID de la materia a la que se asociará el examen.
	 * @param preguntas La lista de preguntas con respuestas que se agregarán al examen.
	 * @return ResponseEntity con un objeto JSON que indica si el examen se creó con éxito o si hubo un error.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
	@PostMapping("/crearExamen/{idMateria}")
	public ResponseEntity<?> crearExamen(@PathVariable long idMateria, @RequestBody List<Pregunta> preguntas) {

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

	/*
	 * Este método busca y muestra un examen, convirtiéndolo en un objeto DTO.
	 * Excluye las relaciones y los valores booleanos.
	 *
	 * @param idExamen El ID del examen a mostrar.
	 * @param idAlumno El ID del alumno relacionado con el examen.
	 * @param examen Un objeto Examen que contiene los datos del examen a calificar.
	 * @return ResponseEntity con el resultado de la corrección del examen.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
	@GetMapping("/mostrarExamen/{idExamen}")
	public ResponseEntity<?> mostrarExamen(@PathVariable long idExamen) { // CREA UN EXAMEN DTO Y PONELE LOS VALORES VOS
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





	/*
	 * Este método permite calificar un examen y registrar la nota en la base de datos.
	 *
	 * @param idExamen El ID del examen a calificar.
	 * @param idAlumno El ID del alumno al que se le asignará la nota.
	 * @param examen Un objeto Examen que contiene los datos del examen calificado.
	 * @return ResponseEntity con un mensaje de éxito (HTTP 200 OK) después de calificar el examen y registrar la nota.
	 */
	@PostMapping("/corregirExamen/{idExamen}/{idAlumno}")
	public ResponseEntity<String> corregirExamen(@RequestBody Examen examen, @PathVariable Long idExamen,
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

		// Devuelve una respuesta HTTP 200 OK
		return ResponseEntity.ok("Examen corregido y nota registrada exitosamente.");
	}

	/*
	 * Este método calcula la calificación de un examen comparando las respuestas del examen del alumno con las respuestas correctas del examen de referencia.
	 *
	 * @param examenBD             El examen de referencia con las respuestas correctas.
	 * @param examenDelAlumno      El examen del alumno que se va a calificar.
	 * @return La calificación en forma de carácter (por ejemplo, A, B, C, etc.) basada en el porcentaje de respuestas correctas.
	 */
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

	/**
	 * Este método calcula la calificación en forma de carácter (A, B, C, D, F) basada en un puntaje numérico.
	 *
	 * @param puntaje El puntaje numérico para el cual se va a determinar la calificación.
	 * @return La calificación en forma de carácter (A, B, C, D, F) basada en el puntaje proporcionado.
	 */
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

	/**
	 * Este método muestra las notas de un alumno en sus materias inscritas.
	 *
	 * @param alumnoId El ID del alumno para el cual se mostrarán las notas.
	 * @return Un mapa que asocia el nombre de la materia con su calificación en forma de carácter.
	 *         Si el alumno no existe o no tiene notas registradas, se devolverá un mapa vacío.
	 */
	@PreAuthorize("hasAnyRole('PROFESOR', 'ALUMNO')")
	@GetMapping("mostrarNotas/{alumnoId}")
	public  ResponseEntity<?> mostrarNotas(@PathVariable int alumnoId) {
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

			return ResponseEntity.ok(notas);

		} else {
			// En caso de que el alumno no exista, puedes devolver un mapa vacío o un
			// mensaje de error
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alumno no encontrado"); // o cualquier otra acción apropiada
		}
	}


	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> eliminarExamen (){




		return null;
	}


}
