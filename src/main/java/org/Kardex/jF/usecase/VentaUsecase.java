package org.Kardex.jF.usecase;

import org.Kardex.jF.bean.entity.Venta;

public interface VentaUsecase extends CRUDUsecase<Venta> {
    String generarNumeroVenta(String tipoComprobante);
}
