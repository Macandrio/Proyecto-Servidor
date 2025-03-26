package com.ies.poligono.sur.app.horario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USUARIO")
@Data
public class Usuario {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "EMAIL")
	private String email;

}
