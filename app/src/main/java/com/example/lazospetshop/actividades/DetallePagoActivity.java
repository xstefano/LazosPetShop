package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Carrito;
import com.example.lazospetshop.clases.CarritoRespuesta;
import com.example.lazospetshop.clases.ProductosCarrito;
import com.example.lazospetshop.clases.ServicioCarrito;
import com.example.lazospetshop.sqlite.LazosPetShop;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public class DetallePagoActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String urlControllerCarrito = "https://lazospetshop.azurewebsites.net/api/carrito/registrar";
    private final static String urlControllerDetalle= "https://lazospetshop.azurewebsites.net/api/detalleproducto/registrar";
    private final static String urlControllerDetalleServicio= "https://lazospetshop.azurewebsites.net/api/detalleservicio/registrar";
    Button btnPaga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pago);
        btnPaga = findViewById(R.id.btnPagar);

        btnPaga.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnPagar:

                pagar();
                limpiarCarritoYDetalle();
                break;
        }

    }
    private void pagar(){
        AsyncHttpClient ahcRegistrarCarrito = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();

        // Obtén la fecha y hora actual en formato UTC
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Define el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Formatea la fecha y hora actual
        String formattedDateTime = now.format(formatter);

        LazosPetShop bd = new LazosPetShop(getApplicationContext());
        Integer idUsuario = bd.obtenerIdUsuario();
        Integer idCarrito = bd.obtenerIdCarrito(idUsuario);
        final Carrito carrito = bd.obtenerCarritoPorUsuario(idUsuario);

        try{
            jsonParams.put("idUsuario",carrito.getIdUsuario()+"");
            jsonParams.put("fechaCreacion",formattedDateTime);
            jsonParams.put("metodoPago",carrito.getMetodoPago());
            jsonParams.put("fechaPago",formattedDateTime);
            jsonParams.put("montoTotal",carrito.getMontoTotal()+"");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ahcRegistrarCarrito.post(null,urlControllerCarrito,  entity, "application/json", new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {

                if((statusCode+"").equals("200")){
                    Toast.makeText(getApplicationContext(),"Pago exitoso!",Toast.LENGTH_SHORT).show();
                    // Parsea la cadena JSON en un objeto Carrito
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        CarritoRespuesta carritoRespuesta = objectMapper.readValue(rawJsonResponse, CarritoRespuesta.class);
                        //Toast.makeText(getApplicationContext(),carritoRespuesta.getId()+"",Toast.LENGTH_SHORT).show();
                        for (ProductosCarrito deta:carrito.getDetalle()) {
                            JSONObject jsonParamsDetalle = new JSONObject();
                            try{
                                jsonParamsDetalle.put("carritoId",carritoRespuesta.getId());
                                jsonParamsDetalle.put("productoId",deta.getIdProducto());
                                jsonParamsDetalle.put("cantidad",deta.getCantidad());
                                jsonParamsDetalle.put("precioUnitario",deta.getPrecioUnitario());
                                jsonParamsDetalle.put("subTotal",deta.getSubTotal());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AsyncHttpClient ahcRegistrarDetalle = new AsyncHttpClient();
                            StringEntity entityDetalle = new StringEntity(jsonParamsDetalle.toString(), "UTF-8");
                            entityDetalle.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                            ahcRegistrarDetalle.post(null,urlControllerDetalle,  entityDetalle, "application/json", new BaseJsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                                    Intent perfilActivity = null;
                                    if((statusCode+"").equals("200")) {
                                        finish();
                                        perfilActivity = new Intent(getApplicationContext(), PerfilActivity.class);
                                        startActivity(perfilActivity);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                                    Toast.makeText(getApplicationContext(),statusCode+"",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                                    return null;
                                }
                            });

                            //Toast.makeText(getApplicationContext(),deta.getNombreProducto(),Toast.LENGTH_SHORT).show();
                        }

                        /*{
                          "carritoId": 0,
                          "servicioId": 0,
                          "precioUnitario": 0,
                          "subTotal": 0,
                          "fechaRegistro": "2023-12-03T03:49:29.642Z",
                          "mascotaId": 0
                        }*/

                        for (ServicioCarrito detaServ:carrito.getDetalleServicio()) {
                            JSONObject jsonParamsDetalleServ = new JSONObject();
                            // Formato de la cadena original
                            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                            String fechaForma = "";
                            try {
                                // Parsear la cadena original a un objeto Date
                                Date fecha = formatoOriginal.parse(detaServ.getFechaServicio().replace("-", "/"));

                                // Formato deseado
                                SimpleDateFormat formatoDeseado = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                                // Formatear la fecha en el nuevo formato
                                String fechaFormateada = formatoDeseado.format(fecha);

                                // Imprimir la fecha formateada
                                //System.out.println("Fecha formateada: " + fechaFormateada);
                                fechaForma = fechaFormateada;
                                Toast.makeText(getApplicationContext(),fechaForma,Toast.LENGTH_SHORT).show();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try{
                                Log.d("Debug", carritoRespuesta.getId()+"");
                                Log.d("Debug", detaServ.getIdServicio()+"");
                                Log.d("Debug", detaServ.getPrecioUnitario()+"");
                                Log.d("Debug", detaServ.getSubTotal()+"");
                                Log.d("Debug", fechaForma);
                                Log.d("Debug", detaServ.getIdMascota()+"");
                                jsonParamsDetalleServ.put("carritoId",carritoRespuesta.getId());
                                jsonParamsDetalleServ.put("servicioId",detaServ.getIdServicio());
                                jsonParamsDetalleServ.put("precioUnitario",detaServ.getPrecioUnitario());
                                jsonParamsDetalleServ.put("subTotal",detaServ.getSubTotal());
                                jsonParamsDetalleServ.put("fechaRegistro",fechaForma);
                                jsonParamsDetalleServ.put("mascotaId",detaServ.getIdMascota());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AsyncHttpClient ahcRegistrarDetalleServ = new AsyncHttpClient();
                            StringEntity entityDetalleServ = new StringEntity(jsonParamsDetalleServ.toString(), "UTF-8");
                            entityDetalleServ.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                            ahcRegistrarDetalleServ.post(null,urlControllerDetalleServicio,  entityDetalleServ, "application/json", new BaseJsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                                    Intent perfilActivity = null;
                                    if((statusCode+"").equals("200")) {
                                        /*finish();
                                        perfilActivity = new Intent(getApplicationContext(), PerfilActivity.class);
                                        startActivity(perfilActivity);*/
                                        Toast.makeText(getApplicationContext(),"Servicio registrado!",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                                    Toast.makeText(getApplicationContext(),statusCode+"",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                                    return null;
                                }
                            });

                            //Toast.makeText(getApplicationContext(),deta.getNombreProducto(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }




                }
                else{
                    Toast.makeText(getApplicationContext(),"Error al pagar!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(),statusCode+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private void limpiarCarritoYDetalle(){
        LazosPetShop bd = new LazosPetShop(getApplicationContext());
        Integer idUsuario = bd.obtenerIdUsuario();
        Integer idCarrito = bd.obtenerIdCarrito(idUsuario);

        bd.eliminarContenidoCarrito(idCarrito);
        bd.eliminarContenidoDetalleProducto();
        bd.eliminarContenidoServicioProducto();
    }
}