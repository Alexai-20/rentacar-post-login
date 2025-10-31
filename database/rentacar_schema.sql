CREATE DATABASE IF NOT EXISTS trabajo;
USE trabajo;

CREATE TABLE IF NOT EXISTS ROLES (
    id_rol INT PRIMARY KEY AUTO_INCREMENT,
    nombre_rol VARCHAR(50) NOT NULL,
    descripcion TEXT,
    permisos TEXT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS USUARIOS (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    rut VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(100),
    fecha_nacimiento DATE,
    tipo_usuario VARCHAR(20) NOT NULL,
    estado VARCHAR(12) NOT NULL,
    fecha_registro DATE NOT NULL,
    fecha_ultima_modificacion DATE NOT NULL,
    contrasena_hash VARCHAR(128)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS CLIENTES_INFO (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    numero_licencia VARCHAR(30),
    fecha_vencimiento_licencia DATE,
    tipo_cliente VARCHAR(20),
    empresa VARCHAR(100),
    CONSTRAINT fk_clientes_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIOS (id_usuario)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS TRABAJADORES_INFO (
    id_trabajador INT PRIMARY KEY,
    cargo VARCHAR(50) NOT NULL,
    departamento VARCHAR(50),
    fecha_contratacion DATE,
    salario DECIMAL(10,2),
    CONSTRAINT fk_trabajador_usuario FOREIGN KEY (id_trabajador)
        REFERENCES USUARIOS (id_usuario)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS MARCAS (
    id_marca INT PRIMARY KEY AUTO_INCREMENT,
    nombre_marca VARCHAR(50) NOT NULL,
    pais_origen VARCHAR(50)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS MODELOS (
    id_modelo INT PRIMARY KEY AUTO_INCREMENT,
    id_marca INT NOT NULL,
    nombre_modelo VARCHAR(50) NOT NULL,
    CONSTRAINT fk_modelo_marca FOREIGN KEY (id_marca)
        REFERENCES MARCAS (id_marca)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS VEHICULOS (
    patente VARCHAR(6) PRIMARY KEY,
    id_modelo INT NOT NULL,
    `año` DATE,
    tipo_combustible VARCHAR(15),
    kilometraje INT,
    color VARCHAR(30),
    numero_asientos INT,
    tipo_vehiculo VARCHAR(20),
    tarifa_diaria DECIMAL(10,2),
    estado_mantenimiento VARCHAR(25),
    disponibilidad VARCHAR(12),
    fecha_registro DATE,
    fecha_ultima_revision DATE,
    CONSTRAINT fk_vehiculo_modelo FOREIGN KEY (id_modelo)
        REFERENCES MODELOS (id_modelo)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS TIPOS_MANTENIMIENTO (
    id_mantenimiento INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    kilometraje_recomendado INT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS MANTENIMIENTOS_VEHICULOS (
    id_mantenimiento INT PRIMARY KEY AUTO_INCREMENT,
    patente VARCHAR(6) NOT NULL,
    id_tipo_mantenimiento INT NOT NULL,
    costo DECIMAL(10,2),
    fecha_mantenimiento DATE,
    kilometraje_mantenimiento INT,
    proximo_mantenimiento_km INT,
    proximo_mantenimiento_fecha DATE,
    id_trabajador INT,
    CONSTRAINT fk_mant_vehiculo FOREIGN KEY (patente)
        REFERENCES VEHICULOS (patente)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_mant_tipo FOREIGN KEY (id_tipo_mantenimiento)
        REFERENCES TIPOS_MANTENIMIENTO (id_mantenimiento)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_mant_trabajador FOREIGN KEY (id_trabajador)
        REFERENCES TRABAJADORES_INFO (id_trabajador)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS RESERVAS (
    id_reserva INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    patente VARCHAR(6) NOT NULL,
    fecha_reserva DATE NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado_reserva VARCHAR(20) NOT NULL,
    monto_estimado DECIMAL(10,2),
    id_trabajador INT,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (id_cliente)
        REFERENCES CLIENTES_INFO (id_cliente)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_reserva_vehiculo FOREIGN KEY (patente)
        REFERENCES VEHICULOS (patente)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_trabajador FOREIGN KEY (id_trabajador)
        REFERENCES TRABAJADORES_INFO (id_trabajador)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ALQUILER (
    id_alquiler INT PRIMARY KEY AUTO_INCREMENT,
    id_reserva INT NOT NULL,
    fecha_inicio DATE,
    fecha_fin_programada DATE,
    fecha_fin_real DATE,
    kilometraje_inicial INT,
    kilometraje_final INT,
    estado_alquiler VARCHAR(25),
    monto_total DECIMAL(12,2),
    dias_retraso INT,
    cargo_retraso DECIMAL(10,2),
    id_trabajador_entrega INT,
    id_trabajador_devolucion INT,
    CONSTRAINT fk_alquiler_reserva FOREIGN KEY (id_reserva)
        REFERENCES RESERVAS (id_reserva)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_alquiler_trabajador_entrega FOREIGN KEY (id_trabajador_entrega)
        REFERENCES TRABAJADORES_INFO (id_trabajador)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_alquiler_trabajador_devolucion FOREIGN KEY (id_trabajador_devolucion)
        REFERENCES TRABAJADORES_INFO (id_trabajador)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS FACTURA (
    id_factura INT PRIMARY KEY AUTO_INCREMENT,
    id_alquiler INT NOT NULL,
    numero_factura VARCHAR(30) NOT NULL,
    fecha_emision DATE,
    subtotal DECIMAL(12,2),
    impuestos DECIMAL(12,2),
    monto_total DECIMAL(12,2),
    estado_pago VARCHAR(20),
    fecha_vencimiento DATE,
    CONSTRAINT fk_factura_alquiler FOREIGN KEY (id_alquiler)
        REFERENCES ALQUILER (id_alquiler)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS PAGOS (
    id_pago INT PRIMARY KEY AUTO_INCREMENT,
    id_factura INT NOT NULL,
    monto_pago DECIMAL(12,2),
    metodo_pago VARCHAR(30),
    fecha_pago DATE,
    numero_transaccion VARCHAR(50),
    id_trabajador INT,
    CONSTRAINT fk_pago_factura FOREIGN KEY (id_factura)
        REFERENCES FACTURA (id_factura)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_pago_trabajador FOREIGN KEY (id_trabajador)
        REFERENCES TRABAJADORES_INFO (id_trabajador)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS HISTORIAL_CLIENTE (
    id_historial INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_alquiler INT NOT NULL,
    valoracion INT,
    fecha_registro DATE,
    CONSTRAINT fk_historial_cliente FOREIGN KEY (id_cliente)
        REFERENCES CLIENTES_INFO (id_cliente)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_historial_alquiler FOREIGN KEY (id_alquiler)
        REFERENCES ALQUILER (id_alquiler)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO USUARIOS (nombre, apellido, rut, email, telefono, direccion,
    fecha_nacimiento, tipo_usuario, estado, fecha_registro,
    fecha_ultima_modificacion, contrasena_hash)
SELECT 'Administrador', 'General', '11.111.111-1', 'admin@rentacar.cl',
       '+56 9 1234 5678', 'Casa Matriz 123', DATE('1990-01-01'),
       'ADMINISTRADOR', 'ACTIVO', CURDATE(), CURDATE(), SHA2('admin123', 256)
WHERE NOT EXISTS (
    SELECT 1 FROM USUARIOS WHERE email = 'admin@rentacar.cl'
);

INSERT INTO TRABAJADORES_INFO (id_trabajador, cargo, departamento,
    fecha_contratacion, salario)
SELECT u.id_usuario, 'Administrador General', 'Sistemas', CURDATE(), 0
FROM USUARIOS u
WHERE u.email = 'admin@rentacar.cl'
  AND NOT EXISTS (
      SELECT 1 FROM TRABAJADORES_INFO t WHERE t.id_trabajador = u.id_usuario
  );
