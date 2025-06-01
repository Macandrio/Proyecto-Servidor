package com.ies.poligono.sur.app.horario.service;

import java.time.LocalDate;
import java.util.List;

import com.ies.poligono.sur.app.horario.dto.AusenciaAgrupadaDTO;
import com.ies.poligono.sur.app.horario.dto.AusenteGuardiaDTO;
import com.ies.poligono.sur.app.horario.dto.PostAusenciasInputDTO;

public interface AusenciaService {

	void crearAusencia(PostAusenciasInputDTO dto, Long idProfesor);

	void eliminarAusenciaPorId(Long id);

	void eliminarAusenciasPorFechaYProfesor(LocalDate fecha, Long idProfesor);

	void crearAusenciaV2(PostAusenciasInputDTO dto, Long idProfesor);

	List<AusenciaAgrupadaDTO> obtenerAusenciasAgrupadas(Long idProfesor);

	void justificarAusenciasPorDia(LocalDate fecha, Long idProfesor);

	void borrarTodasLasAusencias();

	List<AusenteGuardiaDTO> findProfesoresAusentes(LocalDate fecha, Long idFranja);

}
