# 📚 API REST de Usuarios y Notas

---
## 📜 **Descripción**

**API REST en Java + Spring Boot** que permite gestionar **👤 Usuarios** y sus **📄 Notas** de forma persistente, usando **JPA** y siguiendo buenas prácticas arquitectónicas.

---
## 🛠️ **Entidades y relaciones**

#### 👤 **Usuario:**
- `id` (Long, autogenerado)
- `nombre` (String)
- `email` (String, único)
- `passwordHash` (String)
##### 🔗 **Relación:**
- Un Usuario puede tener muchas **Notas**
- Al guardar/borrar un Usuario, sus notas deben guardarse/borrarse automáticamente: `cascade = ALL`
- Si una nota queda huérfana, debe eliminarse: `orphanRemoval = true`
#### 📄 **Nota:**
- `id` (Long, autogenerado)
- `titulo` (String)
- `contenido` (texto largo)
- `fechaCreacion` (fecha y hora)
##### 🔗 **Relación:**
- Cada nota pertenece a un único usuario.

---
## ⚙️ **Estructura**

```txt
com.miapp.notasapi/  
├── controller/ # Controladores REST  
│   ├── UsuarioController.java # Endpoints de usuario
│   └── NotaController.java # Endpoints de nota
│
├── service/ # Lógica de negocio  
│   ├── UsuarioService.java
│   └── NotaService.java
│
├── repository/ # Acceso a datos  
│   ├── UsuarioRepository.java
│   └── NotaRepository.java
│
├── model/ # Entidades Autor y Libro  
│   ├── Usuario.java # Entidad usuario
│   └── Nota.java # Entidad nota
│
├── resources/ # Configuración
│   └── application.properties
│
├── README.md
└── test/ # Pruebas del sistema
```
