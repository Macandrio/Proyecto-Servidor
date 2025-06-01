package com.ies.poligono.sur.app.horario.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.service.GenerarParteAusenciaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parte-ausencias")
@RequiredArgsConstructor
public class ParteAusenciasController {

	@Autowired
	private GenerarParteAusenciaService generarParteAusenciaService;

	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> obtenerParteAusencias() {
		return ResponseEntity.ok(generarParteAusenciaService.generarParte(LocalDate.now()));
	}

}