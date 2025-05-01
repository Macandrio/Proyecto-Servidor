package com.ies.poligono.sur.app.horario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.dao.HorarioRepository;
import com.ies.poligono.sur.app.horario.dto.HorarioDetalleDTO;
import com.ies.poligono.sur.app.horario.model.Horario;

@Service
public class HorarioServiceImpl implements HorarioService {

	@Autowired
	HorarioRepository horarioRepository;

	@Override
	public Horario crearHorario(Horario horario) {
		return horarioRepository.save(horario);
	}

	@Override
	public void borrarTodosLosHorarios() {
		horarioRepository.deleteAll();
	}
	
	@Override
	public List<HorarioDetalleDTO> obtenerHorarioPorProfesor(Long idProfesor) {
	    List<Horario> horarios = horarioRepository.findByProfesor_IdProfesor(idProfesor);

	    return horarios.stream()
	        .map(h -> new HorarioDetalleDTO(
	            h.getFranja(),
	            h.getDia(),
	            h.getAsignatura(),
	            h.getAula(),
	            h.getCurso()
	        ))
	        .collect(Collectors.toList());
	}



}
