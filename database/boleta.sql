-- Base de datos Golocentro - Sistema de Inventario
-- Tablas del dominio: categorias, productos, clientes, tipos_cliente, proveedores,
-- movimientos_inventario, ventas, detalle_ventas y precios_producto.

DROP TABLE IF EXISTS detalle_ventas CASCADE;
DROP TABLE IF EXISTS ventas CASCADE;
DROP TABLE IF EXISTS precios_producto CASCADE;
DROP TABLE IF EXISTS movimiento_inventario CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS tipo_cliente CASCADE;
DROP TABLE IF EXISTS proveedor CASCADE;

CREATE TABLE tipo_cliente (
    id_tipo_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL UNIQUE,
    descripcion VARCHAR(160),
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    telefono BIGINT,
    correo VARCHAR(120),
    direccion VARCHAR(180),
    tipo_cliente VARCHAR(40) NOT NULL,
    ruc BIGINT,
    CONSTRAINT fk_cliente_tipo FOREIGN KEY (tipo_cliente) REFERENCES tipo_cliente(nombre)
);

CREATE TABLE categoria (
    id_categoria SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(180),
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE proveedor (
    id_proveedor SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    razon_social VARCHAR(140) NOT NULL,
    ruc BIGINT UNIQUE,
    telefono BIGINT,
    correo VARCHAR(120),
    direccion VARCHAR(180),
    contacto VARCHAR(100),
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE producto (
    id_producto SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    presentacion VARCHAR(80),
    unidad_medida VARCHAR(40),
    precio_compra NUMERIC(10,2) NOT NULL DEFAULT 0,
    precio_minorista NUMERIC(10,2) NOT NULL DEFAULT 0,
    precio_mayorista NUMERIC(10,2) NOT NULL DEFAULT 0,
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria) REFERENCES categoria(nombre)
);

CREATE TABLE precios_producto (
    id_precio_producto SERIAL PRIMARY KEY,
    id_producto INTEGER NOT NULL REFERENCES producto(id_producto),
    tipo_cliente VARCHAR(40) NOT NULL REFERENCES tipo_cliente(nombre),
    precio NUMERIC(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_fin DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE movimiento_inventario (
    id_movimiento SERIAL PRIMARY KEY,
    id_producto INTEGER NOT NULL REFERENCES producto(id_producto),
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    motivo VARCHAR(120),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    observacion VARCHAR(220)
);

CREATE TABLE ventas (
    id_venta SERIAL PRIMARY KEY,
    numero VARCHAR(30) NOT NULL UNIQUE,
    tipo_comprobante VARCHAR(30) NOT NULL,
    id_cliente INTEGER NOT NULL REFERENCES cliente(id_cliente),
    dni_ruc VARCHAR(20),
    metodo_pago VARCHAR(40) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal NUMERIC(10,2) NOT NULL DEFAULT 0,
    igv NUMERIC(10,2) NOT NULL DEFAULT 0,
    total NUMERIC(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'Pagado'
);

CREATE TABLE detalle_ventas (
    id_detalle_venta SERIAL PRIMARY KEY,
    id_venta INTEGER NOT NULL REFERENCES ventas(id_venta) ON DELETE CASCADE,
    id_producto INTEGER NOT NULL REFERENCES producto(id_producto),
    descripcion VARCHAR(180) NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL,
    importe NUMERIC(10,2) NOT NULL
);


CREATE INDEX idx_producto_categoria ON producto(categoria);
CREATE INDEX idx_movimiento_producto ON movimiento_inventario(id_producto);
CREATE INDEX idx_ventas_cliente ON ventas(id_cliente);
CREATE INDEX idx_detalle_ventas_producto ON detalle_ventas(id_producto);

INSERT INTO tipo_cliente (nombre, descripcion) VALUES
('Minorista', 'Clientes con compras por unidad'),
('Mayorista', 'Clientes con compras por volumen');

INSERT INTO cliente (codigo, nombre, apellido, telefono, correo, direccion, tipo_cliente, ruc) VALUES
('C001', 'María', 'Quispe', 987654321, 'maria.quispe@example.com', 'Av. Los Dulces 120', 'Minorista', NULL),
('C002', 'Bodega San Martín', '', 987111222, 'compras@bodegasanmartin.pe', 'Jr. Comercio 455', 'Mayorista', 20500111222),
('C003', 'Carlos', 'Ramírez', 986222333, 'carlos.ramirez@example.com', 'Calle Central 88', 'Minorista', NULL),
('C004', 'Distribuidora El Sol', '', 985333444, 'pedidos@elsol.pe', 'Av. Mercado 700', 'Mayorista', 20600999888);

INSERT INTO categoria (codigo, nombre, descripcion) VALUES
('CAT001', 'Caramelos', 'Caramelos surtidos y confites'),
('CAT002', 'Chocolates', 'Chocolates en barra y bombones'),
('CAT003', 'Galletas', 'Galletas dulces y rellenas'),
('CAT004', 'Gomitas', 'Gomitas frutales y ácidas'),
('CAT005', 'Bebidas', 'Bebidas azucaradas embotelladas');

INSERT INTO proveedor (codigo, razon_social, ruc, telefono, correo, direccion, contacto) VALUES
('P001', 'Dulces Andinos S.A.C.', 20444555666, 984100200, 'ventas@dulcesandinos.pe', 'Av. Industria 150', 'Rosa Paredes'),
('P002', 'ChocoPerú Distribuciones', 20555666777, 984200300, 'pedidos@chocoperu.pe', 'Jr. Cacao 321', 'Luis Torres'),
('P003', 'Bebidas Golosas S.R.L.', 20666777888, 984300400, 'logistica@bebidasgolosas.pe', 'Av. Refrescos 980', 'Ana Salas');

INSERT INTO producto (codigo, nombre, categoria, presentacion, unidad_medida, precio_compra, precio_minorista, precio_mayorista, stock_actual, stock_minimo) VALUES
('PROD001', 'Caramelos surtidos Frutal', 'Caramelos', 'Bolsa 1 kg', 'bolsa', 8.50, 12.00, 10.20, 80, 15),
('PROD002', 'Chocolate clásico', 'Chocolates', 'Caja x 24 unidades', 'caja', 28.00, 42.00, 36.00, 45, 10),
('PROD003', 'Galletas vainilla', 'Galletas', 'Paquete x 12', 'paquete', 14.50, 22.00, 18.50, 65, 12),
('PROD004', 'Gomitas frutales', 'Gomitas', 'Bolsa 500 g', 'bolsa', 6.80, 10.50, 8.90, 70, 15),
('PROD005', 'Bebida azucarada cola', 'Bebidas', 'Botella 500 ml', 'botella', 1.80, 3.00, 2.40, 120, 24);

INSERT INTO precios_producto (id_producto, tipo_cliente, precio) VALUES
(1, 'Minorista', 12.00), (1, 'Mayorista', 10.20),
(2, 'Minorista', 42.00), (2, 'Mayorista', 36.00),
(3, 'Minorista', 22.00), (3, 'Mayorista', 18.50),
(4, 'Minorista', 10.50), (4, 'Mayorista', 8.90),
(5, 'Minorista', 3.00), (5, 'Mayorista', 2.40);

INSERT INTO movimiento_inventario (id_producto, tipo_movimiento, cantidad, motivo, observacion) VALUES
(1, 'Entrada', 80, 'Carga inicial', 'Inventario inicial Golocentro'),
(2, 'Entrada', 45, 'Carga inicial', 'Inventario inicial Golocentro'),
(3, 'Entrada', 65, 'Carga inicial', 'Inventario inicial Golocentro'),
(4, 'Entrada', 70, 'Carga inicial', 'Inventario inicial Golocentro'),
(5, 'Entrada', 120, 'Carga inicial', 'Inventario inicial Golocentro');

INSERT INTO ventas (numero, tipo_comprobante, id_cliente, dni_ruc, metodo_pago, subtotal, igv, total) VALUES
('B0001', 'Boleta', 1, NULL, 'Efectivo', 22.50, 4.05, 26.55),
('F0002', 'Factura', 2, '20500111222', 'Transferencia', 54.50, 9.81, 64.31);

INSERT INTO detalle_ventas (id_venta, id_producto, descripcion, cantidad, precio_unitario, importe) VALUES
(1, 1, 'PROD001 - Caramelos surtidos Frutal', 1, 12.00, 12.00),
(1, 4, 'PROD004 - Gomitas frutales', 1, 10.50, 10.50),
(2, 2, 'PROD002 - Chocolate clásico', 1, 36.00, 36.00),
(2, 3, 'PROD003 - Galletas vainilla', 1, 18.50, 18.50);

