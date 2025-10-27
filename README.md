# RentaCarMVC

Proyecto académico desarrollado en Java que implementa un sistema de gestión de alquiler de vehículos bajo arquitectura MVC.
Incluye separación en paquetes, modelos orientados a objetos con herencia, controladores con operaciones CRUD mediante JDBC y
vistas construidas con Swing (JFrame). Toda la capa de datos respeta los nombres de tablas y columnas oficiales de la base de
datos `rentacar`.

## Estructura del proyecto

```
src/
 ├── Main.java
 ├── conexion/
 │   └── Conexion.java
 ├── modelo/
 │   ├── Alquiler.java
 │   ├── Cliente.java
 │   ├── Factura.java
 │   ├── HistorialCliente.java
 │   ├── MantenimientoVehiculo.java
 │   ├── Marca.java
 │   ├── Modelo.java
 │   ├── Pago.java
 │   ├── Reserva.java
 │   ├── Rol.java
 │   ├── TipoMantenimiento.java
 │   ├── Trabajador.java
 │   ├── Usuario.java
 │   └── Vehiculo.java
 ├── controlador/
 │   ├── ClienteController.java
 │   ├── ReservaController.java
 │   └── VehiculoController.java
 └── vista/
     ├── FrmClientes.java
     ├── FrmPrincipal.java
     ├── FrmReservas.java
     └── FrmVehiculos.java
```

## Requisitos previos

- JDK 17 o superior
- NetBeans 15+ (opcional, pero recomendado para edición visual)
- Servidor MySQL en ejecución con la base de datos `rentacar`
- Driver JDBC de MySQL en el classpath del proyecto

## Configuración de la base de datos

La clase `conexion.Conexion` contiene las credenciales para conectarse a la base de datos:

```java
private final String bd = "rentacar";
private final String url = "jdbc:mysql://127.0.0.1:3306/";
private final String user = "ian";
private final String password = "BN/A7R3.pkLtUOYB";
```

Asegúrate de que la base de datos y las tablas existan con la estructura esperada antes de ejecutar la aplicación. Los
controladores incluyen operaciones CRUD para las tablas `USUARIOS`, `CLIENTES_INFO`, `VEHICULOS` y `RESERVAS`, utilizando los
campos exactos definidos en la base (por ejemplo `fecha_ultima_modificacion`, `numero_licencia`, `tarifa_diaria` o
`estado_reserva`).

El script `database/rentacar_schema.sql` contiene la definición completa de las 14 tablas solicitadas con sus llaves primarias y
foráneas, listo para ejecutarse en MySQL.

## Ejecución

1. Importa el proyecto en NetBeans como proyecto Java con Ant.
2. Verifica que el driver de MySQL (`mysql-connector-j`) esté referenciado en las librerías del proyecto.
3. Ejecuta la clase `Main`. Se abrirá el formulario `FrmPrincipal` y desde él podrás acceder a los módulos de Clientes, Vehículos y Reservas.

## Notas

- Las clases modelo implementan encapsulamiento mediante atributos privados y métodos `getter/setter`.
- `Cliente` y `Trabajador` extienden de `Usuario`, demostrando herencia y polimorfismo a través del método `resumenPerfil()`.
- El código fuente está comentado para facilitar su lectura y mantenimiento.
