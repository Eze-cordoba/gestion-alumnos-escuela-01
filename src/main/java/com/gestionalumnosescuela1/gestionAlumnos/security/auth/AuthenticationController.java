package com.gestionalumnosescuela1.gestionAlumnos.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(  @RequestBody RegisterRequest request) {
	  
	  Map<String, Object> response = new HashMap<>();
	AuthenticationResponse authenticationResponse = service.register(request);
	
	if (authenticationResponse.getError() != null ) {
		  response.put("error",authenticationResponse.getError());
		  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	} else {
		   return ResponseEntity.ok(authenticationResponse);
	}
	  
  }
  
  
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate( @RequestBody AuthenticationRequest request ) {
	  
	  AuthenticationResponse authenticationResponse = service.authenticate(request);
	    
	    if (authenticationResponse == null) {
	        // Autenticación fallida, devuelve una respuesta de error
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } else {
	        // Autenticación exitosa, devuelve la respuesta con el token
	        return ResponseEntity.ok(authenticationResponse);
	    }
	    
	}

  

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
