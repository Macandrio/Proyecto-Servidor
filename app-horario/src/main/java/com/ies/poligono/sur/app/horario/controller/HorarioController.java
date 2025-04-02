package com.ies.poligono.sur.app.horario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.dto.PostImportacionInputDTO;
import com.ies.poligono.sur.app.horario.processor.HorarioServiceProcessor;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

	@Autowired
	HorarioServiceProcessor horarioServiceProcessor;

	// Endpoint para subir el archivo
	@PostMapping("/importacion")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> importacion(@RequestBody PostImportacionInputDTO inputDTO) {
		horarioServiceProcessor.importarHorario(inputDTO);
		return new ResponseEntity<>(new Object(), HttpStatus.CREATED);
	}

}
