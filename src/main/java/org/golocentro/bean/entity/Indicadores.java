package org.golocentro.bean.entity;

public class Indicadores {
    private int productosBajoStock;
    private int ventasRegistradas;
    private int movimientosEsteMes;
    private int clientesRegistrados;
    private double ingresosDelMes;

    public Indicadores() {}

    public Indicadores(int productosBajoStock, int ventasRegistradas, int movimientosEsteMes,
            int clientesRegistrados, double ingresosDelMes) {
        this.productosBajoStock = productosBajoStock;
        this.ventasRegistradas = ventasRegistradas;
        this.movimientosEsteMes = movimientosEsteMes;
        this.clientesRegistrados = clientesRegistrados;
        this.ingresosDelMes = ingresosDelMes;
    }

    public int getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(int productosBajoStock) { this.productosBajoStock = productosBajoStock; }
    public int getVentasRegistradas() { return ventasRegistradas; }
    public void setVentasRegistradas(int ventasRegistradas) { this.ventasRegistradas = ventasRegistradas; }
    public int getMovimientosEsteMes() { return movimientosEsteMes; }
    public void setMovimientosEsteMes(int movimientosEsteMes) { this.movimientosEsteMes = movimientosEsteMes; }
    public int getClientesRegistrados() { return clientesRegistrados; }
    public void setClientesRegistrados(int clientesRegistrados) { this.clientesRegistrados = clientesRegistrados; }
    public double getIngresosDelMes() { return ingresosDelMes; }
    public void setIngresosDelMes(double ingresosDelMes) { this.ingresosDelMes = ingresosDelMes; }
}
