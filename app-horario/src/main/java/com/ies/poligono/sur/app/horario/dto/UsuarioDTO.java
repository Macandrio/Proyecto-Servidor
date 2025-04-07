package com.ies.poligono.sur.app.horario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "Debe tener un formato de email válido")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 6, message = "Debe tener al menos 6 caracteres")
    private String contraseña;

    @NotNull(message = "El rol no puede ser nulo")
    @Pattern(regexp = "^(profesor|administrador)$", message = "El rol debe ser 'profesor' o 'administrador'")
    private String rol;
}
