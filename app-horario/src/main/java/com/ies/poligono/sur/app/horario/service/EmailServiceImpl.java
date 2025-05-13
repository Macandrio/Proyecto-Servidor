package com.ies.poligono.sur.app.horario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void enviarMail(String destinatario, String asunto, String contenido) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("alejandrogomezzrivera2002@gmail.com");
		message.setSubject(asunto);
		message.setText(contenido);
		mailSender.send(message);
	}
}
