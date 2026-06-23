-- Tabla para guardar los servicios aplicados a cada cliente antes de la facturación.
CREATE TABLE cliente_servicio (
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

CREATE INDEX idx_cliente_servicio_cliente ON cliente_servicio(id_cliente);
CREATE INDEX idx_cliente_servicio_facturado ON cliente_servicio(facturado);
