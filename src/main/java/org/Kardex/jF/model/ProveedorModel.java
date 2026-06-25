package org.Kardex.jF.model;

import java.util.*;
import org.Kardex.jF.bean.entity.Proveedor;
import org.Kardex.jF.usecase.CRUDUsecase;

public class ProveedorModel implements CRUDUsecase<Proveedor> {
    public boolean insertar(Proveedor p) { return false; }
    public List<Proveedor> listar() { return new ArrayList<>(); }
    public boolean actualizar(Proveedor p) { return false; }
    public boolean eliminar(Integer id) { return false; }
}
