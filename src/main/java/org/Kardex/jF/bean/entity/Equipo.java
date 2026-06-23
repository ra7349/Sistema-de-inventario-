package org.Kardex.jF.bean.entity;

import java.time.LocalDate;

public class Equipo {
    private String    id;
    private String    codigo;
    private String    marca;
    private String    modelo;
    private String    numeroSerie;
    private String    tipoEquipo;
    private Boolean   estado;
    private LocalDate fechaIngreso;
    private Integer   idCliente;
    private String    nombreCliente; // join helper

    
    public Equipo(String id, String codigo, String marca, String modelo, String numeroSerie, String tipoEquipo,
			Boolean estado, LocalDate fechaIngreso, Integer idCliente, String nombreCliente) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.marca = marca;
		this.modelo = modelo;
		this.numeroSerie = numeroSerie;
		this.tipoEquipo = tipoEquipo;
		this.estado = estado;
		this.fechaIngreso = fechaIngreso;
		this.idCliente = idCliente;
		this.nombreCliente = nombreCliente;
	}
    

    public Equipo(String marca, String modelo, String numero_serie) {
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numero_serie;
    }
    
	public Equipo() {
		super();
	}



	public String    getId()                         { return id; }
    public void      setId(String id)                { this.id = id; }
    public String    getCodigo()                     { return codigo; }
    public void      setCodigo(String c)             { this.codigo = c; }
    public String    getMarca()                      { return marca; }
    public void      setMarca(String m)              { this.marca = m; }
    public String    getModelo()                     { return modelo; }
    public void      setModelo(String m)             { this.modelo = m; }
    public String    getNumeroSerie()                { return numeroSerie; }
    public void      setNumeroSerie(String n)        { this.numeroSerie = n; }
    public String    getTipoEquipo()                 { return tipoEquipo; }
    public void      setTipoEquipo(String t)         { this.tipoEquipo = t; }
    public Boolean   getEstado()                     { return estado; }
    public void      setEstado(Boolean e)            { this.estado = e; }
    public LocalDate getFechaIngreso()               { return fechaIngreso; }
    public void      setFechaIngreso(LocalDate f)    { this.fechaIngreso = f; }
    public Integer   getIdCliente()                  { return idCliente; }
    public void      setIdCliente(Integer i)         { this.idCliente = i; }
    public String    getNombreCliente()              { return nombreCliente; }
    public void      setNombreCliente(String n)      { this.nombreCliente = n; }
}
