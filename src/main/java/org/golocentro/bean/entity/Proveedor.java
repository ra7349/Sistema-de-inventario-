package org.golocentro.bean.entity;

public class Proveedor extends Persona {
    private String codigo;
    private String razonSocial;
    private Long ruc;
    private String direccion;
    private String contacto;
    private String estado;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public Long getRuc() { return ruc; }
    public void setRuc(Long ruc) { this.ruc = ruc; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
