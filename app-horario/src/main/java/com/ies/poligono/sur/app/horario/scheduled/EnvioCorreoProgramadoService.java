package com.ies.poligono.sur.app.horario.scheduled;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.apputils.AppHorarioUtils;
import com.ies.poligono.sur.app.horario.enums.EmailEnum;
import com.ies.poligono.sur.app.horario.model.Usuario;
import com.ies.poligono.sur.app.horario.service.EmailService;
import com.ies.poligono.sur.app.horario.service.GenerarParteAusenciaService;
import com.ies.poligono.sur.app.horario.service.UsuarioService;

import jakarta.mail.MessagingException;

@Service
public class EnvioCorreoProgramadoService {

	@Autowired
	private GenerarParteAusenciaService generarParteAusenciaService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UsuarioService usuarioService;

//	@Scheduled(cron = "0 * * * * *")
	@Scheduled(cron = "0 0 8 * * *")
	public void envioCorreoParteAusencias() {
		try {
			List<String> lstEmails = usuarioService.findLstEmailByRol("administrador");

			String nombreAdjunto = "Parte_Ausencias_".concat(AppHorarioUtils.formatearFecha(LocalDate.now()))
					.concat("_");
			emailService.enviarMailConAdjuntos(lstEmails.toArray(new String[lstEmails.size()]),
					EmailEnum.MSG_PARTE_AUSENCIAS_ASUNTO.getValor(), EmailEnum.MSG_PARTE_AUSENCIAS_CONTENIDO.getValor(),
					generarParteAusenciaService.generarParte(LocalDate.now()), nombreAdjunto, ".pdf");
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

}