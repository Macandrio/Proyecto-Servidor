package com.ies.poligono.sur.app.horario.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.apputils.AppHorarioUtils;
import com.ies.poligono.sur.app.horario.dao.AusenciaRepository;
import com.ies.poligono.sur.app.horario.dao.HorarioRepository;
import com.ies.poligono.sur.app.horario.dto.AusenciaAgrupadaDTO;
import com.ies.poligono.sur.app.horario.dto.AusenteGuardiaDTO;
import com.ies.poligono.sur.app.horario.dto.PostAusenciasInputDTO;
import com.ies.poligono.sur.app.horario.model.Ausencia;
import com.ies.poligono.sur.app.horario.model.Horario;

@Service
public class AusenciaServiceImpl implements AusenciaService {

	@Autowired
	private HorarioRepository horarioRepository;

	@Autowired
	private AusenciaRepository ausenciaRepository;

	// --------------------------------------------------------------------------
	// MÉTODO: crearAusencia
	// Descripción: Crea nuevas ausencias para un profesor en un rango horario
	// determinado. Comprueba si ya existen ausencias para esos horarios y evita
	// duplicados.
	// --------------------------------------------------------------------------
	@Override
	public void crearAusencia(PostAusenciasInputDTO dto, Long idProfesor) {
		if (dto.getFecha().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("No se puede registrar una ausencia en el pasado.");
		}

		LocalTime horaInicio = dto.getHoraInicio() != null ? dto.getHoraInicio() : LocalTime.of(8, 0);
		LocalTime horaFin = dto.getHoraFin() != null ? dto.getHoraFin() : LocalTime.of(14, 0);

		if (horaInicio.isAfter(horaFin)) {
			throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la de fin.");
		}

		String diaAbrev = AppHorarioUtils.obtenerDiaSemanaAbrevByFecha(dto.getFecha());

		// Buscar todos los horarios que tiene el profesor ese día y en ese rango
		// horario
		List<Horario> horarios = horarioRepository.findHorariosEntreHoras(idProfesor, diaAbrev, horaInicio, horaFin);

		if (horarios.isEmpty()) {
			throw new IllegalArgumentException("No tienes clases asignadas en ese tramo horario.");
		}

		// Obtener ausencias ya registradas de ese profesor ese mismo día
		List<Ausencia> ausenciasDelProfesorEseDia = ausenciaRepository
				.findByFechaAndHorario_Profesor_IdProfesor(dto.getFecha(), idProfesor);

		int creadas = 0;

		for (Horario h : horarios) {
			// Verifica si ya existe una ausencia para ese horario
			boolean yaExiste = ausenciasDelProfesorEseDia.stream()
					.anyMatch(a -> a.getHorario().getId().equals(h.getId()));

			// Si no existe, se crea
			if (!yaExiste) {
				Ausencia a = new Ausencia();
				a.setHorario(h);
				a.setDescripcion(dto.getMotivo());
				a.setFecha(dto.getFecha());
				ausenciaRepository.save(a);
				creadas++;
			}
		}

		if (creadas == 0) {
			throw new IllegalArgumentException("Esa ausencia ya existe.");
		}

	}

