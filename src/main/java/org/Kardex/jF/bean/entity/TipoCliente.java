package org.Kardex.jF.bean.entity;

public class TipoCliente {
    private Integer idTipoCliente;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double descuentoPorcentaje;

    public Integer getIdTipoCliente() { return idTipoCliente; }
    public void setIdTipoCliente(Integer idTipoCliente) { this.idTipoCliente = idTipoCliente; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(double descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }
}
