-- StockFácil: base de datos completa (semana 6)
DROP DATABASE IF EXISTS stockfacil;
CREATE DATABASE stockfacil CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE stockfacil;

CREATE TABLE rol (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre     VARCHAR(100) NOT NULL,
  usuario    VARCHAR(30)  NOT NULL UNIQUE,
  clave      VARCHAR(60)  NOT NULL,
  id_rol     INT          NOT NULL,
  activo     TINYINT(1)   NOT NULL DEFAULT 1,
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE categoria (
  id_categoria INT AUTO_INCREMENT PRIMARY KEY,
  nombre       VARCHAR(60)  NOT NULL UNIQUE,
  descripcion  VARCHAR(200),
  activo       TINYINT(1)   NOT NULL DEFAULT 1
);

CREATE TABLE producto (
  id_producto  INT AUTO_INCREMENT PRIMARY KEY,
  codigo       VARCHAR(20)   NOT NULL UNIQUE,
  nombre       VARCHAR(100)  NOT NULL,
  id_categoria INT           NOT NULL,
  precio       DECIMAL(10,2) NOT NULL,
  stock        INT           NOT NULL DEFAULT 0,
  stock_minimo INT           NOT NULL DEFAULT 5,
  activo       TINYINT(1)    NOT NULL DEFAULT 1,
  CHECK (precio >= 0),
  CHECK (stock >= 0),
  CHECK (stock_minimo >= 0),
  FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE movimiento (
  id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
  id_producto   INT      NOT NULL,
  tipo          ENUM('ENTRADA','SALIDA') NOT NULL,
  cantidad      INT      NOT NULL,
  fecha         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_usuario    INT      NOT NULL,
  nota          VARCHAR(200),
  CHECK (cantidad > 0),
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  FOREIGN KEY (id_usuario)  REFERENCES usuario(id_usuario)
);

-- Datos de prueba
INSERT INTO rol (nombre) VALUES ('ADMIN'), ('OPERADOR');

-- Claves: admin123 y operador123 (cifradas con BCrypt)
INSERT INTO usuario (nombre, usuario, clave, id_rol) VALUES
('Administradora', 'admin',    '$2a$10$Z1Lday4CGQtPLR7Or0rbWeDaymUbV3AzJ/K68OOOLnTNTWDC.pEQS', 1),
('Operador de bodega', 'operador', '$2a$10$/nJTnqS3MMLtHfjr6XFcuOWaBDtuKWub70rtBOpIdCWW7hjlYrx9S', 2);

INSERT INTO categoria (nombre, descripcion) VALUES
('Bebidas', 'Refrescos, jugos y agua'),
('Abarrotes', 'Arroz, frijoles, aceite y granos'),
('Limpieza', 'Productos de limpieza del hogar'),
('Snacks', 'Galletas, papas y confites');

INSERT INTO producto (codigo, nombre, id_categoria, precio, stock, stock_minimo) VALUES
('BEB-001', 'Agua 600 ml',          1,  500.00, 40, 10),
('BEB-002', 'Refresco cola 2 L',    1, 1500.00,  6, 8),
('ABA-001', 'Arroz 1 kg',           2, 1200.00, 25, 10),
('ABA-002', 'Frijoles negros 800 g',2, 1350.00,  4, 6),
('ABA-003', 'Aceite 900 ml',        2, 2100.00, 12, 5),
('LIM-001', 'Jabón líquido 1 L',    3, 1800.00,  9, 4),
('LIM-002', 'Cloro 1 L',            3,  950.00,  2, 5),
('SNA-001', 'Galletas de vainilla', 4,  700.00, 30, 10);

INSERT INTO movimiento (id_producto, tipo, cantidad, fecha, id_usuario, nota) VALUES
(1, 'ENTRADA', 48, '2026-10-01 09:00:00', 1, 'Compra inicial'),
(1, 'SALIDA',   8, '2026-10-03 15:20:00', 2, 'Venta'),
(2, 'ENTRADA', 12, '2026-10-01 09:10:00', 1, 'Compra inicial'),
(2, 'SALIDA',   6, '2026-10-05 11:00:00', 2, 'Venta'),
(7, 'ENTRADA', 10, '2026-10-02 08:30:00', 1, 'Compra inicial'),
(7, 'SALIDA',   8, '2026-10-06 16:45:00', 2, 'Venta'),
(3, 'ENTRADA', 25, '2026-10-01 09:20:00', 1, 'Compra inicial'),
(4, 'ENTRADA',  4, '2026-10-01 09:25:00', 1, 'Compra inicial'),
(5, 'ENTRADA', 12, '2026-10-02 08:40:00', 1, 'Compra inicial'),
(6, 'ENTRADA',  9, '2026-10-02 08:45:00', 1, 'Compra inicial'),
(8, 'ENTRADA', 30, '2026-10-02 08:50:00', 1, 'Compra inicial');
