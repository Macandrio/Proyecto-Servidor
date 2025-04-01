package com.ies.poligono.sur.app.horario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "USUARIO")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Esto asegura que el ID se genere automáticamente
	@Column(name = "ID")
	private Long id;

	@NotNull(message = "El nombre no puede ser nulo")
	@NotEmpty(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
	@Column(name = "NOMBRE")
	private String nombre;

	@NotNull(message = "El email no puede ser nulo")
	@NotEmpty(message = "El email no puede estar vacío")
	@Email(message = "El email debe tener un formato válido")
	@Column(name = "EMAIL")
	private String email;

	@NotNull(message = "La contraseña no puede ser nula")
	@NotEmpty(message = "La contraseña no puede estar vacía")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	@Column(name = "CONTRASEÑA")
	private String contraseña;

	@NotNull(message = "El rol no puede ser nulo")
	@NotEmpty(message = "El rol no puede estar vacío")
	@Pattern(regexp = "^(profesor|administrador)$", message = "El rol debe ser 'profesor' o 'administrador'")
	@Column(name = "ROL")
	private String rol;

	/*
	 * public Role getRole() { return Role.valueOf(rol.toUpperCase()); // Asignamos
	 * el rol en formato enum }
	 */

}
