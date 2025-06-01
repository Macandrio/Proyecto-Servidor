package com.ies.poligono.sur.app.horario.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ies.poligono.sur.app.horario.model.Asignatura;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

	Asignatura findByNombre(String nombre);

}