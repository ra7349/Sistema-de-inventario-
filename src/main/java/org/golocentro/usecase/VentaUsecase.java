package org.golocentro.usecase;

import org.golocentro.bean.entity.Venta;

public interface VentaUsecase extends CRUDUsecase<Venta> {
    String generarNumeroVenta(String tipoComprobante);
}
