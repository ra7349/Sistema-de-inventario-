package org.Kardex.jF.usecase;

import java.util.List;

public interface CRUDUsecase<T> {
    boolean insertar(T obj);
    List<T> listar();
    boolean actualizar(T obj);
    boolean eliminar(Integer id);
    
}
