package com.ies.poligono.sur.app.horario.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteAusenciaInputDTO {

    private Long id;  // ID de la ausencia individual (opcional)

    private LocalDate fecha; // Fecha para eliminar todas las ausencias de ese día (opcional)

    private Long idProfesor; // Solo lo usará el admin, igual que en POST
}
