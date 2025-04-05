package com.ies.poligono.sur.app.horario.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ies.poligono.sur.app.horario.model.Profesor;
import com.ies.poligono.sur.app.horario.model.Usuario;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

	Profesor findByNombre(String nombre);

	List<Profesor> findByNombreContainingIgnoreCase(String nombre);

	Profesor findByUsuario(Usuario usuario);

}