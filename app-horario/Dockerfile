# Usa una imagen de Java
FROM openjdk:21-jdk-slim

# Directorio donde se copiará el JAR
WORKDIR /app

# Copia el JAR generado
# COPY target/app-horario.jar app-horario.jar
COPY target/app-horario-0.0.1-SNAPSHOT.jar app-horario.jar


# Expone el puerto 8081
EXPOSE 8081

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app-horario.jar"]
