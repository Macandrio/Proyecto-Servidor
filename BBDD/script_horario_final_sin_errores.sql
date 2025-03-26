
-- BORRADO DE TABLAS SI EXISTEN
DROP TABLE IF EXISTS Ausencia;
DROP TABLE IF EXISTS Horario;
DROP TABLE IF EXISTS Franja;
DROP TABLE IF EXISTS Aula;
DROP TABLE IF EXISTS Curso;
DROP TABLE IF EXISTS Asignatura;
DROP TABLE IF EXISTS Profesor;
DROP TABLE IF EXISTS Usuario;

-- CREACIÓN DE TABLAS

CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

CREATE TABLE Profesor (
    id_profesor INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Asignatura (
    id_asignatura INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE Curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE Aula (
    id_aula INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(100) NOT NULL
);

CREATE TABLE Franja (
    id_franja INT AUTO_INCREMENT PRIMARY KEY,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL
);

CREATE TABLE Horario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_asignatura INT NOT NULL,
    id_curso INT NOT NULL,
    id_aula INT NOT NULL,
    id_profesor INT NOT NULL,
    dia VARCHAR(20) NOT NULL,
    franja INT NOT NULL,
    FOREIGN KEY (id_asignatura) REFERENCES Asignatura(id_asignatura),
    FOREIGN KEY (id_curso) REFERENCES Curso(id_curso),
    FOREIGN KEY (id_aula) REFERENCES Aula(id_aula),
    FOREIGN KEY (id_profesor) REFERENCES Profesor(id_profesor),
    FOREIGN KEY (franja) REFERENCES Franja(id_franja)
);

CREATE TABLE Ausencia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT,
    id_horario INT NOT NULL,
    FOREIGN KEY (id_horario) REFERENCES Horario(id)
);

-- INSERTS DE DATOS SEGUROS

-- Usuarios y Profesores
INSERT INTO Usuario (nombre, correo, contraseña, rol) VALUES 
('Ana Torres', 'ana@example.com', '1234', 'profesor'),
('Carlos Gómez', 'carlos@example.com', 'pass123', 'profesor');

INSERT INTO Profesor (id_usuario, nombre) VALUES 
(1, 'Ana Torres'),
(2, 'Carlos Gómez');

-- Asignaturas
INSERT INTO Asignatura (nombre) VALUES 
('Matemáticas'),
('Lengua');

-- Cursos
INSERT INTO Curso (nombre) VALUES 
('1º ESO'),
('2º ESO');

-- Aulas
INSERT INTO Aula (codigo) VALUES 
('A101'),
('B202');

-- Franjas
INSERT INTO Franja (hora_inicio, hora_fin) VALUES 
('08:00:00', '09:00:00'),
('09:00:00', '10:00:00');

-- Horarios (ahora insertamos usando IDs válidos)
INSERT INTO Horario (id_asignatura, id_curso, id_aula, id_profesor, dia, franja) VALUES 
(1, 1, 1, 1, 'Lunes', 1),
(2, 2, 2, 2, 'Martes', 2);

-- Ausencias válidas que referencian horarios creados
INSERT INTO Ausencia (descripcion, id_horario) VALUES 
('Falta por enfermedad', 1),
('Reunión externa', 2);
