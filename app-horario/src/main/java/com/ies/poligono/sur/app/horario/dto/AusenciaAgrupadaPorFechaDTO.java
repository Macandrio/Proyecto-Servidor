package com.ies.poligono.sur.app.horario.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AusenciaAgrupadaPorFechaDTO {
    private LocalDate fecha;
    private List<AusenciaTramoDTO> tramos;
}