	@Override
	public void crearAusenciaV2(PostAusenciasInputDTO dto, Long idProfesor) {

		if (dto.getFecha().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("No se puede registrar una ausencia en el pasado.");
		}

		if (dto.getHoraInicio() != null && dto.getHoraFin() != null && dto.getHoraInicio().isAfter(dto.getHoraFin())) {
			throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la de fin.");
		}

		String diaAbrev = AppHorarioUtils.obtenerDiaSemanaAbrevByFecha(dto.getFecha());
		LocalTime horaInicio = dto.getHoraInicio() != null ? dto.getHoraInicio() : LocalTime.MIN;
		LocalTime horaFin = dto.getHoraFin() != null ? dto.getHoraFin() : LocalTime.of(23, 59, 59);
		// Buscar todos los horarios que tiene el profesor ese día y en ese rango
		// horario
		List<Horario> horarios = horarioRepository.findHorariosSolapados(idProfesor, diaAbrev, horaInicio, horaFin);


		if (horarios.isEmpty()) {
			throw new IllegalArgumentException("No tienes clases asignadas en ese tramo horario.");
		}

		// Obtener ausencias ya registradas de ese profesor ese mismo día
		List<Ausencia> lstAusenciasProfFecha = ausenciaRepository
				.findByFechaAndHorario_Profesor_IdProfesor(dto.getFecha(), idProfesor);

		List<Long> lstIdHorarioConAusenciaExistente = lstAusenciasProfFecha.stream().map(a -> a.getHorario().getId())
				.toList();

		List<Horario> lstHorarioCrearAusencia = horarios.stream()
				.filter(h -> !lstIdHorarioConAusenciaExistente.contains(h.getId())).toList();

		for (Horario horario : lstHorarioCrearAusencia) {
			Ausencia a = new Ausencia();
			a.setHorario(horario);
			a.setDescripcion(dto.getMotivo());
			a.setFecha(dto.getFecha());
			ausenciaRepository.save(a);
		}

	}

	// --------------------------------------------------------------------------
	// MÉTODO: eliminarAusenciaPorId
	// Descripción: Elimina una ausencia concreta por su ID
	// --------------------------------------------------------------------------
	@Override
	public void eliminarAusenciaPorId(Long id) {
		if (!ausenciaRepository.existsById(id)) {
			throw new IllegalArgumentException("No se encontró ninguna ausencia con el ID: " + id);
		}

		ausenciaRepository.deleteById(id);
	}

	@Override
	public List<AusenciaAgrupadaDTO> obtenerAusenciasAgrupadas(Long idProfesor) {

		// Consulta al repositorio
		List<Ausencia> lstAusencias = ausenciaRepository
				.findByHorarioProfesorIdProfesorOrderByHorarioDiaAscHorarioFranjaIdFranjaAsc(idProfesor);

		// Agrupar por fecha
		Map<LocalDate, List<Ausencia>> mapAusenciasPorFecha = lstAusencias.stream()
				.collect(Collectors.groupingBy(Ausencia::getFecha));

		List<AusenciaAgrupadaDTO> ausenciasAgrupadas = mapAusenciasPorFecha.entrySet().stream()
				.map(entry -> new AusenciaAgrupadaDTO(entry.getKey(), entry.getValue())).toList();

		return ausenciasAgrupadas;
	}

	// --------------------------------------------------------------------------
	// MÉTODO: eliminarAusenciasPorFechaYProfesor
	// Descripción: Borra todas las ausencias de un profesor en una fecha concreta
	// --------------------------------------------------------------------------
	@Override
	public void eliminarAusenciasPorFechaYProfesor(LocalDate fecha, Long idProfesor) {
		List<Ausencia> ausencias = ausenciaRepository.findByFechaAndHorario_Profesor_IdProfesor(fecha, idProfesor);
		ausenciaRepository.deleteAll(ausencias);
	}

	@Override
	public void justificarAusenciasPorDia(LocalDate fecha, Long idProfesor) {
		List<Ausencia> ausencias = ausenciaRepository.findByFechaAndHorario_Profesor_idProfesor(fecha, idProfesor);
		for (Ausencia a : ausencias) {
			if (!a.isJustificada()) {
				a.setJustificada(true);
			}
		}
		ausenciaRepository.saveAll(ausencias);
	}

	@Override
	public void borrarTodasLasAusencias() {
		ausenciaRepository.deleteAll();
	}

	@Override
	public List<AusenteGuardiaDTO> findProfesoresAusentes(LocalDate fecha, Long idFranja) {
		return ausenciaRepository.findProfesoresAusentes(fecha, idFranja);
	}

}