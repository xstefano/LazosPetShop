package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Carrito;
import com.example.lazospetshop.clases.ProductosCarrito;
import com.example.lazospetshop.sqlite.LazosPetShop;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

public class DetallePagoActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String urlControllerCarrito = "https://lazospetshop.azurewebsites.net/api/carrito/registrar";
    private final static String urlControllerDetalle= "https://lazospetshop.azurewebsites.net/api/detalleproducto/registrar";
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
                break;
        }

    }
    private void pagar(){
        AsyncHttpClient ahcRegistrarCarrito = new AsyncHttpClient();
        AsyncHttpClient ahcRegistrarDetalle = new AsyncHttpClient();
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
        Carrito carrito = bd.obtenerCarritoPorUsuario(idUsuario);
        Toast.makeText(getApplicationContext(),formattedDateTime+"",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"Procesando compra!",Toast.LENGTH_SHORT).show();
                if((statusCode+"").equals("200")){
                    Toast.makeText(getApplicationContext(),"Carrito registrado!",Toast.LENGTH_SHORT).show();
                    /*finish();
                    iniciarSecion = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                    startActivity(iniciarSecion);*/
                    /*for (ProductosCarrito deta:carrito.getDetalle()) {
                        //JSONObject jsonParamsDetalle = new JSONObject();
                        Toast.makeText(getApplicationContext(),deta.getNombreProducto(),Toast.LENGTH_SHORT).show();
                    }*/


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
}