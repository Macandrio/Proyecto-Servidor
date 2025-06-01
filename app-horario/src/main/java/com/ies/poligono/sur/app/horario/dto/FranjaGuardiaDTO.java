package com.ies.poligono.sur.app.horario.dto;

import java.util.List;

import com.ies.poligono.sur.app.horario.model.Franja;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranjaGuardiaDTO {

	Franja franjaHoraria;

	List<String> lstProfesoresGuardia;

	List<AusenteGuardiaDTO> lstAusentesGuardia;

}
