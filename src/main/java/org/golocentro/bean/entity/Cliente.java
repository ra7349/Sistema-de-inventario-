package org.golocentro.bean.entity;

public class Cliente extends Persona {
    private String codigo;
    private String direccion;
    private String tipoCliente;
    private Long   ruc;
    
    public Cliente(String id, String nombre) {
        super(id, nombre, null, null, null); 
    }
    
    @Override
    public String toString() {
        return this.getNombre(); // Usamos el getter heredado de Persona
    }
    public Cliente() {
		super();
	}
	public Cliente(String codigo, String direccion, String tipoCliente, Long ruc) {
		super();
		this.codigo = codigo;
		this.direccion = direccion;
		this.tipoCliente = tipoCliente;
		this.ruc = ruc;
	}
	public String getCodigo()                  { return codigo; }
    public void   setCodigo(String c)          { this.codigo = c; }
    public String getDireccion()               { return direccion; }
    public void   setDireccion(String d)       { this.direccion = d; }
    public String getTipoCliente()             { return tipoCliente; }
    public void   setTipoCliente(String t)     { this.tipoCliente = t; }
    public Long   getRUC()                     { return ruc; }
    public void   setRUC(Long r)               { this.ruc = r; }
}
