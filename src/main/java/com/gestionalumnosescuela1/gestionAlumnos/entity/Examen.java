package com.gestionalumnosescuela1.gestionAlumnos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter///getter and setter
@Builder//no hace falta el @autowired
@AllArgsConstructor 
@NoArgsConstructor
@Entity
@Table(name = "examen")
public class Examen implements Serializable  {

	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	   
	   @OneToOne
	    @JoinColumn(name = "materia_id")
	    private Materia materia;  //cuando llamo a materia materia va a llamar a alumnos y bucle. 

	    private String titulo;

	    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
	    private List<Pregunta> preguntas ;

	   
	     
	    
	
	     
	 //   @Temporal(TemporalType.DATE)
	   // private Date fecha;

	//@Override
	 //public String toString() {
       // return "Nombre:"  +"- Edad:" ;
   // }
	    
	    
}
