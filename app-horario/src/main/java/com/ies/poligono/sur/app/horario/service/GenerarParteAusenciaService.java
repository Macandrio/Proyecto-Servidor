package com.ies.poligono.sur.app.horario.service;

import java.time.LocalDate;

import com.ies.poligono.sur.app.horario.dto.CuadranteGuardiasDTO;

public interface GenerarParteAusenciaService {

	String generarParte(LocalDate fecha);

	CuadranteGuardiasDTO obtenerInformacionCuadranteGuardia(LocalDate fecha);

}
