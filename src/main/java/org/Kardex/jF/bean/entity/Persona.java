package org.Kardex.jF.bean.entity;

public class Persona {
    private String id;
    private String nombre;
    private String apellido;
    private Long   telefono;
    private String correo;

    
    public Persona() {
		super();
	}


	public Persona(String id, String nombre, String apellido, Long telefono, String correo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.correo = correo;
	}


	public String getId()                  { return id; }
    public void   setId(String id)         { this.id = id; }
    public String getNombre()              { return nombre; }
    public void   setNombre(String n)      { this.nombre = n; }
    public String getApellido()            { return apellido; }
    public void   setApellido(String a)    { this.apellido = a; }
    public Long   getTelefono()            { return telefono; }
    public void   setTelefono(Long t)      { this.telefono = t; }
    public String getCorreo()              { return correo; }
    public void   setCorreo(String c)      { this.correo = c; }
}
