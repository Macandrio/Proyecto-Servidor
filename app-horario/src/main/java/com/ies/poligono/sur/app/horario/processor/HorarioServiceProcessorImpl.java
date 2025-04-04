package com.ies.poligono.sur.app.horario.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.dto.PostImportacionInputDTO;
import com.ies.poligono.sur.app.horario.model.Asignatura;
import com.ies.poligono.sur.app.horario.model.Aula;
import com.ies.poligono.sur.app.horario.model.Curso;
import com.ies.poligono.sur.app.horario.model.Horario;
import com.ies.poligono.sur.app.horario.model.Profesor;
import com.ies.poligono.sur.app.horario.service.AsignaturaService;
import com.ies.poligono.sur.app.horario.service.AulaService;
import com.ies.poligono.sur.app.horario.service.CursoService;
import com.ies.poligono.sur.app.horario.service.HorarioService;
import com.ies.poligono.sur.app.horario.service.ProfesorService;

@Service
public class HorarioServiceProcessorImpl implements HorarioServiceProcessor {

	@Autowired
	HorarioService horarioService;

	@Autowired
	AsignaturaService asignaturaService;

	@Autowired
	CursoService cursoService;

	@Autowired
	AulaService aulaService;

	@Autowired
	ProfesorService profesorService;

	@Override
	public void importarHorario(PostImportacionInputDTO inputDTO) {
		
		// decodificar el fichero
		byte[] decoded = Base64.getDecoder().decode(inputDTO.getFile());
		String decodedStr = new String(decoded, StandardCharsets.UTF_8);
		
		// leer el fichero línea a línea con la finalidad de obtener una List<Horario>
		insertarHorarioImportado(montarLstHorarioDesdeTxt(decodedStr));
	}

	/**
	 * Devuelva una lista de horarios dado el txt completo
	 * 
	 * @param txtHorario
	 * @return
	 */
	private List<Horario> montarLstHorarioDesdeTxt(String txtHorario) {
		List<Horario> lstHorario = new ArrayList<Horario>();
		
		// recorrer txt línea por línea y añadir un registro al array por cada línea
		try (BufferedReader reader = new BufferedReader(new StringReader(txtHorario))) {
			String txtFilaHorario;
			while ((txtFilaHorario = reader.readLine()) != null) {
				lstHorario.add(montarRegistroDesdeFilaTxt(txtFilaHorario));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lstHorario;
	}

	/**
	 * Crea un objeto horario dada la fila del txt
	 * 
	 * @param txtFilaHorario
	 * @return
	 */
	private Horario montarRegistroDesdeFilaTxt(String txtFilaHorario) {
		Horario horario = new Horario();
		String[] arrHorario = txtFilaHorario.split("\t");
		Asignatura asignatura = asignaturaService.findByNombre(arrHorario[0]);
		horario.setAsignatura(asignatura);
		Curso curso = cursoService.findByNombre(arrHorario[1]);
		horario.setCurso(curso);
		Aula aula = aulaService.findByCodigo(arrHorario[2]);
		horario.setAula(aula);
		Profesor profesor = profesorService.findByNombre(arrHorario[3]);
		horario.setProfesor(profesor);
		horario.setDia(arrHorario[4]);
		horario.setFranja(Integer.valueOf(arrHorario[5]));
		return horario;
	}

	/**
	 * Borra el horario anterior e inserta el nuevo
	 * 
	 * @param lstHorario
	 */
	private void insertarHorarioImportado(List<Horario> lstHorario) {
		// borrar todos los registros del horario anterior
		horarioService.borrarTodosLosHorarios();
		
		// iterar lista de horarios y hacer insert por cada registro
		for (Horario horario : lstHorario) {
			horarioService.crearHorario(horario);
		}
	}

}
