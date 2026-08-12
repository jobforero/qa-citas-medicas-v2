# 🏥 Backend Citas Médicas HL7 FHIR - Quarkus REST API

API REST construida con **Quarkus (Java 17)** y **MongoDB Atlas Panache** para el agendamiento y gestión de citas médicas con interoperabilidad bajo el estándar **HL7 FHIR (Appointment Resource)**.

---

## 🛠️ Tech Stack & Arquitectura

* **Framework:** Quarkus 3.x (Java 17)
* **Persistencia:** Quarkus MongoDB con Panache (`PanacheMongoEntity` / `PanacheMongoRepository`)
* **Interoperabilidad:** Mapeo BSON a recursos HL7 FHIR (`Appointment`)
* **Pruebas Unitarias e Integración:** JUnit 5, RestAssured, JaCoCo (Cobertura > 85%)
* **Calidad de Código:** SonarCloud / SonarQube
* **Despliegue:** Docker Container desplegado en Render PaaS

---

## 🚀 Comandos para Ejecución Local

### Prerrequisitos
* Java 17 SDK instalado
* Maven 3.8+ (o utilizar `./mvnw`)
* Instancia activa de MongoDB local o URI de MongoDB Atlas

### 1. Ejecutar en Modo Desarrollo (Dev Mode)
El modo dev de Quarkus incluye Live Coding y Dev UI:
```bash
./mvnw quarkus:dev

./mvnw clean test jacoco:report