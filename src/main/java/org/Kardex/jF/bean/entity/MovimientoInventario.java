package org.Kardex.jF.bean.entity;

import java.time.LocalDate;

public class MovimientoInventario {
    private String id;
    private String tipo;
    private Integer idProducto;
    private String codigoProducto;
    private String nombreProducto;
    private Integer cantidad;
    private String motivo;
    private LocalDate fecha;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Integer getIdRepuesto() { return idProducto; }
    public void setIdRepuesto(Integer idRepuesto) { this.idProducto = idRepuesto; }
    public String getCodigoRepuesto() { return codigoProducto; }
    public void setCodigoRepuesto(String codigoRepuesto) { this.codigoProducto = codigoRepuesto; }
    public String getNombreRepuesto() { return nombreProducto; }
    public void setNombreRepuesto(String nombreRepuesto) { this.nombreProducto = nombreRepuesto; }
}
