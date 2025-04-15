package com.ies.poligono.sur.app.horario.service;

import java.time.LocalDate;
import java.util.List;

import com.ies.poligono.sur.app.horario.dto.AusenciaAgrupadaPorFechaDTO;
import com.ies.poligono.sur.app.horario.dto.CrearAusenciaDTO;
import com.ies.poligono.sur.app.horario.model.Asignatura;

public interface AusenciaService {

	void crearAusencia(CrearAusenciaDTO dto, Long idProfesor);

	void eliminarAusenciaPorId(Long id);

	List<AusenciaAgrupadaPorFechaDTO> obtenerAusenciasAgrupadas(Long idProfesor);

	void eliminarAusenciasPorFechaYProfesor(LocalDate fecha, Long idProfesor);
	
}
