package com.example.lazospetshop.clases;
public class ProductosCarrito {
    private int idDetalle;
    private int idCarrito;
    private int idProducto;
    private  String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subTotal;

    private String imagen;

    public ProductosCarrito(int idDetalle,int idProducto,String nombreProducto,int cantidad,double precioUnitario, double subTotal,int idCarrito, String imagen){
        this.idDetalle = idDetalle;
        this.idProducto = idProducto;
        this.setNombreProducto(nombreProducto);
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
        this.idCarrito = idCarrito;
        this.imagen = imagen;
    }
    public ProductosCarrito(){

    }
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getImagen() { return imagen; }
}
