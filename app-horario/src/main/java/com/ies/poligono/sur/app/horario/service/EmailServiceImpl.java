package com.ies.poligono.sur.app.horario.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.apputils.AppHorarioUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void enviarMail(String destinatario, String asunto, String contenido) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(destinatario, "alejandrogomezzrivera2002@gmail.com");
		message.setSubject(asunto);
		message.setText(contenido);
		mailSender.send(message);
	}

	@Override
	public void enviarMailConAdjuntos(String[] destinatarios, String asunto, String contenido, String base64Pdf,
			String nombreAdjunto, String extensionAdjunto)
			throws MessagingException, FileNotFoundException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(destinatarios);
		helper.setCc("alejandrogomezzrivera2002@gmail.com");
		helper.setSubject(asunto);
		helper.setText(contenido);

		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(base64Pdf);

		// Crear un archivo temporal
		Path tempFile = Files.createTempFile(nombreAdjunto, extensionAdjunto);

		// Escribir los datos en el archivo temporal
		Files.write(tempFile, decodedBytes, StandardOpenOption.CREATE);

		File archivoAdjunto = tempFile.toFile();

		if (archivoAdjunto != null && archivoAdjunto.exists()) {
			FileSystemResource file = new FileSystemResource(archivoAdjunto);
			helper.addAttachment(archivoAdjunto.getName(), file);
		}

		mailSender.send(message);

	}
}
