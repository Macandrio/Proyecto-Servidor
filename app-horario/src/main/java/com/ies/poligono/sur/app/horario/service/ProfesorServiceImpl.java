package com.ies.poligono.sur.app.horario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.dao.ProfesorRepository;
import com.ies.poligono.sur.app.horario.model.Profesor;

@Service
public class ProfesorServiceImpl implements ProfesorService {

	@Autowired
	ProfesorRepository profesorRepository;

	@Override
	public Profesor findByNombre(String nombre) {
		return profesorRepository.findByNombre(nombre);
	}
	
	public List<Profesor> buscarPorNombreParcial(String nombre) {
	    return profesorRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public Profesor findById(Long id) {
		return profesorRepository.findById(id).orElse(null);
	}
	
	@Override
    public List<Profesor> obtenerTodos() {
        return profesorRepository.findAll();
    }
	
	@Override
	public Profesor guardar(Profesor profesor) {
	    return profesorRepository.save(profesor);
	}

}
