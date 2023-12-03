package com.example.lazospetshop.clases;

import java.util.List;

public class Carrito {
    private int idCarrito;
    private int idUsuario;
    private double montoTotal;
    private String metodoPago;
    private List<ProductosCarrito> detalle;
    private List<ServicioCarrito> detalleServicio;

    public  Carrito(int idCarrito,int idUsuario,double montoTotal,String metodoPago, List<ProductosCarrito> detalle,List<ServicioCarrito> detalleServicio){
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.montoTotal = montoTotal;
        this.metodoPago = metodoPago;
        this.detalle = detalle;
        this.setDetalleServicio(detalleServicio);

    }
    public  Carrito(){

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

    public List<ProductosCarrito> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<ProductosCarrito> detalle) {
        this.detalle = detalle;
    }

    public List<ServicioCarrito> getDetalleServicio() {
        return detalleServicio;
    }

    public void setDetalleServicio(List<ServicioCarrito> detalleServicio) {
        this.detalleServicio = detalleServicio;
    }
}
