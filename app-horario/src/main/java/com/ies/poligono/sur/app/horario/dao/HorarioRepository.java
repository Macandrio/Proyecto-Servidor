package com.ies.poligono.sur.app.horario.dao;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ies.poligono.sur.app.horario.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

	
	@Query("""
	        SELECT h FROM Horario h
	        JOIN h.franja f
	        WHERE h.profesor.id = :idProfesor
	        AND h.dia = :dia
	        AND f.horaInicio >= :horaInicio
	        AND f.horaInicio < :horaFin
	    """)
	    List<Horario> findHorariosEntreHoras(
	        Long idProfesor,
	        String dia,
	        LocalTime horaInicio,
	        LocalTime horaFin
	    );
	
	List<Horario> findByProfesor_IdProfesor(Long idProfesor);

	@Query("SELECT h FROM Horario h WHERE h.profesor.idProfesor = :idProfesor AND h.dia = :dia AND "
		     + "((h.franja.horaInicio <= :horaFin) AND (h.franja.horaFin >= :horaInicio))")
		List<Horario> findHorariosSolapados(@Param("idProfesor") Long idProfesor,
		                                     @Param("dia") String dia,
		                                     @Param("horaInicio") LocalTime horaInicio,
		                                     @Param("horaFin") LocalTime horaFin);


	
}
