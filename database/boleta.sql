CREATE TABLE IF NOT EXISTS cliente (
    id_cliente SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    telefono BIGINT,
    correo VARCHAR(100),
    direccion VARCHAR(200),
    tipo_cliente VARCHAR(50) NOT NULL DEFAULT 'Natural',
    ruc BIGINT
);


CREATE TABLE IF NOT EXISTS categoria (
    id_categoria SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS producto (
    id_producto SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(200) NOT NULL,
    presentacion VARCHAR(120),
    unidad_medida VARCHAR(50),
    precio_compra NUMERIC(10, 2) NOT NULL DEFAULT 0,
    precio_minorista NUMERIC(10, 2) NOT NULL DEFAULT 0,
    precio_mayorista NUMERIC(10, 2) NOT NULL DEFAULT 0,
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS servicio (
    id_servicio SERIAL PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    servicio VARCHAR(200) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS repuesto (
    id_repuesto SERIAL PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    marca VARCHAR(80),
    categoria VARCHAR(80) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    precio_compra NUMERIC(10, 2) NOT NULL DEFAULT 0,
    precio_venta NUMERIC(10, 2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario SERIAL PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL
);

-- 2. Tablas relacionadas con clientes, servicios y repuestos
CREATE TABLE IF NOT EXISTS equipo (
    id_equipo SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(80),
    modelo VARCHAR(80),
    numero_serie VARCHAR(80),
    tipo_equipo VARCHAR(80),
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    id_cliente INTEGER NOT NULL,

    CONSTRAINT fk_equipo_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS cliente_servicio (
    id_cliente_servicio SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    id_servicio INTEGER NOT NULL,
    numero_orden VARCHAR(30) NOT NULL,
    equipo VARCHAR(150),
    falla TEXT,
    estado VARCHAR(30) NOT NULL DEFAULT 'Pendiente',
    precio_unitario NUMERIC(10, 2) NOT NULL DEFAULT 0,
    facturado BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_aplicacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_facturacion TIMESTAMP,

    CONSTRAINT fk_cliente_servicio_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    CONSTRAINT fk_cliente_servicio_servicio
        FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
);

select * from cliente_servicio;

CREATE TABLE IF NOT EXISTS movimiento_inventario (
    id_movimiento SERIAL PRIMARY KEY,
    id_repuesto INTEGER NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    cantidad INTEGER NOT NULL,
    motivo VARCHAR(200),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_movimiento_repuesto
        FOREIGN KEY (id_repuesto) REFERENCES repuesto(id_repuesto)
);

CREATE TABLE IF NOT EXISTS boleta (
    id_boleta SERIAL PRIMARY KEY,
    numero VARCHAR(30) NOT NULL UNIQUE,
    tipo_comprobante VARCHAR(20) NOT NULL,
    id_cliente INTEGER NOT NULL,
    dni_ruc VARCHAR(20),
    metodo_pago VARCHAR(40) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal NUMERIC(10, 2) NOT NULL DEFAULT 0,
    igv NUMERIC(10, 2) NOT NULL DEFAULT 0,
    total NUMERIC(10, 2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'Pagado',

    CONSTRAINT fk_boleta_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS boleta_detalle (
    id_boleta_detalle SERIAL PRIMARY KEY,
    id_boleta INTEGER NOT NULL,
    tipo_item VARCHAR(20) NOT NULL,
    descripcion TEXT NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(10, 2) NOT NULL DEFAULT 0,
    importe NUMERIC(10, 2) NOT NULL DEFAULT 0,
    id_cliente_servicio INTEGER,
    id_repuesto INTEGER,

    CONSTRAINT fk_boleta_detalle_boleta
        FOREIGN KEY (id_boleta) REFERENCES boleta(id_boleta) ON DELETE CASCADE,
    CONSTRAINT fk_boleta_detalle_cliente_servicio
        FOREIGN KEY (id_cliente_servicio) REFERENCES cliente_servicio(id_cliente_servicio),
    CONSTRAINT fk_boleta_detalle_repuesto
        FOREIGN KEY (id_repuesto) REFERENCES repuesto(id_repuesto)
);

-- 3. Índices simples para búsquedas comunes
CREATE INDEX IF NOT EXISTS idx_cliente_servicio_cliente ON cliente_servicio(id_cliente);
CREATE INDEX IF NOT EXISTS idx_cliente_servicio_facturado ON cliente_servicio(facturado);
CREATE INDEX IF NOT EXISTS idx_repuesto_codigo ON repuesto(codigo);
CREATE INDEX IF NOT EXISTS idx_repuesto_estado ON repuesto(estado);
CREATE INDEX IF NOT EXISTS idx_movimiento_repuesto ON movimiento_inventario(id_repuesto);
CREATE INDEX IF NOT EXISTS idx_boleta_cliente ON boleta(id_cliente);
CREATE INDEX IF NOT EXISTS idx_boleta_fecha ON boleta(fecha);
CREATE INDEX IF NOT EXISTS idx_boleta_detalle_boleta ON boleta_detalle(id_boleta);

INSERT INTO usuario (usuario, contraseña)
VALUES ('admin', '123456')
ON CONFLICT (usuario) DO NOTHING;

-- CLIENTES
INSERT INTO cliente
(codigo, nombre, apellido, telefono, correo, direccion, tipo_cliente, ruc)
VALUES
('C001','Juan','Pérez',987654321,'juan@gmail.com','Av. Los Incas 123','Natural',NULL),
('C002','María','Quispe',912345678,'maria@gmail.com','Av. Cultura 456','Natural',NULL),
('C003','Carlos','Huamán',923456789,'carlos@gmail.com','Urb. Magisterio','Natural',NULL),
('C004','Tech Solutions SAC',NULL,984567123,'ventas@techsolutions.com','Parque Industrial','Jurídico',20601234567),
('C005','Innova Perú SAC',NULL,976543210,'contacto@innova.com','Av. El Sol 789','Jurídico',20598765432);


-- CATEGORÍAS
INSERT INTO categoria
(codigo, nombre, descripcion, estado)
VALUES
('CAT001','Golosinas','Dulces, caramelos y productos de confitería','Activo'),
('CAT002','Bebidas','Bebidas embotelladas y en pack','Activo'),
('CAT003','Galletas','Galletas dulces y saladas','Activo'),
('CAT004','Snacks','Piqueos, chips y productos similares','Activo')
ON CONFLICT (codigo) DO NOTHING;

-- PRODUCTOS
INSERT INTO producto
(codigo, nombre, categoria, presentacion, unidad_medida, precio_compra, precio_minorista, precio_mayorista, stock_actual, stock_minimo, estado)
VALUES
('CAR001','Caramelos surtidos','Golosinas','Bolsa 150 unidades','Bolsa',5.50,7.50,6.80,40,10,'Activo'),
('CHO001','Chocolate multipack','Golosinas','Caja','Caja',14.00,18.00,16.50,25,8,'Activo'),
('GAL001','Galletas vainilla','Golosinas','Paquete 6 unidades','Paquete',3.80,5.00,4.50,60,15,'Activo'),
('BEB001','Gaseosa personal','Bebidas','Pack','Pack',20.00,24.00,22.50,30,10,'Activo');

-- SERVICIOS
INSERT INTO servicio
(codigo, servicio, precio, estado)
VALUES
('S001','Mantenimiento Preventivo',50.00,'Activo'),
('S002','Formateo e Instalación de SO',80.00,'Activo'),
('S003','Cambio de Pantalla Laptop',250.00,'Activo'),
('S004','Limpieza Interna de PC',40.00,'Activo'),
('S005','Diagnóstico Técnico',30.00,'Activo');
-- REPUESTOS
INSERT INTO repuesto
(codigo, nombre, marca, categoria, stock, precio_compra, precio_venta, estado)
VALUES
('R001','Disco SSD 240GB','Kingston','Almacenamiento',20,90.00,120.00,'Activo'),
('R002','Memoria RAM 8GB DDR4','Corsair','Memoria',15,80.00,110.00,'Activo'),
('R003','Fuente Poder 500W','Thermaltake','Fuente',10,120.00,160.00,'Activo'),
('R004','Ventilador CPU','Cooler Master','Refrigeración',25,20.00,35.00,'Activo'),
('R005','Pantalla Laptop 15.6','HP','Pantallas',8,180.00,250.00,'Activo');

-- EQUIPOS
INSERT INTO equipo
(codigo, marca, modelo, numero_serie, tipo_equipo, estado, fecha_ingreso, id_cliente)
VALUES
('E001','HP','15-DW3000','HP123456','Laptop',TRUE,CURRENT_DATE,1),
('E002','Lenovo','IdeaPad 3','LN654321','Laptop',TRUE,CURRENT_DATE,2),
('E003','Dell','OptiPlex 7090','DL456789','PC Escritorio',TRUE,CURRENT_DATE,3),
('E004','Asus','ROG Strix','AS741258','Laptop',TRUE,CURRENT_DATE,1),
('E005','Acer','Aspire 5','AC852963','Laptop',TRUE,CURRENT_DATE,2);

-- ÓRDENES / CLIENTE_SERVICIO
INSERT INTO cliente_servicio
(id_cliente, id_servicio, numero_orden, equipo, falla, estado, precio_unitario)
VALUES
(1,1,'OS001','HP 15-DW3000','Equipo lento','Recibido',50.00),
(2,2,'OS002','Lenovo IdeaPad 3','Sistema operativo dañado','Reparación',80.00),
(3,5,'OS003','Dell OptiPlex 7090','No enciende','Listo',30.00),
(1,4,'OS004','Asus ROG Strix','Acumulación de polvo','Entregado',40.00),
(2,3,'OS005','Acer Aspire 5','Pantalla rota','Reparación',250.00);

-- MOVIMIENTOS DE INVENTARIO
INSERT INTO movimiento_inventario
(id_repuesto, tipo, cantidad, motivo)
VALUES
(1,'ENTRADA',10,'Compra proveedor'),
(2,'ENTRADA',15,'Compra proveedor'),
(3,'SALIDA',2,'Reparación equipo'),
(4,'SALIDA',5,'Mantenimiento'),
(5,'ENTRADA',3,'Reposición stock');

-- BOLETAS
INSERT INTO boleta
(numero, tipo_comprobante, id_cliente, dni_ruc, metodo_pago, subtotal, igv, total)
VALUES
('B001','Boleta',1,'76543210','Efectivo',50.00,9.00,59.00),
('B002','Boleta',2,'71234567','Yape',80.00,14.40,94.40),
('B003','Boleta',3,'72345678','Tarjeta',30.00,5.40,35.40),
('B004','Boleta',1,'76543210','Efectivo',40.00,7.20,47.20),
('B005','Factura',4,'20601234567','Transferencia',250.00,45.00,295.00);

-- DETALLE BOLETA
INSERT INTO boleta_detalle
(id_boleta, tipo_item, descripcion, cantidad, precio_unitario, importe, id_cliente_servicio, id_repuesto)
VALUES
(1,'SERVICIO','Mantenimiento Preventivo',1,50.00,50.00,1,NULL),
(2,'SERVICIO','Formateo e Instalación de SO',1,80.00,80.00,2,NULL),
(3,'SERVICIO','Diagnóstico Técnico',1,30.00,30.00,3,NULL),
(4,'SERVICIO','Limpieza Interna de PC',1,40.00,40.00,4,NULL),
(5,'SERVICIO','Cambio de Pantalla Laptop',1,250.00,250.00,5,5);