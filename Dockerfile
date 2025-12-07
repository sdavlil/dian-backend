# ---------------------------
# Etapa 1: Build
# ---------------------------
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Crear directorio de trabajo
WORKDIR /app

# Copiar solo archivos necesarios para build
COPY pom.xml mvnw ./
COPY .mvn/ .mvn/

# Dar permisos al wrapper (Linux)
RUN chmod +x mvnw

# Copiar el código fuente
COPY src/ src/

# Construir la app (generar JAR)
RUN ./mvnw clean package -DskipTests

# ---------------------------
# Etapa 2: Runtime
# ---------------------------
FROM eclipse-temurin:17-jre

# Crear directorio de la app en runtime
WORKDIR /app

# Copiar el JAR generado en la etapa de build
COPY --from=build /app/target/dian-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto que usará tu app
EXPOSE 8080

# Comando para ejecutar la app
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]

