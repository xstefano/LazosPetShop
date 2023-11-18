package com.example.lazospetshop.clases;

public class Usuario {



    private int id;
    private String nombres;
    private String apellidos;
    private String numeroDocumetno;
    private String correo;
    private String contraseña;
    private int tipoDocumentoId;
    private int generoId;
    private String imagen;

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroDocumetno() {
        return numeroDocumetno;
    }

    public void setNumeroDocumetno(String numeroDocumetno) {
        this.numeroDocumetno = numeroDocumetno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoIt(int tipoDocumentoIt) {
        this.tipoDocumentoId = tipoDocumentoIt;
    }

    public int getGeneroId() {
        return generoId;
    }

    public void setGeneroId(int generoId) {
        this.generoId = generoId;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
