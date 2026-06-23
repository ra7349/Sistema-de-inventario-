package org.Kardex.jF.bean.entity;

public class Indicadores {
    private int equiposEnReparacion;
    private int equiposEntregados;
    private int serviciosEsteMes;
    private int clientesRegistrados;
    private double ingresosDelMes;

    public Indicadores() {
    }

    public Indicadores(int equiposEnReparacion, int equiposEntregados, int serviciosEsteMes,
            int clientesRegistrados, double ingresosDelMes) {
        this.equiposEnReparacion = equiposEnReparacion;
        this.equiposEntregados = equiposEntregados;
        this.serviciosEsteMes = serviciosEsteMes;
        this.clientesRegistrados = clientesRegistrados;
        this.ingresosDelMes = ingresosDelMes;
    }

    public int getEquiposEnReparacion() {
        return equiposEnReparacion;
    }

    public void setEquiposEnReparacion(int equiposEnReparacion) {
        this.equiposEnReparacion = equiposEnReparacion;
    }

    public int getEquiposEntregados() {
        return equiposEntregados;
    }

    public void setEquiposEntregados(int equiposEntregados) {
        this.equiposEntregados = equiposEntregados;
    }

    public int getServiciosEsteMes() {
        return serviciosEsteMes;
    }

    public void setServiciosEsteMes(int serviciosEsteMes) {
        this.serviciosEsteMes = serviciosEsteMes;
    }

    public int getClientesRegistrados() {
        return clientesRegistrados;
    }

    public void setClientesRegistrados(int clientesRegistrados) {
        this.clientesRegistrados = clientesRegistrados;
    }

    public double getIngresosDelMes() {
        return ingresosDelMes;
    }

    public void setIngresosDelMes(double ingresosDelMes) {
        this.ingresosDelMes = ingresosDelMes;
    }
}
