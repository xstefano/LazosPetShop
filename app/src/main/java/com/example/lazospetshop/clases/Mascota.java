package com.example.lazospetshop.clases;

public class Mascota {
    private int id;
    private String nombre;
    private String sexo;
    private String imagen;
    private int tipoMascotaId;
    private int usuarioId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getTipoMascotaId() {
        return tipoMascotaId;
    }

    public void setTipoMascotaId(int tipoMascotaId) {
        this.tipoMascotaId = tipoMascotaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return nombre; // Devuelve el campo 'tipo' para representar el objeto en el Spinner
    }
}
