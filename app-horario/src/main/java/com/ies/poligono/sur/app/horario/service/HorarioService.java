package com.ies.poligono.sur.app.horario.service;

import com.ies.poligono.sur.app.horario.model.Horario;

public interface HorarioService {

	Horario crearHorario(Horario horario);

	void borrarTodosLosHorarios();

}
