package com.ies.poligono.sur.app.horario.enums;

public enum EmailEnum {

	MSG_RECUP_PASS_ASUNTO("Recuperación de contraseña"),
	MSG_RECUP_PASS_CONTENIDO("La nueva contraseña de acceso a la app de horario es: %s"),

	MSG_PARTE_AUSENCIAS_ASUNTO("Parte de profesores ausentes"),
	MSG_PARTE_AUSENCIAS_CONTENIDO("Se adjunta el parte de profesores ausentes.");

	private final String valor;

	EmailEnum(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

}
