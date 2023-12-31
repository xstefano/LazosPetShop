package com.example.lazospetshop.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lazospetshop.clases.Carrito;
import com.example.lazospetshop.clases.ProductosCarrito;
import com.example.lazospetshop.clases.ServicioCarrito;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LazosPetShop extends SQLiteOpenHelper{
    private static final String nombreBD = "LazosPetShop.db";
    private static final int versionBD = 2;
    private static final String createTableUsuario = "CREATE TABLE IF NOT EXISTS USUARIO (ID INTEGER, CORREO VARCHAR(255), CLAVE VARCHAR(255))";
    private static final String createTableCarrito =
            "CREATE TABLE IF NOT EXISTS Carrito (" +
                    "idCarrito INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idUsuario INTEGER," +
                    "fechaCreacion DATE," +
                    "metodoPago TEXT," +
                    "fechaPago DATE," +
                    "montoTotal REAL);";
    private static final String createTableDetalleProducto =
            "CREATE TABLE IF NOT EXISTS DetalleProducto (" +
                    "idDetalleCarritoProducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idCarrito INTEGER," +
                    "idProducto INTEGER," +
                    "nombreProducto TEXT," +
                    "cantidad INTEGER," +
                    "PrecioUnitario REAL," +
                    "imagen TEXT," +
                    "Subtotal REAL);";
    private static final String createTableDetalleServicio =
            "CREATE TABLE IF NOT EXISTS DetalleServicio (" +
                    "idDetalleCarritoServicio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idCarrito INTEGER," +
                    "idServicio INTEGER," +
                    "idMascota INTEGER," +
                    "imagen TEXT," +
                    "fechaServicio DATE," +
                    "precioUnitario REAL," +
                    "subtotal REAL);";
    private static final String dropTableUsuario = "DROP TABLE IF EXISTS USUARIO";
    private static final String dropTableCarrito= "DROP TABLE IF EXISTS Carrito";
    private static final String dropTableDetalleProducto= "DROP TABLE IF EXISTS DetalleProducto";
    private static final String dropTableDetalleServicio= "DROP TABLE IF EXISTS DetalleServicio";
    //Constructor
    public LazosPetShop(@Nullable Context context) {
        super(context, nombreBD, null, versionBD);
    }

    //Se ejecuta cuando se llama al constructor
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableUsuario);
        sqLiteDatabase.execSQL(createTableCarrito);
        sqLiteDatabase.execSQL(createTableDetalleProducto);
        sqLiteDatabase.execSQL(createTableDetalleServicio);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropTableUsuario);
        sqLiteDatabase.execSQL(createTableUsuario);

        sqLiteDatabase.execSQL(dropTableCarrito);
        sqLiteDatabase.execSQL(createTableCarrito);

        sqLiteDatabase.execSQL(dropTableDetalleProducto);
        sqLiteDatabase.execSQL(createTableDetalleProducto);

        sqLiteDatabase.execSQL(dropTableDetalleServicio);
        sqLiteDatabase.execSQL(createTableDetalleServicio);
    }

    public boolean agregarUsuario(int id, String correo, String clave){
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            db.execSQL("INSERT INTO USUARIO VALUES("+id+", '"+correo+"', '"+clave+"')");
            db.close();
            ret = true;
        }
        return ret;
    }
    // Método para obtener la fecha actual en formato yyyy-MM-dd
    public String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public boolean agregarCarrito(int idUsuario, String metodoPago, String fechaPago, double montoTotal) {
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                // Obtener la fecha actual
                String fechaCreacion = obtenerFechaActual();
                // Iniciar la transacción
                db.beginTransaction();

                // Construir el ContentValues con los valores a insertar
                ContentValues values = new ContentValues();
                values.put("idUsuario", idUsuario);
                values.put("fechaCreacion", fechaCreacion);
                values.put("metodoPago", metodoPago);
                values.put("fechaPago", fechaPago);
                values.put("montoTotal", montoTotal);

                // Insertar el nuevo registro en la tabla Carrito
                db.insert("Carrito", null, values);

                // Establecer la transacción como exitosa
                db.setTransactionSuccessful();
                ret = true;
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar la base de datos
                db.close();
            }
        }
        return ret;
    }
    public boolean actualizarMontoCarrito(int idCarrito, double nuevoMonto) {
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                // Iniciar la transacción
                db.beginTransaction();

                // Construir el ContentValues con el nuevo monto
                ContentValues values = new ContentValues();
                values.put("montoTotal", nuevoMonto);

                // Actualizar el registro en la tabla Carrito
                db.update("Carrito", values, "idCarrito = ?", new String[]{String.valueOf(idCarrito)});

                // Establecer la transacción como exitosa
                db.setTransactionSuccessful();
                ret = true;
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar la base de datos
                db.close();
            }
        }
        return ret;
    }
    @SuppressLint("Range")
    public boolean agregarDetalleProducto(int idCarrito, int cantidad, double precioUnitario, int idProducto,String nombreProducto, String imagen) {
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                // Iniciar la transacción
                db.beginTransaction();

                // Consulta para verificar si ya existe un detalle para este producto en el carrito
                Cursor cursor = db.rawQuery("SELECT idDetalleCarritoProducto, cantidad FROM DetalleProducto " +
                        "WHERE idCarrito = ? AND idProducto = ? LIMIT 1", new String[]{String.valueOf(idCarrito), String.valueOf(idProducto)});

                if (cursor != null && cursor.moveToFirst()) {
                    // Si existe, actualizar la cantidad y recalcular el subtotal
                    int idDetalle = cursor.getInt(cursor.getColumnIndex("idDetalleCarritoProducto"));
                    int cantidadExistente = cursor.getInt(cursor.getColumnIndex("cantidad"));

                    cantidad += cantidadExistente;

                    double nuevoSubtotal = cantidad * precioUnitario;

                    ContentValues values = new ContentValues();
                    values.put("cantidad", cantidad);
                    values.put("Subtotal", nuevoSubtotal);

                    // Actualizar el detalle existente
                    db.update("DetalleProducto", values, "idDetalleCarritoProducto = ?", new String[]{String.valueOf(idDetalle)});
                } else {
                    // Si no existe, agregar un nuevo detalle de producto
                    ContentValues values = new ContentValues();
                    values.put("idCarrito", idCarrito);
                    values.put("idProducto", idProducto);
                    values.put("nombreProducto",nombreProducto);
                    values.put("cantidad", cantidad);
                    values.put("PrecioUnitario", precioUnitario);
                    values.put("imagen", imagen);
                    values.put("Subtotal", cantidad * precioUnitario);

                    // Insertar el nuevo detalle de producto en la tabla DetalleProducto
                    db.insert("DetalleProducto", null, values);
                }

                // Establecer la transacción como exitosa
                db.setTransactionSuccessful();

                ret = true;
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar la base de datos
                db.close();
            }
        }
        return ret;
    }
    @SuppressLint("Range")
    public boolean agregarDetalleServicio(int idCarrito, double precioUnitario, int idServicio,double subTotal,int idMascota,String fechaServicio,String imagen) {
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                // Iniciar la transacción
                db.beginTransaction();

                // Consulta para verificar si ya existe un detalle para este producto en el carrito
                Cursor cursor = db.rawQuery("SELECT idDetalleCarritoServicio FROM DetalleServicio " +
                        "WHERE idCarrito = ? AND idServicio = ? AND idMascota = ? LIMIT 1", new String[]{String.valueOf(idCarrito), String.valueOf(idServicio), String.valueOf(idMascota)});

                if (cursor != null && cursor.moveToFirst()) {
                    // Si existe, actualizar la cantidad y recalcular el subtotal
                    /*int idDetalle = cursor.getInt(cursor.getColumnIndex("idDetalleCarritoProducto"));
                    int cantidadExistente = cursor.getInt(cursor.getColumnIndex("cantidad"));

                    cantidad += cantidadExistente;

                    double nuevoSubtotal = cantidad * precioUnitario;

                    ContentValues values = new ContentValues();
                    values.put("cantidad", cantidad);
                    values.put("Subtotal", nuevoSubtotal);

                    // Actualizar el detalle existente
                    db.update("DetalleProducto", values, "idDetalleCarritoProducto = ?", new String[]{String.valueOf(idDetalle)});*/
                    return false;
                } else {
                    /*"idDetalleCarritoServicio INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idCarrito INTEGER," +
                        "idServicio INTEGER," +
                        "idMascota INTEGER," +
                        "precioUnitario REAL," +
                        "subtotal REAL);";*/
                    // Si no existe, agregar un nuevo detalle de producto
                    ContentValues values = new ContentValues();
                    values.put("idCarrito", idCarrito);
                    values.put("idServicio", idServicio);
                    values.put("idMascota", idMascota);
                    values.put("fechaServicio",fechaServicio);
                    values.put("precioUnitario", precioUnitario);
                    values.put("subtotal", subTotal);
                    values.put("imagen",imagen);

                    // Insertar el nuevo detalle de producto en la tabla DetalleProducto
                    db.insert("DetalleServicio", null, values);
                }

                // Establecer la transacción como exitosa
                db.setTransactionSuccessful();

                ret = true;
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar la base de datos
                db.close();
            }
        }
        return ret;
    }
    @SuppressLint("Range")
    public Carrito obtenerCarritoPorUsuario(int idUsuario) {
        SQLiteDatabase db = getReadableDatabase();
        boolean flag = false;
        Carrito carrito = new Carrito();
        if (db != null) {
            Cursor cursor = null;
            Cursor cursorDetalle = null;

            try {
                // Consulta para obtener el carrito y sus detalles
                String consulta = "SELECT C.idCarrito, C.idUsuario, C.montoTotal,C.metodoPago, DP.idDetalleCarritoProducto, DP.idProducto, DP.cantidad, DP.PrecioUnitario, DP.Subtotal, DP.nombreProducto " +
                        "FROM Carrito C " +
                        "LEFT JOIN DetalleProducto DP ON C.idCarrito = DP.idCarrito " +
                        "WHERE C.idUsuario = ?";
                String consultaServicio = "SELECT C.idCarrito, C.idUsuario, C.montoTotal,C.metodoPago, DS.idDetalleCarritoServicio, DS.idServicio, DS.idMascota, DS.precioUnitario, DS.subtotal,DS.fechaServicio " +
                        "FROM Carrito C " +
                        "LEFT JOIN DetalleServicio DS ON C.idCarrito = DS.idCarrito " +
                        "WHERE C.idUsuario = ?";

                cursor = db.rawQuery(consulta, new String[]{String.valueOf(idUsuario)});
                cursorDetalle = db.rawQuery(consultaServicio, new String[]{String.valueOf(idUsuario)});
                List<ProductosCarrito> detalle = new ArrayList();
                List<ServicioCarrito> detalleServicio = new ArrayList<>();

                if (cursor != null && cursor.moveToFirst()) {

                    // Crear el objeto Carrito
                    carrito.setIdCarrito(cursor.getInt(cursor.getColumnIndex("idCarrito")));
                    carrito.setIdUsuario(cursor.getInt(cursor.getColumnIndex("idUsuario")));
                    carrito.setMontoTotal(cursor.getDouble(cursor.getColumnIndex("montoTotal")));
                    carrito.setMetodoPago(cursor.getString(cursor.getColumnIndex("metodoPago")));

                    // Crear y agregar detalles de producto al carrito
                    do {
                        ProductosCarrito detalleProducto = new ProductosCarrito();
                        detalleProducto.setIdDetalle(cursor.getInt(cursor.getColumnIndex("idDetalleCarritoProducto")));
                        detalleProducto.setIdCarrito(cursor.getInt(cursor.getColumnIndex("idCarrito")));
                        detalleProducto.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
                        detalleProducto.setNombreProducto(cursor.getString(cursor.getColumnIndex("nombreProducto")));
                        detalleProducto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
                        detalleProducto.setPrecioUnitario(cursor.getDouble(cursor.getColumnIndex("PrecioUnitario")));
                        detalleProducto.setSubTotal(cursor.getDouble(cursor.getColumnIndex("Subtotal")));

                        // Agregar detalleProducto a la lista de detalles en el carrito
                        detalle.add(detalleProducto);

                    } while (cursor.moveToNext());
                    carrito.setDetalle(detalle);
                }

                if (cursorDetalle != null && cursorDetalle.moveToFirst()) {


                    // Crear y agregar detalles de producto al carrito
                    do {
                        ServicioCarrito detServicio = new ServicioCarrito();
                        detServicio.setIdCarrito(cursorDetalle.getInt(cursorDetalle.getColumnIndex("idCarrito")));
                        detServicio.setIdServicio(cursorDetalle.getInt(cursorDetalle.getColumnIndex("idServicio")));
                        detServicio.setIdMascota(cursorDetalle.getInt(cursorDetalle.getColumnIndex("idMascota")));
                        detServicio.setFechaServicio(cursorDetalle.getString(cursorDetalle.getColumnIndex("fechaServicio")));
                        detServicio.setPrecioUnitario(cursorDetalle.getDouble(cursorDetalle.getColumnIndex("precioUnitario")));
                        detServicio.setSubTotal(cursorDetalle.getDouble(cursorDetalle.getColumnIndex("subtotal")));
                        // Agregar detalleProducto a la lista de detalles en el carrito
                        detalleServicio.add(detServicio);

                    } while (cursorDetalle.moveToNext());
                    carrito.setDetalleServicio(detalleServicio);
                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Cerrar el cursor y la base de datos
                if (cursor != null) {
                    cursor.close();
                }
                if (cursorDetalle != null) {
                    cursorDetalle.close();
                }
                db.close();
            }
        }

        return carrito;
    }

    @SuppressLint("Range")
    public void eliminarContenidoCarrito(int idCarrito) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                db.beginTransaction();
                db.delete("DetalleProducto", "idCarrito = ?", new String[]{String.valueOf(idCarrito)});
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    @SuppressLint("Range")
    public void eliminarContenidoDetalleProducto() {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                db.beginTransaction();
                db.delete("DetalleProducto", null, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    @SuppressLint("Range")
    public void eliminarContenidoServicioProducto() {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                db.beginTransaction();
                db.delete("DetalleServicio", null, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }
    @SuppressLint("Range")
    public int obtenerIdUsuario() {
        SQLiteDatabase db = getReadableDatabase();
        int idUsuario = -1;

        if (db != null) {
            Cursor cursor = null;

            try {
                // Consulta para obtener el ID del usuario basado en el correo y la clave
                cursor = db.rawQuery("SELECT ID FROM USUARIO",null);

                // Mover el cursor al primer resultado
                if (cursor != null && cursor.moveToFirst()) {
                    // Obtener el valor del ID
                    idUsuario = cursor.getInt(cursor.getColumnIndex("ID"));
                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Cerrar el cursor
                if (cursor != null) {
                    cursor.close();
                }

                // Cerrar la base de datos
                db.close();
            }
        }

        return idUsuario;
    }
    @SuppressLint("Range")
    public List<ProductosCarrito> obtenerProductosCarrito(int idCarrito) {
        List<ProductosCarrito> listaProductos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (db != null) {
            Cursor cursor = null;

            try {
                // Consulta para obtener los productos del carrito con sus detalles
                cursor = db.rawQuery("SELECT idDetalleCarritoProducto, idProducto, nombreProducto, cantidad, " +
                        "PrecioUnitario, imagen, Subtotal FROM DetalleProducto " +
                        "WHERE idCarrito = ?", new String[]{String.valueOf(idCarrito)});



                // Mover el cursor a través de los resultados
                while (cursor != null && cursor.moveToNext()) {
                    int idDetalle = cursor.getInt(cursor.getColumnIndex("idDetalleCarritoProducto"));
                    int idProducto = cursor.getInt(cursor.getColumnIndex("idProducto"));
                    String nombreProducto = cursor.getString(cursor.getColumnIndex("nombreProducto"));
                    int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                    double precioUnitario = cursor.getDouble(cursor.getColumnIndex("PrecioUnitario"));
                    String imagen = cursor.getString(cursor.getColumnIndex("imagen"));
                    double subtotal = cursor.getDouble(cursor.getColumnIndex("Subtotal"));

                    // Crear un objeto ProductoCarrito y agregarlo a la lista
                    ProductosCarrito productoCarrito = new ProductosCarrito(idDetalle, idProducto, nombreProducto, cantidad, precioUnitario, subtotal,idCarrito, imagen);
                    listaProductos.add(productoCarrito);
                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Cerrar el cursor
                if (cursor != null) {
                    cursor.close();
                }

                // Cerrar la base de datos
                db.close();
            }
        }

        return listaProductos;
    }

    @SuppressLint("Range")
    public List<ServicioCarrito> obtenerServiciosCarrito(int idCarrito) {
        List<ServicioCarrito> listaServicios = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (db != null) {
            Cursor cursor = null;
            /*"idDetalleCarritoServicio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idCarrito INTEGER," +
                    "idServicio INTEGER," +
                    "idMascota INTEGER," +
                    "fechaServicio DATE," +
                    "precioUnitario REAL," +
                    "subtotal REAL);";*/
            try {
                // Consulta para obtener los productos del carrito con sus detalles
                cursor = db.rawQuery("SELECT idDetalleCarritoServicio, idServicio, case when idServicio = 1 then 'baño' when idServicio = 2 then 'Corte' when idServicio = 3 then 'Baño/Servicio' end as nombreServicio, idMascota, fechaServicio,precioUnitario,subtotal,imagen " +
                        " FROM DetalleServicio " +
                        "WHERE idCarrito = ?", new String[]{String.valueOf(idCarrito)});



                // Mover el cursor a través de los resultados
                while (cursor != null && cursor.moveToNext()) {
                    ServicioCarrito serv = new ServicioCarrito();
                    serv.setIdServicio(cursor.getInt(cursor.getColumnIndex("idServicio")));
                    serv.setNombreServicio(cursor.getString(cursor.getColumnIndex("nombreServicio")));
                    serv.setIdMascota(cursor.getInt(cursor.getColumnIndex("idMascota")));
                    serv.setPrecioUnitario(cursor.getDouble(cursor.getColumnIndex("precioUnitario")));
                    serv.setFechaServicio(cursor.getString(cursor.getColumnIndex("fechaServicio")));
                    serv.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));
                    serv.setSubTotal(cursor.getDouble(cursor.getColumnIndex("subtotal")));

                    // Crear un objeto ProductoCarrito y agregarlo a la lista

                    listaServicios.add(serv);
                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Cerrar el cursor
                if (cursor != null) {
                    cursor.close();
                }

                // Cerrar la base de datos
                db.close();
            }
        }

        return listaServicios;
    }

    @SuppressLint("Range")
    public void actualizarMontoTotalCarrito(int idCarrito) {
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            Cursor cursor = null;
            Cursor cursorServicio = null;
            double nuevoMonto = 0;
            double nuevoMontoServicio = 0;
            try {
                // Iniciar la transacción
                db.beginTransaction();

                // Consulta para calcular el nuevo monto total del carrito
                cursor = db.rawQuery("SELECT SUM(Subtotal) AS nuevoMonto FROM DetalleProducto " +
                        "WHERE idCarrito = ?", new String[]{String.valueOf(idCarrito)});
                cursorServicio = db.rawQuery("SELECT SUM(subtotal) AS nuevoMonto FROM DetalleServicio " +
                        "WHERE idCarrito = ?", new String[]{String.valueOf(idCarrito)});

                // Mover el cursor al primer resultado
                if (cursor != null && cursor.moveToFirst()) {
                    nuevoMonto = cursor.getDouble(cursor.getColumnIndex("nuevoMonto"));


                }
                if (cursorServicio != null && cursorServicio.moveToFirst()) {
                    nuevoMontoServicio = cursorServicio.getDouble(cursorServicio.getColumnIndex("nuevoMonto"));
                }
                if((nuevoMontoServicio+nuevoMonto) > 0){
                    // Actualizar el monto total en la tabla Carrito
                    ContentValues values = new ContentValues();
                    values.put("montoTotal", (nuevoMonto + nuevoMontoServicio));

                    db.update("Carrito", values, "idCarrito = ?", new String[]{String.valueOf(idCarrito)});

                    // Establecer la transacción como exitosa
                    db.setTransactionSuccessful();
                    ret = true;

                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar el cursor y la base de datos
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        }

        //return ret;
    }

    @SuppressLint("Range")
    public void actualizarMontoTotalCarritoServicio(int idCarrito) {
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            Cursor cursor = null;
            try {
                // Iniciar la transacción
                db.beginTransaction();

                // Consulta para calcular el nuevo monto total del carrito
                cursor = db.rawQuery("SELECT SUM(subtotal) AS nuevoMonto FROM DetalleServicio " +
                        "WHERE idCarrito = ?", new String[]{String.valueOf(idCarrito)});

                // Mover el cursor al primer resultado
                if (cursor != null && cursor.moveToFirst()) {
                    double nuevoMonto = cursor.getDouble(cursor.getColumnIndex("nuevoMonto"));

                    // Actualizar el monto total en la tabla Carrito
                    ContentValues values = new ContentValues();
                    values.put("montoTotal", nuevoMonto);

                    db.update("Carrito", values, "idCarrito = ?", new String[]{String.valueOf(idCarrito)});

                    // Establecer la transacción como exitosa
                    db.setTransactionSuccessful();
                    ret = true;
                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar el cursor y la base de datos
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        }

        //return ret;
    }
    public void eliminarRegistros() {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                // Iniciar la transacción
                db.beginTransaction();

                // Eliminar todos los registros de la tabla DetalleProducto
                db.delete("DetalleProducto", null, null);

                // Eliminar todos los registros de la tabla DetalleServicio
                db.delete("DetalleServicio", null, null);

                // Eliminar todos los registros de la tabla Carrito
                db.delete("Carrito", null, null);

                // Eliminar todos los registros de la tabla Usuario
                db.delete("Usuario", null, null);

                // Establecer la transacción como exitosa
                db.setTransactionSuccessful();
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Finalizar la transacción
                db.endTransaction();

                // Cerrar la base de datos
                db.close();
            }
        }
    }
    @SuppressLint("Range")
    public int obtenerIdCarrito(int idUsuario) {
        SQLiteDatabase db = getReadableDatabase();
        int idCarrito = -1;

        if (db != null) {
            Cursor cursor = null;

            try {
                // Consulta para obtener el ID del carrito basado en el ID de usuario
                cursor = db.rawQuery("SELECT idCarrito FROM Carrito WHERE idUsuario = ?",
                        new String[]{String.valueOf(idUsuario)});

                // Mover el cursor al primer resultado
                if (cursor != null && cursor.moveToFirst()) {
                    // Obtener el valor del ID del carrito
                    idCarrito = cursor.getInt(cursor.getColumnIndex("idCarrito"));
                }
            } catch (Exception e) {
                // Manejar la excepción si ocurre algún error
                e.printStackTrace();
            } finally {
                // Cerrar el cursor
                if (cursor != null) {
                    cursor.close();
                }

                // Cerrar la base de datos
                db.close();
            }
        }

        return idCarrito;
    }
    public boolean recordoSesion(){
        boolean ret = false;
        SQLiteDatabase db = getReadableDatabase();
        if(db != null){
            Cursor cursor = db.rawQuery("SELECT * FROM USUARIO", null);
            if(cursor.moveToNext())
                ret = true;
        }
        return ret;
    }

    public String extraerDatoUsuario(String campo){
        String data = "";
        SQLiteDatabase db = getReadableDatabase();
        String consulta = String.format("SELECT %s FROM USUARIO", campo);
        if(db != null){
            Cursor cursor = db.rawQuery(consulta, null);
            if(cursor.moveToNext())
                data = cursor.getString(0);
        }
        return data;
    }

    public boolean eliminarUsuario(int id){
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            db.execSQL("DELETE FROM USUARIO WHERE ID = "+id);
            db.close();
            ret = true;
        }
        return ret;
    }

    public boolean actualizarDatoUsuario(int id, String campo, String valor){
        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            db.execSQL("UPDATE USUARIO SET "+campo+" = '"+valor+"' WHERE ID = "+id);
            db.close();
            ret = true;
        }
        return ret;
    }
}
