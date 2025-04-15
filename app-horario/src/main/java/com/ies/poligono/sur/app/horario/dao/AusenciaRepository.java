package com.ies.poligono.sur.app.horario.dao;

import com.ies.poligono.sur.app.horario.model.Ausencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AusenciaRepository extends JpaRepository<Ausencia, Long> {

	List<Ausencia> findByHorarioProfesorIdOrderByHorarioDiaAscHorarioFranjaIdFranjaAsc(Long idProfesor);

	List<Ausencia> findByFechaAndHorario_Profesor_Id(LocalDate fecha, Long idProfesor);


}
