# 📚 API REST de Usuarios y Notas

## 📜 **Descripción**

**API REST desarrollada en Java + Spring Boot** que permite gestionar **👤 Usuarios** y sus **📄 Notas** de forma persistente. Utiliza **JPA** para el manejo de la base de datos y sigue buenas prácticas arquitectónicas.

---

## 🚀 **Características**

- Gestión de usuarios y notas con relaciones **1:N**.
- Persistencia de datos mediante **MySQL**.
- Validación de datos con **Jakarta Validation**.
- Manejo de excepciones globales.
- Endpoints RESTful con soporte para operaciones CRUD.
- Pruebas unitarias y de integración con **JUnit** y **Mockito**.
- Documentación y colección de pruebas en **Postman**.

---

## 🛠️ **Tecnologías**

- **Java 17**
- **Spring Boot 3.5.0**
- **MySQL**
- **Hibernate/JPA**
- **Lombok**
- **JUnit 5**
- **Mockito**
- **Postman**

---

## 📂 **Estructura del Proyecto**

```plaintext
notasapi/
├── controller/         # Controladores REST
│   ├── UsuarioController.java
│   └── NotaController.java
├── service/            # Lógica de negocio
│   ├── UsuarioService.java
│   ├── NotaService.java
│   ├── AbstractCrudService.java
│   └── CrudService.java
├── repository/         # Acceso a datos
│   ├── UsuarioRepository.java
│   └── NotaRepository.java
├── model/              # Entidades
│   ├── Usuario.java
│   └── Nota.java
├── exception/          # Manejo de excepciones
│   └── GlobalExceptionHandler.java
├── resources/          # Configuración
│   └── application.properties
├── test/               # Pruebas unitarias e integración
│   ├── controller/
│   ├── service/
│   └── NotasapiApplicationTests.java
├── README.md           # Documentación del proyecto
├── pom.xml             # Configuración de Maven
└── 📂 Colección- API notas- CRUD, test.postman_collection.json
```

---

## ⚙️ **Configuración**

### Base de Datos
> La aplicación utiliza **MySQL** como base de datos. Configura las credenciales directamente en el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/notasapi?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=[tu contraseña]
spring.jpa.hibernate.ddl-auto=update
```

### Maven
Asegúrate de tener configurado **Maven Wrapper** para ejecutar el proyecto:

```bash
./mvnw spring-boot:run
```

---

## 📖 **Documentación de Endpoints**

### Usuarios
| Método | Endpoint                  | Descripción                     |
|--------|---------------------------|---------------------------------|
| GET    | `/api/v1/usuarios`        | Listar todos los usuarios       |
| GET    | `/api/v1/usuarios/{id}`   | Obtener usuario por ID          |
| POST   | `/api/v1/usuarios`        | Crear un nuevo usuario          |
| PUT    | `/api/v1/usuarios/{id}`   | Actualizar un usuario existente |
| DELETE | `/api/v1/usuarios/{id}`   | Eliminar un usuario por ID      |

### Notas
| Método | Endpoint                  | Descripción                     |
|--------|---------------------------|---------------------------------|
| GET    | `/api/v1/notas`           | Listar todas las notas          |
| GET    | `/api/v1/notas/{id}`      | Obtener nota por ID             |
| POST   | `/api/v1/notas?usuarioId` | Crear una nueva nota            |
| PUT    | `/api/v1/notas/{id}`      | Actualizar una nota existente   |
| DELETE | `/api/v1/notas/{id}`      | Eliminar una nota por ID        |

---

## 🧪 **Pruebas**

### Ejecución de Pruebas
Ejecuta las pruebas con Maven:

```bash
./mvnw test
```

### Colección de Postman
> Importa la colección `📂 Colección- API notas- CRUD, test.postman_collection.json` en **Postman** para probar los endpoints.

---

## 📄 **Licencia**

> Este proyecto está bajo la licencia **MIT**. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 👨‍💻 **Autores**

**Izan Carlo Celis Afonso**
[GitHub](https://github.com/IzanKing2)  
> He desarrollado todo lo relacionado con el backend: servicios CRUD de usuarios y notas, así como las pruebas unitarias y las pruebas para Postman.

**Diego Lázaro Cádiz Torres**  
[GitHub](https://github.com/Diego12132025)  
> Se encarga de la interfaz gráfica utilizando JSwing, enlazando la interfaz con la lógica del backend y creando pruebas unitarias para la interfaz si es necesario.

---

## 📝 **Notas Adicionales**

- Asegúrate de configurar correctamente la base de datos antes de ejecutar la aplicación.
- Utiliza la colección de Postman para validar el funcionamiento de los endpoints.
- Si encuentras algún problema, abre un **issue** en el repositorio.
