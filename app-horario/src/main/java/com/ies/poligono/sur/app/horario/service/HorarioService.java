package com.ies.poligono.sur.app.horario.service;

import java.util.List;

import com.ies.poligono.sur.app.horario.dto.HorarioDetalleDTO;
import com.ies.poligono.sur.app.horario.model.Horario;

public interface HorarioService {

	Horario crearHorario(Horario horario);

	void borrarTodosLosHorarios();
	
	List<HorarioDetalleDTO> obtenerHorarioPorProfesor(Long idProfesor);


}
