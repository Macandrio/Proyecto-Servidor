package com.ies.poligono.sur.app.horario.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ies.poligono.sur.app.horario.model.Profesor;
import com.ies.poligono.sur.app.horario.model.Usuario;
import com.ies.poligono.sur.app.horario.service.ProfesorService;
import com.ies.poligono.sur.app.horario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	private ProfesorService profesorService;

	
	@GetMapping
	public List<Usuario> obtenerUsuarios() {
		return usuarioService.obtenerUsuarios();
	}
	
	
	// Endpoint para crear un nuevo usuario
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping("/crear-con-profesor/{idProfesor}")
	public ResponseEntity<?> crearUsuarioYVincularAProfesor(
	        @PathVariable Long idProfesor,
	        @Valid @RequestBody Usuario usuario,
	        BindingResult result) {

	    if (result.hasErrors()) {
	        Map<String, String> errors = new HashMap<>();
	        result.getAllErrors().forEach(error -> {
	            String fieldName = ((org.springframework.validation.FieldError) error).getField();
	            String errorMessage = error.getDefaultMessage();
	            errors.put(fieldName, errorMessage);
	        });
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }

	    Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);

	    Profesor profesor = profesorService.findById(idProfesor);
	    if (profesor == null) {
	        return new ResponseEntity<>("Profesor no encontrado", HttpStatus.NOT_FOUND);
	    }

	    profesor.setUsuario(nuevoUsuario);
	    profesorService.guardar(profesor);
	    
	    return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
	}

	
	// Endpoint para Eliminar un nuevo usuario
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content cuando la eliminación es exitosa
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id); // Llama al servicio para eliminar el usuario
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Usuario> actualizarUsuario(
	        @PathVariable Long id,
	        @Valid @RequestBody Usuario usuarioActualizado,
	        BindingResult result) {

	    if (result.hasErrors()) {
	        Map<String, String> errores = new HashMap<>();
	        result.getFieldErrors().forEach(error ->
	            errores.put(error.getField(), error.getDefaultMessage())
	        );
	        return new ResponseEntity(errores, HttpStatus.BAD_REQUEST);
	    }

	    Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
	    return ResponseEntity.ok(actualizado);
	}


}
