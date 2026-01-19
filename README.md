
# Pricing Service – Spring Boot (Hexagonal Architecture)

## Descripción

Este proyecto implementa un **servicio REST** para consultar el **precio aplicable de un producto** en una fecha concreta, teniendo en cuenta **rangos temporales** y **prioridad de tarifas**, según el enunciado de la prueba técnica.

La solución está desarrollada en **Java + Spring Boot**, siguiendo **Arquitectura Hexagonal**, principios **SOLID**, y con **tests de integración** que validan todos los escenarios solicitados.

---

## Arquitectura

El proyecto sigue una **Arquitectura Hexagonal (Ports & Adapters)**, separando claramente responsabilidades:

```
domain
 └── model               → Modelo de dominio (Price)
application
 ├── port.in             → Casos de uso (interfaces)
 ├── port.out            → Puertos de persistencia
 └── service             → Implementación de casos de uso
adapter.in.web           → REST Controllers + DTOs
adapter.out.persistence  → Implementación JPA del puerto de persistencia (H2 en memoria)

```
<img width="571" height="942" alt="image" src="https://github.com/user-attachments/assets/7d32f1ad-9cc8-4dfd-8e79-807dafb46d2f" />


### Beneficios

* Dominio desacoplado de frameworks
* Fácil testabilidad
* Alta mantenibilidad
* Cumple SOLID y Clean Architecture

---

## Tecnologías

* Java 17
* Spring Boot 3.3.2
* Spring Web (REST)
* Spring Data JPA
* H2 (Base de datos en memoria)
* Maven
* JUnit 5 + MockMvc
* Hibernate

---

## Ejecución del proyecto

### Requisitos

* Java 17+
* Maven 3.8+

### Arrancar la aplicación

```bash
mvn spring-boot:run
````

La aplicación expone el siguiente endpoint REST:

```
GET http://localhost:8080/api/v1/prices/applicable
```

Ejemplo de petición:

```bash
curl "http://localhost:8080/api/v1/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```
<img width="1478" height="368" alt="image" src="https://github.com/user-attachments/assets/30b11f98-c294-488e-ab1c-0da14e38125d" />



---

## Base de datos H2

La base de datos se inicializa automáticamente al arrancar la aplicación usando `data.sql`, con los datos del enunciado.

### Consola H2

```
http://localhost:8080/h2-console
```

**Configuración:**

* JDBC URL: `jdbc:h2:mem:pricingdb`
* User: `sa`
* Password: *(vacío)*
<img width="786" height="487" alt="image" src="https://github.com/user-attachments/assets/b4b08d8c-8d56-40f1-8a5f-971c92a505b1" />
<img width="927" height="486" alt="image" src="https://github.com/user-attachments/assets/95a4ced8-9cce-4cf6-bd12-a374374af000" />

---

## Endpoint REST

### Obtener precio aplicable

**Endpoint**

```
GET /api/v1/prices/applicable
```

**Parámetros**

| Parámetro       | Tipo              | Descripción                |
| ---------       | ----------------- | -------------------------- |
| applicationDate | ISO-8601 datetime | Fecha de aplicación        |
| productId       | Long              | Identificador del producto |
| brandId         | Long              | Identificador de la cadena |

**Ejemplo**

```bash
curl "http://localhost:8080/api/v1/prices/applicable?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

**Respuesta**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

**Códigos HTTP**

* `200 OK` → Precio encontrado
* `404 Not Found` → No existe precio aplicable


<img width="893" height="219" alt="image" src="https://github.com/user-attachments/assets/1143dbb9-d934-4864-9f6e-3592b308b16d" />


Las respuestas de error siguen el estándar `ProblemDetail` (RFC 7807), devolviendo información consistente para errores de validación y recursos no encontrados.

```
{
  "type": "about:blank",
  "title": "Price not found",
  "status": 404,
  "detail": "No applicable price found for productId=99999, brandId=1 at 2020-06-14T10:00",
  "path": "/api/v1/prices/applicable"
}
```
---

## Eficiencia de la consulta

La consulta a base de datos:

* Filtra por **brandId**, **productId** y **rango de fechas**
* Ordena por **prioridad descendente**
* Devuelve **un único resultado**

Esto se implementa mediante una consulta **JPQL explícita** en el adaptador de persistencia:



```@Query("""
    SELECT p
    FROM PriceJpaEntity p
    WHERE p.brandId = :brandId
      AND p.productId = :productId
      AND :applicationDate BETWEEN p.startDate AND p.endDate
    ORDER BY p.priority DESC
""")
List<PriceJpaEntity> findApplicableOrderedByPriorityDesc(...);
```

✔️ Un único acceso a base de datos  
✔️ Sin filtrado en memoria  
✔️ Intención de negocio explícita  
✔️ Fácil de mantener y testear


---

## Testing

Se incluyen **tests de integración** que validan todos los escenarios del enunciado, ejecutando el endpoint REST real.

### Ejecutar tests

```bash
mvn clean test
```

### Escenarios cubiertos

| Test   | Fecha       | Resultado esperado  |
| ------ | ----------- | ------------------- |
| Test 1 | 14/06 10:00 | priceList 1 – 35.50 |
| Test 2 | 14/06 16:00 | priceList 2 – 25.45 |
| Test 3 | 14/06 21:00 | priceList 1 – 35.50 |
| Test 4 | 15/06 10:00 | priceList 3 – 30.50 |
| Test 5 | 16/06 21:00 | priceList 4 – 38.95 |

Los tests se encuentran en:

```
src/test/java/com/inditex/pricing
```
<img width="994" height="842" alt="image" src="https://github.com/user-attachments/assets/e1de8e60-ef17-4b20-ad06-385935102153" />

---

## Evidencia de ejecución de tests

Tras ejecutar:

```bash
mvn clean test
```
<img width="2334" height="763" alt="image" src="https://github.com/user-attachments/assets/af31c0d9-499a-4275-afe4-685c69d7ff9f" />


Se genera el reporte en:

```
target/surefire-reports/
```
<img width="443" height="130" alt="image" src="https://github.com/user-attachments/assets/1cb1f2a3-f67e-43ba-a47a-b8d8728adb5e" />


---
## Conclusión

Esta solución cumple con los requisitos establecidos en el enunciado de la prueba técnica.

La aplicación ha sido diseñada siguiendo una Arquitectura Hexagonal, garantizando una correcta separación de responsabilidades y manteniendo la lógica de dominio desacoplada de la infraestructura. La API REST aplica buenas prácticas y expone un único endpoint que resuelve de forma determinista el precio aplicable en función de la fecha, el producto y la cadena.

La inicialización de datos se realiza automáticamente mediante una base de datos en memoria H2. Los escenarios solicitados han sido validados mediante pruebas de integración que cubren el flujo completo de la aplicación, dando como resultado una solución clara, mantenible y fácil de extender.


---
Autor  
Carlos Mondragón
