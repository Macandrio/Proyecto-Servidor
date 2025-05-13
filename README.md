1. Construcción de la aplicación Spring Boot
    a. Compilar el proyecto y generar el .jar
        Desde la raíz del proyecto, ejecuta:

        ./mvnw clean package

        Este comando genera el archivo .jar en la carpeta target/. Ejemplo:

        target/app-horario-0.0.1-SNAPSHOT.jar

        ![Image](img/image_1.png)

2. Crear la imagen Docker del backend
    a. Dockerfile utilizado:

        FROM openjdk:21-jdk-slim
        WORKDIR /app
        COPY target/app-horario-0.0.1-SNAPSHOT.jar app-horario.jar
        EXPOSE 8081
        ENTRYPOINT ["java", "-jar", "app-horario.jar"]

        ![Image](img/image_2.png)

    b. Crear imagen:

    docker build -t macandrio/backend-app-horario:v0.1 .

3. Subir imagen a Docker Hub

    a. Iniciar sesión:
        docker login

    b. Subir la imagen:
        docker push macandrio/backend-app-horario:v0.1
    La imagen aparece en Docker Hub:

4. docker-compose.yml para backend y MariaDB

    a. Creamos el archivo docker-compose.yml
        version: "3.8"

        services:
        mariadb:
            image: mariadb
            container_name: mariadb
            restart: always
            environment:
            MYSQL_ROOT_PASSWORD: root
            MYSQL_DATABASE: bbdd-mi-app
            MYSQL_USER: admin
            MYSQL_PASSWORD: admin
            ports:
            - "3306:3306"
            networks:
            - red_horario

        backend:
            image: macandrio/backend-app-horario:v0.1
            container_name: Backend-app
            restart: always
            ports:
            - "8081:8081"
            depends_on:
            - mariadb
            networks:
            - red_horario

        networks:
        red_horario:
            driver: bridge

        ![Image](img/image_3.png)

 5. application.properties del backend

    # BASE DE DATOS
    spring.datasource.url=jdbc:mariadb://mariadb:3306/bbdd-mi-app
    spring.datasource.username=admin
    spring.datasource.password=admin
    spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect

    ![Image](img/image_4.png)

6. Despliegue en AWS EC2
    a. En la instancia EC2:
        sudo apt update
        sudo apt install docker.io
        sudo systemctl start docker
        sudo systemctl enable docker

    b. Instalar Docker Compose:
        sudo curl -L "https://github.com/docker/compose/releases/download/v2.27.0/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose
        sudo chmod +x /usr/local/bin/docker-compose
        docker-compose --version

    c.Crear el archivo docker-compose.yml y lanzarlo:
        docker-compose up -d

7. Verificación y conexión desde DBeaver
    a. Ver que los contenedores están corriendo:
        docker ps
    b. Asegurarte de que el puerto 3306 esté abierto en el grupo de seguridad de la instancia EC2.
    c. Conectarte desde DBeaver:
        Host: IP pública de la instancia EC2

        Puerto: 3306

        Base de datos: bbdd-mi-app

        Usuario: admin

        Contraseña: admin

        ![Image](img/image_5.png)

