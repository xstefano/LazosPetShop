package com.example.lazospetshop.clases;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private String imagen;

    public Producto(int id, String nombre, double precio, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }
}
