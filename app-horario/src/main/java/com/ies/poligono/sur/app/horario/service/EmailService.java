package com.ies.poligono.sur.app.horario.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import jakarta.mail.MessagingException;

public interface EmailService {

	public void enviarMail(String destinatario, String asunto, String contenido);

	public void enviarMailConAdjuntos(String[] destinatarios, String asunto, String contenido, String base64Pdf,
			String nombreAdjunto, String extensionAdjunto)
			throws MessagingException, FileNotFoundException, IOException;

}
