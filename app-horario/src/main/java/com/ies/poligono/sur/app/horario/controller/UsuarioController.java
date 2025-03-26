package com.ies.poligono.sur.app.horario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.model.Usuario;
import com.ies.poligono.sur.app.horario.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;

	@GetMapping
	public List<Usuario> obtenerUsuarios() {
		return usuarioService.obtenerUsuarios();
	}

}
