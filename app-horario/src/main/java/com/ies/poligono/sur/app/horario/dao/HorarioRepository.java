package com.ies.poligono.sur.app.horario.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ies.poligono.sur.app.horario.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

}
