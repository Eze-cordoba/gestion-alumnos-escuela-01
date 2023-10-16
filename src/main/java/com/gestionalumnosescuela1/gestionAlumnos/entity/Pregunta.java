package com.gestionalumnosescuela1.gestionAlumnos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter///getter and setter
@Builder//no hace falta el @autowired
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table(name = "pregunta")
public class Pregunta  implements Serializable {

	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	
	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "examen_id")
	    private Examen examen;

	    private String enunciado;

	    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Respuesta> respuestas;
	
}
