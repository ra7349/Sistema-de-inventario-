package org.Kardex.jF.bean.entity;

public class PrecioProducto {
    private Integer idPrecioProducto;
    private Integer idProducto;
    private String tipoCliente;
    private double precioUnitario;
    private String unidadVenta;

    public Integer getIdPrecioProducto() { return idPrecioProducto; }
    public void setIdPrecioProducto(Integer idPrecioProducto) { this.idPrecioProducto = idPrecioProducto; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public String getUnidadVenta() { return unidadVenta; }
    public void setUnidadVenta(String unidadVenta) { this.unidadVenta = unidadVenta; }
}
