package com.ies.poligono.sur.app.horario.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.apputils.AppHorarioUtils;
import com.ies.poligono.sur.app.horario.dto.AusenteGuardiaDTO;
import com.ies.poligono.sur.app.horario.dto.CuadranteGuardiasDTO;
import com.ies.poligono.sur.app.horario.dto.FranjaGuardiaDTO;
import com.ies.poligono.sur.app.horario.model.Franja;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class GenerarParteAusenciaServiceImpl implements GenerarParteAusenciaService {

	private static final String SALTO_LINEA = "\n";

	@Autowired
	private FranjaService franjaService;

	@Autowired
	private ProfesorService profesorService;

	@Autowired
	private AusenciaService ausenciaService;

	@Override
	public String generarParte(LocalDate fecha) {
		String base64Pdf = StringUtils.EMPTY;

		CuadranteGuardiasDTO cuadrante = obtenerInformacionCuadranteGuardia(fecha);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			Document documento = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance(documento, baos);
			documento.open();

			// Configurar fuentes
			Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);
			Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLACK);
			Font cellFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);

			// Título del documento
			Paragraph titulo = new Paragraph(crearTituloDocumento(fecha), tituloFont);
			titulo.setAlignment(Element.ALIGN_CENTER);
			titulo.setSpacingAfter(15);
			documento.add(titulo);

			// Crear tabla con 5 columnas
			PdfPTable tabla = new PdfPTable(5);
			tabla.setWidthPercentage(100);
			tabla.setSpacingBefore(10f);
			tabla.setWidths(new float[] { 1.0f, 3.5f, 3.5f, 2.5f, 1.0f });

			// Encabezados
			Stream.of("Hora", "Profesores", "Ausentes", "Grupo", "Aula").forEach(col -> {
				PdfPCell header = new PdfPCell(new Phrase(col, headerFont));
				header.setBackgroundColor(Color.LIGHT_GRAY);
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header.setFixedHeight(28f);
				tabla.addCell(header);
			});

			// Contenido de la tabla
			for (FranjaGuardiaDTO franjaGuardia : cuadrante.getLstFranjaGuardias()) {

				// Hora
				tabla.addCell(celdaTexto(extraerHoraFranja(franjaGuardia.getFranjaHoraria()), cellFont));

				// Profesores
				tabla.addCell(celdaViñetas(extraerProfesores(franjaGuardia.getLstProfesoresGuardia()), cellFont));

				// Ausentes
				tabla.addCell(celdaViñetas(extraerAusentes(franjaGuardia.getLstAusentesGuardia()), cellFont));

				// Grupo
				tabla.addCell(celdaViñetas(extraerGrupos(franjaGuardia.getLstAusentesGuardia()), cellFont));

				// Aula
				tabla.addCell(celdaViñetas(extraerAulas(franjaGuardia.getLstAusentesGuardia()), cellFont));
			}

			// Añadir tabla al documento
			documento.add(tabla);
			documento.close();
			base64Pdf = new String(Base64.getEncoder().encode(baos.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Pdf;
	}

	private String crearTituloDocumento(LocalDate fecha) {
		StringBuilder sb = new StringBuilder();
		sb.append(AppHorarioUtils.formatearFecha(fecha));
		sb.append(" ");
		sb.append("(");
		sb.append(AppHorarioUtils.obtenerDiaSemanaByFecha(fecha));
		sb.append(")");
		return sb.toString();
	}

	@Override
	public CuadranteGuardiasDTO obtenerInformacionCuadranteGuardia(LocalDate fecha) {

		CuadranteGuardiasDTO dto = new CuadranteGuardiasDTO();

		List<FranjaGuardiaDTO> lstFranjaGuardias = new ArrayList<FranjaGuardiaDTO>();

		// Hora
		List<Franja> lstFranja = franjaService.findAll();

		for (Franja franja : lstFranja) {
			FranjaGuardiaDTO franjaGuardiaDTO = new FranjaGuardiaDTO();
			franjaGuardiaDTO.setFranjaHoraria(franja);
			franjaGuardiaDTO.setLstProfesoresGuardia(profesorService
					.findProfesoresGuardia(AppHorarioUtils.obtenerDiaSemanaAbrevByFecha(fecha), franja.getIdFranja()));
			franjaGuardiaDTO.setLstAusentesGuardia(ausenciaService.findProfesoresAusentes(fecha, franja.getIdFranja()));
			lstFranjaGuardias.add(franjaGuardiaDTO);
		}

		dto.setLstFranjaGuardias(lstFranjaGuardias);

		return dto;
	}

	private String extraerHoraFranja(Franja franja) {
		StringBuilder sb = new StringBuilder();
		sb.append(franja.getIdFranja());
		sb.append(SALTO_LINEA);
		sb.append(localTimeToString(franja.getHoraInicio()));
		sb.append(SALTO_LINEA);
		sb.append(localTimeToString(franja.getHoraFin()));
		return sb.toString();
	}

	private String extraerProfesores(List<String> lstProfesores) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> iterator = lstProfesores.iterator(); iterator.hasNext();) {
			String nombre = (String) iterator.next();
			sb.append(nombre);
			if (iterator.hasNext()) {
				sb.append(SALTO_LINEA);
			}
		}

		return sb.toString();
	}

	private String extraerAulas(List<AusenteGuardiaDTO> lstAusentesGuardia) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<AusenteGuardiaDTO> iterator = lstAusentesGuardia.iterator(); iterator.hasNext();) {
			AusenteGuardiaDTO dto = (AusenteGuardiaDTO) iterator.next();
			sb.append(dto.getAula());
			if (iterator.hasNext()) {
				sb.append(SALTO_LINEA);
			}
		}
		return sb.toString();
	}

	private String extraerGrupos(List<AusenteGuardiaDTO> lstAusentesGuardia) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<AusenteGuardiaDTO> iterator = lstAusentesGuardia.iterator(); iterator.hasNext();) {
			AusenteGuardiaDTO dto = (AusenteGuardiaDTO) iterator.next();
			sb.append(dto.getGrupo());
			if (iterator.hasNext()) {
				sb.append(SALTO_LINEA);
			}
		}
		return sb.toString();
	}

	private String extraerAusentes(List<AusenteGuardiaDTO> lstAusentesGuardia) {

		StringBuilder sb = new StringBuilder();
		for (Iterator<AusenteGuardiaDTO> iterator = lstAusentesGuardia.iterator(); iterator.hasNext();) {
			AusenteGuardiaDTO dto = (AusenteGuardiaDTO) iterator.next();
			sb.append(dto.getProfesor());
			if (iterator.hasNext()) {
				sb.append(SALTO_LINEA);
			}
		}
		return sb.toString();
	}

	private String localTimeToString(LocalTime ldt) {
		return ldt.toString();
	}

	private PdfPCell celdaTexto(String contenido, Font font) {
		PdfPCell celda = new PdfPCell(new Phrase(contenido, font));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celda.setFixedHeight(60f);
		celda.setPadding(5);
		celda.setBorderColor(Color.GRAY);
		return celda;
	}

	private PdfPCell celdaViñetas(String contenido, Font font) {
		PdfPCell celda = new PdfPCell(new Phrase(formatearConViñetas(contenido), font));
		celda.setHorizontalAlignment(Element.ALIGN_LEFT);
		celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celda.setFixedHeight(60f);
		celda.setPaddingLeft(10f);
		celda.setBorderColor(Color.GRAY);
		return celda;
	}

	private String formatearConViñetas(String texto) {
		if (texto == null || texto.isBlank())
			return "";
		return Stream.of(texto.split("\n")).map(linea -> "• " + linea).reduce((a, b) -> a + "\n" + b).orElse("");
	}

}
