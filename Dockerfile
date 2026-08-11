# Etapa 1: Compilación con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /code

# Copiar pom.xml y código fuente desde la ruta doble anidada
COPY backend-citas-fhir/backend-citas-fhir/pom.xml .
COPY backend-citas-fhir/backend-citas-fhir/src ./src

# Compilar omitiendo tests
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final de ejecución Quarkus
FROM eclipse-temurin:17-jre
WORKDIR /work/

# Copiar las librerías y ejecutable compilado de Quarkus
COPY --from=build /code/target/quarkus-app/lib/ /work/lib/
COPY --from=build /code/target/quarkus-app/*.jar /work/
COPY --from=build /code/target/quarkus-app/app/ /work/app/
COPY --from=build /code/target/quarkus-app/quarkus/ /work/quarkus/

EXPOSE 8080
USER 1001

CMD ["java", "-jar", "/work/quarkus-run.jar"]