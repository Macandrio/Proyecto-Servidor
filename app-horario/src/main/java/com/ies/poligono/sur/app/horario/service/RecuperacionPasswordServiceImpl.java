package com.ies.poligono.sur.app.horario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.apputils.PasswordUtils;
import com.ies.poligono.sur.app.horario.dao.UsuarioRepository;
import com.ies.poligono.sur.app.horario.dto.PostRecuperacionPasswordInputDTO;
import com.ies.poligono.sur.app.horario.enums.EmailEnum;
import com.ies.poligono.sur.app.horario.model.Usuario;

@Service
public class RecuperacionPasswordServiceImpl implements RecuperacionPasswordService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void recuperarPassword(PostRecuperacionPasswordInputDTO dto) {
		// comprobar que el usuario exista
		Usuario usuario = usuarioRepository.findByEmail(dto.getCorreoRecuperacion());
		if (usuario != null) {
			String nuevaPassword = PasswordUtils.generarPasswordAleatoria(12);
			// actualizo la contraseña
			usuarioService.actualizarContraseña(usuario.getId(), nuevaPassword, true);
			// enviar email con la pass sin encriptar
			enviarEmailRecuperacion(usuario.getEmail(), nuevaPassword);

		}

	}

	private void enviarEmailRecuperacion(String email, String nuevaPassword) {
		emailService.enviarMail(email, EmailEnum.MSG_RECUP_PASS_ASUNTO.getValor(),
				String.format(EmailEnum.MSG_RECUP_PASS_CONTENIDO.getValor(), nuevaPassword));
	}

}
