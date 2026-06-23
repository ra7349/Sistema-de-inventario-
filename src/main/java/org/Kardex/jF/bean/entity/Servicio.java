package org.Kardex.jF.bean.entity;

public class Servicio {

    private Integer idServicio;
    private String codigo;
    private String descripcion;
    private Double precio;
    private String estado;
    
    public String toString() {
        return this.descripcion + " ($" + this.precio + ")";
    }
    
    
	public Servicio() {
		super();
	}
	public Servicio(Integer idServicio, String codigo, String descripcion, Double precio,
			String estado) {
		super();
		this.idServicio = idServicio;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.estado = estado;
	}
	public Integer getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
