# Usa imagen oficial de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Crea un directorio dentro del contenedor
WORKDIR /app

# Copiar el JAR generado (cuando compilemos el proyecto)
COPY target/app.jar app.jar

# Expone el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
