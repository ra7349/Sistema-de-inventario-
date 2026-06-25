-- Base de datos Proyecto - Sistema de Inventario
-- Script adaptado a las entidades y modelos Java del proyecto Kardex.
-- Incluye datos de prueba para trabajar con clientes, proveedores, productos,
-- movimientos, ventas, detalles y precios por tipo de cliente.

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
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(40) NOT NULL UNIQUE,
    descripcion VARCHAR(160),
    descuento_porcentaje NUMERIC(5,2) NOT NULL DEFAULT 0,
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
    precio_unitario NUMERIC(10,2) GENERATED ALWAYS AS (precio) STORED,
    unidad_venta VARCHAR(40),
    fecha_inicio DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_fin DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
    CONSTRAINT uq_precio_producto_tipo UNIQUE (id_producto, tipo_cliente, fecha_inicio)
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

INSERT INTO tipo_cliente (codigo, nombre, descripcion, descuento_porcentaje, estado) VALUES
('TC001', 'Minorista', 'Cliente de compra al detalle', 0.00, 'Activo'),
('TC002', 'Mayorista', 'Cliente de compra por volumen', 12.00, 'Activo'),
('TC003', 'Distribuidor', 'Cliente distribuidor con descuentos especiales', 18.00, 'Activo'),
('TC004', 'Corporativo', 'Empresas con compras programadas', 15.00, 'Activo'),
('TC005', 'Preferente', 'Cliente frecuente de la tienda', 5.00, 'Activo'),
('TC006', 'Online', 'Cliente captado por canales digitales', 3.00, 'Activo'),
('TC007', 'Institucional', 'Colegios, municipios u organizaciones', 10.00, 'Activo'),
('TC008', 'Eventual', 'Cliente ocasional sin beneficios permanentes', 0.00, 'Activo');

INSERT INTO cliente (codigo, nombre, apellido, telefono, correo, direccion, tipo_cliente, ruc) VALUES
('C001', 'María', 'Quispe', 987654321, 'maria.quispe@example.com', 'Av. Los Dulces 120', 'Minorista', NULL),
('C002', 'Bodega San Martín', '', 987111222, 'compras@bodegasanmartin.pe', 'Jr. Comercio 455', 'Mayorista', 20500111222),
('C003', 'Carlos', 'Ramírez', 986222333, 'carlos.ramirez@example.com', 'Calle Central 88', 'Minorista', NULL),
('C004', 'Distribuidora El Sol', '', 985333444, 'pedidos@elsol.pe', 'Av. Mercado 700', 'Distribuidor', 20600999888),
('C005', 'Lucía', 'Fernández', 984444555, 'lucia.fernandez@example.com', 'Pasaje Las Flores 214', 'Preferente', NULL),
('C006', 'Eventos Dulce Fiesta', '', 983555666, 'contacto@dulcefiesta.pe', 'Av. Celebración 321', 'Corporativo', 20456789123),
('C007', 'Colegio San José', '', 982666777, 'compras@sanjose.edu.pe', 'Jr. Educación 101', 'Institucional', 20123456789),
('C008', 'Pedro', 'Salas', 981777888, 'pedro.salas@example.com', 'Mz. A Lt. 9', 'Online', NULL);

INSERT INTO categoria (codigo, nombre, descripcion, estado) VALUES
('CAT001', 'Caramelos', 'Caramelos surtidos y confites', 'Activo'),
('CAT002', 'Chocolates', 'Chocolates en barra y bombones', 'Activo'),
('CAT003', 'Galletas', 'Galletas dulces y rellenas', 'Activo'),
('CAT004', 'Gomitas', 'Gomitas frutales y ácidas', 'Activo'),
('CAT005', 'Bebidas', 'Bebidas azucaradas embotelladas', 'Activo'),
('CAT006', 'Snacks', 'Piqueos salados y mixtos', 'Activo'),
('CAT007', 'Panetones', 'Productos de campaña navideña', 'Activo'),
('CAT008', 'Helados', 'Postres helados y paletas', 'Activo');

