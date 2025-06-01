package com.ies.poligono.sur.app.horario.apputils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppHorarioUtils {

	/**
	 * Devuelve el día abreviado (L, M, X, J, V) a partir de una fecha.
	 * 
	 * @param fecha Fecha completa
	 * @return Día abreviado
	 */
	public static String obtenerDiaSemanaAbrevByFecha(LocalDate fecha) {
		return switch (fecha.getDayOfWeek()) {
		case MONDAY -> "L";
		case TUESDAY -> "M";
		case WEDNESDAY -> "X";
		case THURSDAY -> "J";
		case FRIDAY -> "V";
		default -> throw new IllegalArgumentException("El día debe estar entre lunes y viernes");
		};
	}

	/**
	 * Devuelve el día abreviado (L, M, X, J, V) a partir de una fecha.
	 * 
	 * @param fecha Fecha completa
	 * @return Día abreviado
	 */
	public static String obtenerDiaSemanaByFecha(LocalDate fecha) {
		return switch (fecha.getDayOfWeek()) {
		case MONDAY -> "Lunes";
		case TUESDAY -> "Martes";
		case WEDNESDAY -> "Miércoles";
		case THURSDAY -> "Jueves";
		case FRIDAY -> "Viernes";
		default -> throw new IllegalArgumentException("El día debe estar entre lunes y viernes");
		};
	}

	/**
	 * Devuelve la fecha con formato dd-MM-YYYY
	 * 
	 * @param fecha
	 * @return
	 */
	public static String formatearFecha(LocalDate fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return fecha.format(formatter);
	}
}
