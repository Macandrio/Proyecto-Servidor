package com.ies.poligono.sur.app.horario.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.dto.HorarioDetalleDTO;
import com.ies.poligono.sur.app.horario.dto.PostImportacionInputDTO;
import com.ies.poligono.sur.app.horario.model.Profesor;
import com.ies.poligono.sur.app.horario.processor.HorarioServiceProcessor;
import com.ies.poligono.sur.app.horario.service.HorarioService;
import com.ies.poligono.sur.app.horario.service.ProfesorService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

	@Autowired
	HorarioServiceProcessor horarioServiceProcessor;

	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private HorarioService horarioService;


	// Endpoint para subir el archivo
	@PostMapping("/importacion")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> importacion(@RequestBody PostImportacionInputDTO inputDTO) {
		horarioServiceProcessor.importarHorario(inputDTO);
		return new ResponseEntity<>(new Object(), HttpStatus.CREATED);
	}
	
	
	// Endpoint para Mostrar Horario profesor
	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('PROFESOR')")
	public ResponseEntity<List<HorarioDetalleDTO>> obtenerHorario(
	        @RequestParam(value = "idProfesor", required = false) Long idProfesor) {

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Set<String> roles = auth.getAuthorities().stream()
	            .map(r -> r.getAuthority())
	            .collect(Collectors.toSet());

	    if (roles.contains("ROLE_ADMINISTRADOR") && idProfesor != null) {
	        // Admin que pasa idProfesor explícito
	    	List<HorarioDetalleDTO> horarios = horarioService.obtenerHorarioPorProfesor(idProfesor);
	        return ResponseEntity.ok(horarios);
	    } else {
	        // Profesor o admin sin id → sacar del token
	        String email = auth.getName(); // del token JWT
	        Profesor profesor = profesorService.findByEmailUsuario(email);
	        idProfesor = profesor.getIdProfesor();
	    }

	    List<HorarioDetalleDTO> horarios = horarioService.obtenerHorarioPorProfesor(idProfesor);
	    return ResponseEntity.ok(horarios);
	}


}
