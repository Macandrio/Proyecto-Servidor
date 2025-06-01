package com.ies.poligono.sur.app.horario.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuadranteGuardiasDTO {

	List<FranjaGuardiaDTO> lstFranjaGuardias;

}
