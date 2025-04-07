package com.ies.poligono.sur.app.horario.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ies.poligono.sur.app.horario.dao.ProfesorRepository;
import com.ies.poligono.sur.app.horario.dao.UsuarioRepository;
import com.ies.poligono.sur.app.horario.model.Profesor;
import com.ies.poligono.sur.app.horario.model.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    ProfesorRepository profesorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        
    	if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
    	
        String contraseñaEncriptada = passwordEncoder.encode(usuario.getContraseña());
        usuario.setContraseña(contraseñaEncriptada);
        
        return usuarioRepository.save(usuario);
    }

    
    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id
            ));

        // Buscar si hay un profesor asociado a este usuario
        Profesor profesor = profesorRepository.findByUsuario(usuario);
        if (profesor != null) {
            // Romper la relación antes de borrar al usuario
            profesor.setUsuario(null);
            profesorRepository.save(profesor); // guarda el cambio en la BD
        }

        // Ahora que está libre, eliminamos el usuario
        usuarioRepository.delete(usuario);
    }

    
    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setRol(usuarioActualizado.getRol());

        // Solo actualiza contraseña si se ha enviado una nueva
        if (usuarioActualizado.getContraseña() != null && !usuarioActualizado.getContraseña().isBlank()) {
            String nuevaPass = passwordEncoder.encode(usuarioActualizado.getContraseña());
            usuarioExistente.setContraseña(nuevaPass);
        }

        return usuarioRepository.save(usuarioExistente);
    }

}
