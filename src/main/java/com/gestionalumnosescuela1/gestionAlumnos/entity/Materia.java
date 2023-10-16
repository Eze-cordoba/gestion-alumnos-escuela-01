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
@Table(name = "materia")
public class Materia implements Serializable  {
	 

		  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@Id
		  @GeneratedValue(strategy = GenerationType.IDENTITY)
			private Long id;
			
			private String name;

			
			@ManyToMany(mappedBy ="materias",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
			private List<Usuario>  alumnos ; //LISTA DE MANAGERS (ALUMNOS) 
 

			//PROFESOR QUE LA DICTA 
			private String TeacherName;
			
			  
			  @OneToOne(mappedBy = "materia",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
			    private Examen examen;
			
			  
			  @OneToMany(mappedBy = "materia")
			    private List<Nota> notas;
			  
		}
