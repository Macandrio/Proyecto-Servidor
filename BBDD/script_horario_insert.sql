-- INSERTS DE EJEMPLO

-- Usuarios
INSERT INTO Usuario (nombre, correo, contraseña, rol) VALUES
('Carlos Gómez', 'carlos@example.com', 'pass123', 'profesor'),
('Lucía Herrera', 'lucia@example.com', 'abc456', 'profesor'),
('Pedro López', 'pedro@example.com', 'pedro123', 'administrador'),
('Sara Núñez', 'sara@example.com', 'sara456', 'profesor'),
('Eva Díaz', 'eva@example.com', 'eva789', 'profesor');

-- Profesores
INSERT INTO Profesor (id_usuario, nombre) VALUES
(3, 'Carlos Gómez'),
(4, 'Lucía Herrera'),
(5, 'Pedro López'),
(6, 'Sara Núñez'),
(7, 'Eva Díaz');

-- Asignaturas
INSERT INTO Asignatura (nombre) VALUES
('Lengua'),
('Ciencias Naturales'),
('Física'),
('Educación Física'),
('Tecnología');

-- Cursos
INSERT INTO Curso (nombre) VALUES
('3º ESO'),
('4º ESO'),
('1º Bachillerato'),
('2º Bachillerato');

-- Aulas
INSERT INTO Aula (codigo) VALUES
('C301'),
('D404'),
('E505'),
('F606');

-- Franjas
INSERT INTO Franja (hora_inicio, hora_fin) VALUES
('10:00:00', '11:00:00'),
('11:00:00', '12:00:00'),
('12:00:00', '13:00:00'),
('13:00:00', '14:00:00');

-- Horarios
INSERT INTO Horario (id_asignatura, id_curso, id_aula, id_profesor, dia, franja) VALUES
(3, 3, 3, 3, 'Miércoles', 3),
(4, 2, 2, 4, 'Jueves', 2),
(5, 1, 1, 5, 'Viernes', 1),
(2, 2, 2, 6, 'Martes', 4),
(1, 4, 4, 7, 'Lunes', 2),
(1, 3, 1, 1, 'Lunes', 1),
(2, 1, 2, 2, 'Martes', 2),
(3, 2, 3, 3, 'Miércoles', 3),
(4, 4, 4, 4, 'Jueves', 4),
(5, 3, 1, 5, 'Viernes', 1);

-- Ausencias
INSERT INTO Ausencia (descripcion, id_horario) VALUES
('Falta por reunión', 3),
('Permiso médico', 5),
('Viaje institucional', 6),
('Falta sin justificar', 7),
('Retraso por tráfico', 9);
