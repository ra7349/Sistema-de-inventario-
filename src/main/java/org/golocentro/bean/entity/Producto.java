package org.golocentro.bean.entity;

public class Producto {
    private Integer idProducto;
    private String codigo;
    private String nombre;
    private String categoria;
    private String presentacion;
    private String unidadMedida;
    private double precioCompra;
    private double precioMinorista;
    private double precioMayorista;
    private Integer stockActual;
    private Integer stockMinimo;
    private String estado;

    public Producto() {
        this.estado = "Activo";
        this.stockActual = 0;
        this.stockMinimo = 0;
    }

    public Producto(Integer idProducto, String codigo, String nombre, String categoria, String presentacion,
                    String unidadMedida, double precioCompra, double precioMinorista, double precioMayorista,
                    Integer stockActual, Integer stockMinimo, String estado) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.unidadMedida = unidadMedida;
        this.precioCompra = precioCompra;
        this.precioMinorista = precioMinorista;
        this.precioMayorista = precioMayorista;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.estado = estado;
    }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecioMinorista() { return precioMinorista; }
    public void setPrecioMinorista(double precioMinorista) { this.precioMinorista = precioMinorista; }

    public double getPrecioMayorista() { return precioMayorista; }
    public void setPrecioMayorista(double precioMayorista) { this.precioMayorista = precioMayorista; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Compatibilidad con pantallas antiguas del módulo de productos.
    public String getId() { return idProducto == null ? null : String.valueOf(idProducto); }
    public void setId(String id) { this.idProducto = id == null || id.isBlank() ? null : Integer.parseInt(id); }
    public String getDescripcion() { return presentacion; }
    public void setDescripcion(String descripcion) { this.presentacion = descripcion; }
    public double getPrecio() { return precioMinorista; }
    public void setPrecio(double precio) { this.precioMinorista = precio; }
    public Integer getStock() { return stockActual; }
    public void setStock(Integer stock) { this.stockActual = stock; }
    public String getUnidadVenta() { return unidadMedida; }
    public void setUnidadVenta(String unidadVenta) { this.unidadMedida = unidadVenta; }

    @Override
    public String toString() {
        String codigoTexto = codigo == null ? "" : codigo;
        String nombreTexto = nombre == null ? "" : nombre;
        return codigoTexto + " - " + nombreTexto;
    }
}
