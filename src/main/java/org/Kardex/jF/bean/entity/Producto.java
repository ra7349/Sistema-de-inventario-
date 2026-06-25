package org.Kardex.jF.bean.entity;

public class Producto {
    private String  id;
    private String  codigo;
    private String  nombre;
    private String  descripcion;
    private double  precio;
    private Integer stock;
    private String  categoria;
    private String  unidadVenta;
    private double  precioMayorista;
    private double  precioMinorista;
    private String  proveedor;

    public String  getId()                     { return id; }
    public void    setId(String id)            { this.id = id; }
    public String  getCodigo()                 { return codigo; }
    public void    setCodigo(String c)         { this.codigo = c; }
    public String  getNombre()                 { return nombre; }
    public void    setNombre(String n)         { this.nombre = n; }
    public String  getDescripcion()            { return descripcion; }
    public void    setDescripcion(String d)    { this.descripcion = d; }
    public double  getPrecio()                 { return precio; }
    public void    setPrecio(double p)         { this.precio = p; }
    public Integer getStock()                  { return stock; }
    public void    setStock(Integer s)         { this.stock = s; }
    public String  getCategoria()              { return categoria; }
    public void    setCategoria(String c)      { this.categoria = c; }
    public String  getUnidadVenta()           { return unidadVenta; }
    public void    setUnidadVenta(String u)    { this.unidadVenta = u; }
    public double  getPrecioMayorista()        { return precioMayorista; }
    public void    setPrecioMayorista(double p){ this.precioMayorista = p; }
    public double  getPrecioMinorista()        { return precioMinorista; }
    public void    setPrecioMinorista(double p){ this.precioMinorista = p; }
    public String  getProveedor()              { return proveedor; }
    public void    setProveedor(String p)      { this.proveedor = p; }
}
