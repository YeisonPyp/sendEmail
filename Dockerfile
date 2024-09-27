# Usamos una imagen base de OpenJDK para Java 17
FROM openjdk:17-jdk-alpine

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR desde la máquina local al contenedor
COPY target/envio-correo-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080, que es el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación dentro del contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
