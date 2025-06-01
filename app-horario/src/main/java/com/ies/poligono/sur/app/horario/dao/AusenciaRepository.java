package com.ies.poligono.sur.app.horario.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ies.poligono.sur.app.horario.dto.AusenteGuardiaDTO;
import com.ies.poligono.sur.app.horario.model.Ausencia;
import com.ies.poligono.sur.app.horario.model.Horario;

@Repository
public interface AusenciaRepository extends JpaRepository<Ausencia, Long> {

	List<Ausencia> findByHorarioProfesorIdProfesorOrderByHorarioDiaAscHorarioFranjaIdFranjaAsc(Long idProfesor);

	List<Ausencia> findByFechaAndHorario_Profesor_IdProfesor(LocalDate fecha, Long idProfesor);

	boolean existsByFechaAndHorario(LocalDate fecha, Horario horario);

	List<Ausencia> findByFechaAndHorario_Profesor_idProfesor(LocalDate fecha, Long idProfesor);

	@Query("""
			SELECT new com.ies.poligono.sur.app.horario.dto.AusenteGuardiaDTO (a.horario.profesor.nombre, a.horario.curso.nombre, a.horario.aula.codigo)
			FROM Ausencia a
			WHERE a.fecha = :fecha
			AND a.horario.franja.idFranja = :idFranja
			""")
	List<AusenteGuardiaDTO> findProfesoresAusentes(LocalDate fecha, Long idFranja);
	
	
}
