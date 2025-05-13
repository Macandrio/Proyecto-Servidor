package com.ies.poligono.sur.app.horario.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ies.poligono.sur.app.horario.dao.UsuarioRepository;
import com.ies.poligono.sur.app.horario.dto.CambioContraseñaDTO;
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

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping
	public List<Usuario> obtenerUsuarios() {
		return usuarioService.obtenerUsuarios();
	}
	
	
	// Endpoint para crear un nuevo usuario
	@PostMapping("/crear-con-profesor/{idProfesor}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
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
	    profesorService.insertar(profesor);
	    
	    return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
	}

	
	// Endpoint para Eliminar un nuevo usuario
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content cuando la eliminación es exitosa
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id); // Llama al servicio para eliminar el usuario
    }
	
//	Endpoint para Actualizar un usuario
	@PutMapping("/{id_usuario}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> actualizarUsuario(
	        @PathVariable Long id_usuario, // Recibe el id_usuario en la URL
	        @Valid @RequestBody Usuario usuarioActualizado) {

	    try {
	        // Llamamos al servicio para actualizar el usuario
	        Usuario usuarioActualizadoDesdeServicio = usuarioService.actualizarUsuario(id_usuario, usuarioActualizado);

	        // Si todo es correcto, devolver el usuario actualizado
	        return ResponseEntity.ok(usuarioActualizadoDesdeServicio);

	    } catch (RuntimeException e) {
	        // Si el correo ya existe, devolver un error con el mensaje
	        return ResponseEntity.status(400).body(e.getMessage());
	    } catch (Exception e) {
	        // Para otros errores
	        return ResponseEntity.status(500).body("Error al actualizar el usuario");
	    }
    }

//	Endpoint para cambiar la contraseña al inicio por primera vez	
	@PutMapping("/{id}/cambiar-contraseña")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESOR')")
	public ResponseEntity<Usuario> cambiarContraseña(
	        @PathVariable Long id,
	        @RequestBody CambioContraseñaDTO dto,
	        Authentication authentication) {

	    Usuario usuarioLogueado = usuarioRepository.findByEmail(authentication.getName());

	    if (!usuarioLogueado.getId().equals(id)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }

	    Usuario actualizado = usuarioService.actualizarContraseña(id, dto.getNuevaContraseña(), false);
	    return ResponseEntity.ok(actualizado);
	}

//	Endpoint para Subir una imagen	
	@PostMapping("/{id}/imagen")
//	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESOR')")
	@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('PROFESOR')")
	public ResponseEntity<String> subirImagen(@PathVariable Long id, @RequestParam("imagen") MultipartFile archivo) {
	    try {
	        Usuario usuario = usuarioRepository.findById(id)
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

	        usuario.setImagen(archivo.getBytes());
	        usuarioRepository.save(usuario);

	        return ResponseEntity.ok("Imagen subida correctamente");
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen");
	    }
	}

//	Endpoint para Obtener imagen	
	@GetMapping("/{id}/imagen")
//	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFESOR')")
	@PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('PROFESOR')")
	public ResponseEntity<?> obtenerImagen(@PathVariable Long id) {
	    Usuario usuario = usuarioRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

	    if (usuario.getImagen() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este usuario no tiene imagen");
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.IMAGE_JPEG) // o IMAGE_PNG si usas PNG
	            .body(new ByteArrayResource(usuario.getImagen()));
	}


}
