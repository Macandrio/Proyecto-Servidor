package com.ies.poligono.sur.app.horario.model;

import java.util.List;

public enum Role {
	ADMINISTRADOR(List.of("MANAGE_USERS", "VIEW_SCHEDULE", "ASSIGN_COURSES")),
	PROFESOR(List.of("VIEW_SCHEDULE", "TEACH_COURSE"));

	private final List<String> permissions;

	Role(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<String> getPermissions() {
		return permissions;
	}
}
