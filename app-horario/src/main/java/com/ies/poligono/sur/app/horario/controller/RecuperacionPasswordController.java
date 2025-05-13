package com.ies.poligono.sur.app.horario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.dto.PostRecuperacionPasswordInputDTO;
import com.ies.poligono.sur.app.horario.service.RecuperacionPasswordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recuperacion-password")
@RequiredArgsConstructor
public class RecuperacionPasswordController {

	@Autowired
	private RecuperacionPasswordService recuperacionPasswordService;

	@PostMapping
	public ResponseEntity<?> recuperarPassword(@RequestBody PostRecuperacionPasswordInputDTO dto) {
		recuperacionPasswordService.recuperarPassword(dto);
		return ResponseEntity.ok().build();
	}

}