INSERT INTO proveedor (codigo, razon_social, ruc, telefono, correo, direccion, contacto, estado) VALUES
('P001', 'Dulces Andinos S.A.C.', 20444555666, 984100200, 'ventas@dulcesandinos.pe', 'Av. Industria 150', 'Rosa Paredes', 'Activo'),
('P002', 'ChocoPerú Distribuciones', 20555666777, 984200300, 'pedidos@chocoperu.pe', 'Jr. Cacao 321', 'Luis Torres', 'Activo'),
('P003', 'Bebidas Golosas S.R.L.', 20666777888, 984300400, 'logistica@bebidasgolosas.pe', 'Av. Refrescos 980', 'Ana Salas', 'Activo'),
('P004', 'Galletas del Norte E.I.R.L.', 20411222333, 984400500, 'ventas@galletasnorte.pe', 'Calle Trigo 112', 'Mario León', 'Activo'),
('P005', 'Importadora Snack Mix S.A.', 20533444555, 984500600, 'comercial@snackmix.pe', 'Av. Almacenes 245', 'Cecilia Ríos', 'Activo'),
('P006', 'Helados del Valle S.A.C.', 20677888999, 984600700, 'pedidos@heladosvalle.pe', 'Jr. Frío 456', 'Jorge Castro', 'Activo'),
('P007', 'Panetones La Estrella S.A.C.', 20499888777, 984700800, 'campana@laestrella.pe', 'Av. Navidad 789', 'Patricia Vega', 'Activo'),
('P008', 'Confites Lima Market S.R.L.', 20588777666, 984800900, 'ventas@confiteslima.pe', 'Jr. Mayoristas 650', 'Diego Molina', 'Activo');

INSERT INTO producto (codigo, nombre, categoria, presentacion, unidad_medida, precio_compra, precio_minorista, precio_mayorista, stock_actual, stock_minimo, estado) VALUES
('PROD001', 'Caramelos surtidos Frutal', 'Caramelos', 'Bolsa 1 kg', 'bolsa', 8.50, 12.00, 10.20, 80, 15, 'Activo'),
('PROD002', 'Chocolate clásico', 'Chocolates', 'Caja x 24 unidades', 'caja', 28.00, 42.00, 36.00, 45, 10, 'Activo'),
('PROD003', 'Galletas vainilla', 'Galletas', 'Paquete x 12', 'paquete', 14.50, 22.00, 18.50, 65, 12, 'Activo'),
('PROD004', 'Gomitas frutales', 'Gomitas', 'Bolsa 500 g', 'bolsa', 6.80, 10.50, 8.90, 70, 15, 'Activo'),
('PROD005', 'Bebida azucarada cola', 'Bebidas', 'Botella 500 ml', 'botella', 1.80, 3.00, 2.40, 120, 24, 'Activo'),
('PROD006', 'Snack mixto crocante', 'Snacks', 'Caja x 20 unidades', 'caja', 18.00, 28.00, 23.50, 55, 12, 'Activo'),
('PROD007', 'Panetón clásico familiar', 'Panetones', 'Caja 900 g', 'unidad', 17.50, 27.00, 22.80, 38, 8, 'Activo'),
('PROD008', 'Paleta helada fresa', 'Helados', 'Caja x 30 unidades', 'caja', 24.00, 39.00, 33.00, 42, 10, 'Activo');

INSERT INTO precios_producto (id_producto, tipo_cliente, precio, unidad_venta) VALUES
(1, 'Minorista', 12.00, 'bolsa'), (1, 'Mayorista', 10.20, 'bolsa'),
(2, 'Minorista', 42.00, 'caja'), (2, 'Mayorista', 36.00, 'caja'),
(3, 'Minorista', 22.00, 'paquete'), (3, 'Mayorista', 18.50, 'paquete'),
(4, 'Minorista', 10.50, 'bolsa'), (4, 'Mayorista', 8.90, 'bolsa'),
(5, 'Minorista', 3.00, 'botella'), (5, 'Mayorista', 2.40, 'botella'),
(6, 'Minorista', 28.00, 'caja'), (6, 'Mayorista', 23.50, 'caja'),
(7, 'Minorista', 27.00, 'unidad'), (7, 'Mayorista', 22.80, 'unidad'),
(8, 'Minorista', 39.00, 'caja'), (8, 'Mayorista', 33.00, 'caja');

