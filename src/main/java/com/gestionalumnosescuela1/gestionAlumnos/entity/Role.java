package com.gestionalumnosescuela1.gestionAlumnos.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gestionalumnosescuela1.gestionAlumnos.entity.Permission.*;

@RequiredArgsConstructor
public enum Role {

	  USER(Collections.emptySet()),
	  PROFESOR(
	          Set.of(
	                  PROFESOR_READ,
					  PROFESOR_UPDATE,
					  PROFESOR_DELETE,
					  PROFESOR_CREATE,
	                  ALUMNO_READ,
					  ALUMNO_UPDATE,
					  ALUMNO_DELETE,
					  ALUMNO_CREATE
	          )
	  ),
	  ALUMNO(
	          Set.of(
					  ALUMNO_READ,
					  ALUMNO_UPDATE,
					  ALUMNO_DELETE,
					  ALUMNO_CREATE
	          )
	  )

	  ;

	  @Getter
	  private final Set<Permission> permissions;

	  public List<SimpleGrantedAuthority> getAuthorities() {
	    var authorities = getPermissions()
	            .stream()
	            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
	            .collect(Collectors.toList());
	    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
	    return authorities;
	  }
	}
	

