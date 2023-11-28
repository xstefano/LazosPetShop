package com.example.lazospetshop.clases;

public class CarritoRespuesta {
    private int id;
    private String fechaCreacion;
    private  String fechaPago;
    private int idCarrito;

    private int idUsuario;
    private double montoTotal;
    private String metodoPago;

    public CarritoRespuesta(int idCarrito, int idUsuario, double montoTotal, String metodoPago, int id,String fechaCreacion,String fechaPago){
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.montoTotal = montoTotal;
        this.metodoPago = metodoPago;
        this.setId(id);
        this.setFechaCreacion(fechaCreacion);
        this.setFechaPago(fechaPago);

    }
    public CarritoRespuesta(){

    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
