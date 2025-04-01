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
	
	@Override
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario); // Guarda el usuario en la base de datos
    }
	
	@Override
    public void eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id); // Elimina el usuario por ID si existe
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id); // Lanza un error si no existe
        }
    }


}
