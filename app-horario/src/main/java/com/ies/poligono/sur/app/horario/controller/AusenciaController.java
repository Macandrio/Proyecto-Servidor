package com.ies.poligono.sur.app.horario.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.dto.AusenciaAgrupadaDTO;
import com.ies.poligono.sur.app.horario.dto.DeleteAusenciaInputDTO;
import com.ies.poligono.sur.app.horario.dto.PostAusenciasInputDTO;
import com.ies.poligono.sur.app.horario.model.Profesor;
import com.ies.poligono.sur.app.horario.service.AusenciaService;
import com.ies.poligono.sur.app.horario.service.ProfesorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ausencias")
@RequiredArgsConstructor
public class AusenciaController {

	@Autowired
	private AusenciaService ausenciaService;

	@Autowired
	private ProfesorService profesorService;

	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('PROFESOR')")
	public ResponseEntity<?> crearAusencia(@RequestBody PostAusenciasInputDTO dto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		Long idProfesor = null;
		if (roles.contains("ROLE_ADMINISTRADOR") && dto.getIdProfesor() != null) {
			// lógica para administrador
			idProfesor = dto.getIdProfesor();
		} else {
			// lógica para profesor o administrador que no informa profesor
			String email = auth.getName();
			Profesor profesor = profesorService.findByEmailUsuario(email);
			idProfesor = profesor.getIdProfesor();
		}

		ausenciaService.crearAusenciaV2(dto, idProfesor);

		return ResponseEntity.ok().build();
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('PROFESOR', 'ADMINISTRADOR')")
	public ResponseEntity<List<AusenciaAgrupadaDTO>> obtenerAusencias(
			@RequestParam(name = "idusuario", required = false) Long idUsuario,
		    Principal principal) {
		
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		
		Long idProfesor;
		
		// Si eres administrador y pasas un idUsuario, lo usas para buscar el profesor correspondiente
	    if (roles.contains("ROLE_ADMINISTRADOR") && idUsuario != null) {
	        idProfesor = profesorService.obtenerIdProfesorPorUsuario(idUsuario);
	    } else {
	        // Si eres profesor (o admin sin idUsuario), buscas por el usuario autenticado
	        String email = principal.getName();
	        Profesor profesor = profesorService.findByEmailUsuario(email);
	        idProfesor = profesor.getIdProfesor();
	    }

	    List<AusenciaAgrupadaDTO> ausencias = ausenciaService.obtenerAusenciasAgrupadasV2(idProfesor);
	    return ResponseEntity.ok(ausencias);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('PROFESOR')")
	public ResponseEntity<?> eliminarAusencia(@RequestBody DeleteAusenciaInputDTO dto) {

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Set<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

	    Long idProfesor = null;
	    
//	    Decimos si la ausencia eliminada es para nosotros o para otro profesor
	    if (roles.contains("ROLE_ADMINISTRADOR") && dto.getIdProfesor() != null) {
	        idProfesor = dto.getIdProfesor();
	    } else {
	        String email = auth.getName();
	        idProfesor = profesorService.obtenerIdProfesorPorUsername(email);
	    }

//	    si la ausencia es un tramo 
	    if (dto.getId() != null) {
	        ausenciaService.eliminarAusenciaPorId(dto.getId());
	        return ResponseEntity.noContent().build();
	    }

//	    Si la ausencia es una dia completo
	    if (dto.getFecha() != null) {
	        ausenciaService.eliminarAusenciasPorFechaYProfesor(dto.getFecha(), idProfesor);
	        return ResponseEntity.noContent().build();
	    }

	    return ResponseEntity.badRequest().body("Debes proporcionar un ID o una fecha.");
	}


	@PatchMapping("/justificar-dia")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> justificarAusenciasPorDia(@RequestBody Map<String, String> datos) {
	    String fechaStr = datos.get("fecha");
	    Long idProfesor = Long.parseLong(datos.get("idProfesor"));
	    LocalDate fecha = LocalDate.parse(fechaStr);

	    ausenciaService.justificarAusenciasPorDia(fecha, idProfesor);
	    return ResponseEntity.ok().build();
	}

}