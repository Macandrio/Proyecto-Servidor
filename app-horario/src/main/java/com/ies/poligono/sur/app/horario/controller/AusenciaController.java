package com.ies.poligono.sur.app.horario.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.dto.AusenciaAgrupadaDTO;
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
	public ResponseEntity<List<AusenciaAgrupadaDTO>> obtenerAusencias(Principal principal) {
		// TODO: quitar parámetro de entrada y usar el contextHolder
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Profesor profesor = profesorService.findByEmailUsuario(principal.getName());

		System.out.println("EMAIL desde token: " + principal.getName());
		System.out.println("Profesor encontrado: " + profesor.getNombre() + " - ID: " + profesor.getIdProfesor());

		List<AusenciaAgrupadaDTO> ausencias = ausenciaService
				.obtenerAusenciasAgrupadasV2(profesor.getIdProfesor());

		return ResponseEntity.ok(ausencias);
	}

	@DeleteMapping("{fecha}")
	@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('PROFESOR')")
	public ResponseEntity<Void> eliminarAusenciasPorFecha(@PathVariable LocalDate fecha, Principal principal) {

		System.out.println("→ Petición DELETE recibida para fecha: " + fecha);

		String email = principal.getName();
		System.out.println("→ Usuario autenticado con email: " + email);

		Long idProfesor = profesorService.obtenerIdProfesorPorUsername(email);
		System.out.println("→ ID del profesor: " + idProfesor);

		ausenciaService.eliminarAusenciasPorFechaYProfesor(fecha, idProfesor);

		return ResponseEntity.noContent().build();
	}

}