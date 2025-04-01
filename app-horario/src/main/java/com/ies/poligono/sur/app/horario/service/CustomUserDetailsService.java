package com.ies.poligono.sur.app.horario.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ies.poligono.sur.app.horario.dao.UsuarioRepository;
import com.ies.poligono.sur.app.horario.model.Usuario;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = usuarioRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
//		Role.valueOf(user.getRol()).getPermissions();
		return new User(user.getEmail(), user.getContraseña(), new ArrayList<>());
	}
}
