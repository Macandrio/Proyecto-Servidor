package com.ies.poligono.sur.app.horario.dto;

import com.ies.poligono.sur.app.horario.model.Asignatura;
import com.ies.poligono.sur.app.horario.model.Aula;
import com.ies.poligono.sur.app.horario.model.Curso;
import com.ies.poligono.sur.app.horario.model.Franja;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HorarioDetalleDTO {
    private Franja franja;
    private String dia;
    private Asignatura asignatura;
    private Aula aula;
    private Curso curso;
}
