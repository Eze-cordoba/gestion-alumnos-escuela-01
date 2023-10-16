package com.gestionalumnosescuela1.gestionAlumnos.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter///getter and setter
@Builder//no hace falta el @autowired
@AllArgsConstructor 
@NoArgsConstructor
@Entity
@Table(name = "nota")
public class Nota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	
       @ManyToOne
	    @JoinColumn(name = "alumno_id")
	private Usuario alumno ;
	
	
	private char nota ;
	
	
    @ManyToOne
    @JoinColumn(name = "materia_id")
    private Materia materia;

	
}
