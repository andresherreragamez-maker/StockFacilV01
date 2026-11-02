USE stockfacil;
CREATE TABLE proveedor (
  id_proveedor    INT AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(100) NOT NULL,
  cedula_juridica VARCHAR(20)  NOT NULL UNIQUE,
  telefono        CHAR(8),
  correo          VARCHAR(100),
  activo          TINYINT(1)   NOT NULL DEFAULT 1
);

INSERT INTO proveedor (nombre, cedula_juridica, telefono, correo) VALUES
('Distribuidora del Valle', '3-101-123456', '22223333', 'ventas@delvalle.cr'),
('Abarrotes Mayoreo Central', '3-101-654321', '24445555', 'pedidos@mayoreo.cr'),
('Limpieza Total', '3-102-111222', '26667777', 'info@limpiezatotal.cr');
