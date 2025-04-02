package com.ies.poligono.sur.app.horario.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ies.poligono.sur.app.horario.model.Profesor;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

	Profesor findByNombre(String nombre);

}