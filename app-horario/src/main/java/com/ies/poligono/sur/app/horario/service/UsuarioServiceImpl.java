package com.ies.poligono.sur.app.horario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.dao.UsuarioRepository;
import com.ies.poligono.sur.app.horario.model.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	public List<Usuario> obtenerUsuarios() {
		return usuarioRepository.findAll();
	}

}
