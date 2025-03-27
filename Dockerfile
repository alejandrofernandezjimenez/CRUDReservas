# Usar una imagen base de Java
FROM openjdk:17-jdk-alpine

# Email del encargado del dockerfile
LABEL maintainer="Alejandro y Pablo"

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/crudreservas-0.0.1-SNAPSHOT.jar crudreservas.jar

# Exponer el puerto en el que la aplicación se ejecutará
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "crudreservas.jar"]