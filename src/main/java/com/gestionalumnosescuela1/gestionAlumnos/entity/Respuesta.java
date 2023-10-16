package com.gestionalumnosescuela1.gestionAlumnos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter///getter and setter
@Builder//no hace falta el @autowired
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table(name = "respuesta")
public class Respuesta implements Serializable  {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    private String contenido;

    private boolean esCorrecta;
	
	
}