INSERT INTO movimiento_inventario (id_producto, tipo_movimiento, cantidad, motivo, fecha, observacion) VALUES
(1, 'Entrada', 80, 'Carga inicial', CURRENT_DATE - INTERVAL '7 days', 'Inventario inicial'),
(2, 'Entrada', 45, 'Carga inicial', CURRENT_DATE - INTERVAL '7 days', 'Inventario inicial'),
(3, 'Entrada', 65, 'Carga inicial', CURRENT_DATE - INTERVAL '6 days', 'Inventario inicial'),
(4, 'Entrada', 70, 'Carga inicial', CURRENT_DATE - INTERVAL '6 days', 'Inventario inicial'),
(5, 'Entrada', 120, 'Carga inicial', CURRENT_DATE - INTERVAL '5 days', 'Inventario inicial'),
(6, 'Entrada', 55, 'Carga inicial', CURRENT_DATE - INTERVAL '5 days', 'Inventario inicial'),
(7, 'Entrada', 38, 'Carga inicial', CURRENT_DATE - INTERVAL '4 days', 'Inventario inicial'),
(8, 'Entrada', 42, 'Carga inicial', CURRENT_DATE - INTERVAL '4 days', 'Inventario inicial');

INSERT INTO ventas (numero, tipo_comprobante, id_cliente, dni_ruc, metodo_pago, fecha, subtotal, igv, total, estado) VALUES
('B0001', 'Boleta', 1, NULL, 'Efectivo', CURRENT_TIMESTAMP - INTERVAL '6 days', 22.50, 4.05, 26.55, 'Pagado'),
('F0002', 'Factura', 2, '20500111222', 'Transferencia', CURRENT_TIMESTAMP - INTERVAL '5 days', 54.50, 9.81, 64.31, 'Pagado'),
('B0003', 'Boleta', 3, NULL, 'Yape', CURRENT_TIMESTAMP - INTERVAL '4 days', 25.00, 4.50, 29.50, 'Pagado'),
('F0004', 'Factura', 4, '20600999888', 'Transferencia', CURRENT_TIMESTAMP - INTERVAL '3 days', 56.50, 10.17, 66.67, 'Pagado'),
('B0005', 'Boleta', 5, NULL, 'Tarjeta', CURRENT_TIMESTAMP - INTERVAL '2 days', 30.00, 5.40, 35.40, 'Pagado'),
('F0006', 'Factura', 6, '20456789123', 'Transferencia', CURRENT_TIMESTAMP - INTERVAL '1 day', 83.50, 15.03, 98.53, 'Pagado'),
('F0007', 'Factura', 7, '20123456789', 'Efectivo', CURRENT_TIMESTAMP, 45.60, 8.21, 53.81, 'Pagado'),
('B0008', 'Boleta', 8, NULL, 'Plin', CURRENT_TIMESTAMP, 39.00, 7.02, 46.02, 'Pagado');

INSERT INTO detalle_ventas (id_venta, id_producto, descripcion, cantidad, precio_unitario, importe) VALUES
(1, 1, 'PROD001 - Caramelos surtidos Frutal', 1, 12.00, 12.00),
(1, 4, 'PROD004 - Gomitas frutales', 1, 10.50, 10.50),
(2, 2, 'PROD002 - Chocolate clásico', 1, 36.00, 36.00),
(2, 3, 'PROD003 - Galletas vainilla', 1, 18.50, 18.50),
(3, 5, 'PROD005 - Bebida azucarada cola', 4, 3.00, 12.00),
(3, 1, 'PROD001 - Caramelos surtidos Frutal', 1, 12.00, 12.00),
(4, 6, 'PROD006 - Snack mixto crocante', 1, 23.50, 23.50),
(4, 8, 'PROD008 - Paleta helada fresa', 1, 33.00, 33.00),
(5, 7, 'PROD007 - Panetón clásico familiar', 1, 27.00, 27.00),
(5, 5, 'PROD005 - Bebida azucarada cola', 1, 3.00, 3.00),
(6, 2, 'PROD002 - Chocolate clásico', 1, 36.00, 36.00),
(6, 8, 'PROD008 - Paleta helada fresa', 1, 33.00, 33.00),
(6, 6, 'PROD006 - Snack mixto crocante', 1, 14.50, 14.50),
(7, 7, 'PROD007 - Panetón clásico familiar', 2, 22.80, 45.60),
(8, 8, 'PROD008 - Paleta helada fresa', 1, 39.00, 39.00);
