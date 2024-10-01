# Task Manager Backend

## Tabla de contenido:
* [Descripción del proyecto](https://github.com/thesrcielos/BarquitaBackend/tree/featureTaskService?tab=readme-ov-file#Descripción-del-Proyecto)
* [Características principales](https://github.com/thesrcielos/BarquitaBackend/tree/featureTaskService?tab=readme-ov-file#Características-Principales)
* [Tecnologías de uso](https://github.com/thesrcielos/BarquitaBackend/tree/featureTaskService?tab=readme-ov-file#Tecnologías-Utilizadas)
* [Requisitos del sistema](https://github.com/thesrcielos/BarquitaBackend/tree/featureTaskService?tab=readme-ov-file#Requisitos-del-Sistema)
* [Configuración del proyecto](https://github.com/thesrcielos/BarquitaBackend/tree/featureTaskService?tab=readme-ov-file#Configuración-del-Proyecto)
* [Colaboradores](https://github.com/thesrcielos/BarquitaBackend/tree/featureTaskService?tab=readme-ov-file#Colaboradores)


# Task Manager Project

## Descripción del Proyecto

Este proyecto es una implementación de una aplicación web para la 
gestión de tareas personales. En este repositorio se encuentra el 
desarrollo del Backend. El objetivo principal fue afianzar 
nuestros conocimientos en el uso del framework Spring Boot y 
desarrollar habilidades con herramientas clave del ecosistema de 
desarrollo de software, como JACOCO para el análisis de cobertura, 
Maven para la gestión del proyecto, y SonarQube, en conjunto con 
Docker, para el análisis de código. Además, integramos Azure 
DevOps para aplicar la metodología SCRUM, lo que nos permitió aprender 
a trabajar en equipo en entornos de desarrollo organizados y realizar 
un seguimiento eficiente de las tareas. El proyecto se estructuró en 
épicas, que se subdividieron en sprints y tareas individuales.


## Características Principales

- Agregar nuevas tareas con una descripción
- Visualizar una lista de todas las tareas existentes
- Marcar tareas como completadas
- Eliminar tareas de la lista
- Organizar tareas según la prioridad

## Tecnologías Utilizadas

- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Java con Spring Boot
- **Base de datos**: MongoDB Cloud y sistema de almacenamiento en archivo de texto plano
- **Gestión de dependencias**: Maven
- **Control de versiones**: Git y GitHub
- **Análisis de código**: SonarQube
- **Cobertura de pruebas**: JaCoCo

## Requisitos del Sistema

- Java OpenJDK Runtime Environment: 17.x.x
- Apache Maven: 3.9.x
- Docker

## Configuración del Proyecto

1. Clonar el repositorio:
   ```
   git clone https://github.com/thesrcielos/BarquitaBackend
   ```

2. Navegar al directorio del proyecto:
   ```
   cd BarquitaBackend
   ```

3. Instalar las dependencias del proyecto:
   ```
   mvn install
   ```

4. Ejecutar la aplicación:
   ```
   mvn spring-boot:run
   ```


### API REST

La API REST proporciona los siguientes endpoints:

- `POST /addTask`: Agrega una nueva tarea.
    - Cuerpo: JSON con los detalles de la tarea (TaskDTO)
    - Respuesta: La tarea creada en formato JSON

- `GET /getAllTasks`: Obtiene todas las tareas.
    - Respuesta: Lista de todas las tareas en formato JSON

- `GET /getTasksByState`: Obtiene tareas filtradas por estado.
    - Parámetro de consulta: `state` (boolean)
    - Respuesta: Lista de tareas con el estado especificado

- `DELETE /deleteTask`: Elimina una tarea específica.
    - Parámetro de consulta: `id` (String)
    - Respuesta: Confirmación de éxito ("OK")

- `PUT /changeStateTask`: Cambia el estado de una tarea específica.
    - Parámetro de consulta: `id` (String)
    - Respuesta: Confirmación de éxito ("OK")

- `PUT /updateTask`: Actualiza una tarea existente.
    - Cuerpo: JSON con los detalles actualizados de la tarea (TaskDTO)
    - Respuesta: Confirmación de éxito ("OK")

- `GET /getTasksByDeadline`: Obtiene tareas filtradas por fecha límite.
    - Parámetro de consulta: `deadline` (LocalDateTime)
    - Respuesta: Lista de tareas con la fecha límite especificada

- `GET /getTaskByPriority`: Obtiene tareas filtradas por prioridad.
    - Parámetro de consulta: `priority` (Priority)
    - Respuesta: Lista de tareas con la prioridad especificada

Todas las respuestas incluyen códigos de estado HTTP 
apropiados para indicar el éxito o fracaso de la operación.
Además se filtran los errores en el controlador para 
proteger la información del cliente, pero queda registrado en el log
para que el equipo de desarrolladores estén al tanto de los 
posibles errores que se presenten.


### Pruebas

El proyecto incluye pruebas unitarias para el backend. Para ejecutar las pruebas
(debe ejecutarse luego de haber compilado el proyecto con `mvn compile`:

```
mvn test
```

La cobertura de código se realiza con JaCoCo y se puede visualizar 
en SonarQube o en el archivo generado al empaquetar con Maven, se almacena en
el directorio `target/site/jacoco/jacoco-resources`.

### Análisis de Código

El análisis estático del código se realiza con SonarQube. Para ejecutar el análisis:

```
mvn sonar:sonar
```

### Contribución

Si deseas contribuir a nuestro proyecto, 
te invitamos a hacer un fork del repositorio y enviar una Pull Request 
(PR). Agradecemos cualquier comentario o retroalimentación que 
nos ayude a mejorar y continuar creciendo.

## Colaboradores
* [Diego Macia]()
* [Daniel Aldana]()
* [Santiago Avellaneda]()
* [Miguel Motta]()

## Licencia

> Proximamente...