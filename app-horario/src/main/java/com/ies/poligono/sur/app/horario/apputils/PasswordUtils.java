package com.ies.poligono.sur.app.horario.apputils;

import java.security.SecureRandom;

public class PasswordUtils {
	private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final String ESPECIALES = "!@#$%^&*()_-+=<>?";
    private static final String TODOS = MAYUSCULAS + MINUSCULAS + NUMEROS + ESPECIALES;

    private static final SecureRandom random = new SecureRandom();

    public static String generarPasswordAleatoria(int longitud) {
        StringBuilder password = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(TODOS.length());
            password.append(TODOS.charAt(index));
        }

        return password.toString();
    }
}
