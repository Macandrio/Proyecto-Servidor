package com.ies.poligono.sur.app.horario.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.dao.AusenciaRepository;
import com.ies.poligono.sur.app.horario.dao.FranjaRepository;
import com.ies.poligono.sur.app.horario.dao.HorarioRepository;
import com.ies.poligono.sur.app.horario.dto.AusenciaAgrupadaPorFechaDTO;
import com.ies.poligono.sur.app.horario.dto.AusenciaTramoDTO;
import com.ies.poligono.sur.app.horario.dto.CrearAusenciaDTO;
import com.ies.poligono.sur.app.horario.model.Ausencia;
import com.ies.poligono.sur.app.horario.model.Horario;

@Service
public class AusenciaServiceImpl implements AusenciaService {

	@Autowired
    private HorarioRepository horarioRepository;
		
	@Autowired
	private AusenciaRepository ausenciaRepository;
	
	@Autowired
	private FranjaRepository franjaRepository;

    @Override
    public void crearAusencia(CrearAusenciaDTO dto, Long idProfesor) {
        // Validaciones lógicas
        if (dto.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede registrar una ausencia en el pasado.");
        }

        LocalTime horaInicio = dto.getHoraInicio() != null ? dto.getHoraInicio() : LocalTime.of(8, 0);
        LocalTime horaFin = dto.getHoraFin() != null ? dto.getHoraFin() : LocalTime.of(14, 0);
        

        if (horaInicio.isAfter(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la de fin.");
        }

        

        String diaAbrev = obtenerDiaAbreviado(dto.getFecha());

        List<Horario> horarios = horarioRepository.findHorariosEntreHoras(
            idProfesor, diaAbrev, horaInicio, horaFin
        );

        for (Horario h : horarios) {
            Ausencia a = new Ausencia();
            a.setHorario(h);
            a.setDescripcion(dto.getMotivo());
            a.setFecha(dto.getFecha()); 
            ausenciaRepository.save(a);
        }
    }

    private String obtenerDiaAbreviado(LocalDate fecha) {
        return switch (fecha.getDayOfWeek()) {
            case MONDAY -> "L";
            case TUESDAY -> "M";
            case WEDNESDAY -> "X";
            case THURSDAY -> "J";
            case FRIDAY -> "V";
            default -> throw new IllegalArgumentException("El día debe estar entre lunes y viernes");
        };
    }
    
    @Override
    public void eliminarAusenciaPorId(Long id) {
        if (!ausenciaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró ninguna ausencia con el ID: " + id);
        }

        ausenciaRepository.deleteById(id);
    }

    @Override
    public List<AusenciaAgrupadaPorFechaDTO> obtenerAusenciasAgrupadas(Long idProfesor) {

        // 🔍 DEBUG: Mostrar el ID recibido
        System.out.println("Buscando ausencias para profesor ID: " + idProfesor);

        // Consulta al repositorio
        List<Ausencia> ausencias = ausenciaRepository.findByHorarioProfesorIdOrderByHorarioDiaAscHorarioFranjaIdFranjaAsc(idProfesor);

        // 🔍 DEBUG: Ver cuántas ausencias devuelve la consulta
        System.out.println("Ausencias encontradas: " + ausencias.size());

        // Agrupar por fecha
        Map<LocalDate, List<Ausencia>> agrupadasPorFecha = ausencias.stream().collect(
            Collectors.groupingBy(Ausencia::getFecha)
        );

        List<AusenciaAgrupadaPorFechaDTO> resultado = new ArrayList<>();

        for (Entry<LocalDate, List<Ausencia>> entry : agrupadasPorFecha.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Ausencia> delDia = entry.getValue();

            // Ordenar por franja
            delDia.sort(Comparator.comparingInt(a -> a.getHorario().getFranja().getIdFranja().intValue()));

            List<AusenciaTramoDTO> tramos = agruparEnTramosConsecutivos(delDia);
            resultado.add(new AusenciaAgrupadaPorFechaDTO(fecha, tramos));
        }

        return resultado;
    }

    private List<AusenciaTramoDTO> agruparEnTramosConsecutivos(List<Ausencia> ausencias) {
        List<AusenciaTramoDTO> resultado = new ArrayList<>();
        if (ausencias.isEmpty()) return resultado;

        Ausencia actual = ausencias.get(0);
        int franjaInicio = actual.getHorario().getFranja().getIdFranja().intValue();
        int franjaFin = franjaInicio;

        List<String> asignaturas = new ArrayList<>();
        List<String> aulas = new ArrayList<>();
        List<String> cursos = new ArrayList<>();
        String motivo = actual.getDescripcion();

        asignaturas.add(actual.getHorario().getAsignatura().getNombre());
        aulas.add(actual.getHorario().getAula().getCodigo());
        cursos.add(actual.getHorario().getCurso().getNombre());

        for (int i = 1; i < ausencias.size(); i++) {
            Ausencia siguiente = ausencias.get(i);
            int franjaSiguiente = siguiente.getHorario().getFranja().getIdFranja().intValue();

            if (franjaSiguiente == franjaFin + 1 && siguiente.getDescripcion().equals(motivo)) {
                franjaFin = franjaSiguiente;
                asignaturas.add(siguiente.getHorario().getAsignatura().getNombre());
                aulas.add(siguiente.getHorario().getAula().getCodigo());
                cursos.add(siguiente.getHorario().getCurso().getNombre());
            } else {
                resultado.add(crearTramo(franjaInicio, franjaFin, asignaturas, aulas, cursos, motivo));
                franjaInicio = franjaSiguiente;
                franjaFin = franjaSiguiente;
                asignaturas = new ArrayList<>(List.of(siguiente.getHorario().getAsignatura().getNombre()));
                aulas = new ArrayList<>(List.of(siguiente.getHorario().getAula().getCodigo()));
                cursos = new ArrayList<>(List.of(siguiente.getHorario().getCurso().getNombre()));
                motivo = siguiente.getDescripcion();
            }
        }

        resultado.add(crearTramo(franjaInicio, franjaFin, asignaturas, aulas, cursos, motivo));
        return resultado;
    }
    
    private AusenciaTramoDTO crearTramo(int franjaInicio, int franjaFin, List<String> asignaturas, List<String> aulas, List<String> cursos, String motivo) {
        LocalTime horaInicio = franjaRepository.findById((long) franjaInicio).get().getHoraInicio();
        LocalTime horaFin = franjaRepository.findById((long) franjaFin).get().getHoraFin();

        return new AusenciaTramoDTO(horaInicio, horaFin, asignaturas, aulas, cursos, motivo);
    }

    @Override
    public void eliminarAusenciasPorFechaYProfesor(LocalDate fecha, Long idProfesor) {
        List<Ausencia> ausencias = ausenciaRepository.findByFechaAndHorario_Profesor_Id(fecha, idProfesor);
        ausenciaRepository.deleteAll(ausencias);
    }


}