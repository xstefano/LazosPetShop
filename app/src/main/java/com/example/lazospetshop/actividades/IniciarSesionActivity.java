package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Hash;
import com.example.lazospetshop.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class  IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String urlController = "https://lazospetshop.azurewebsites.net/api/usuario/login";
    EditText txtCorreo, txtContraseña;
    Button btnIniciar, btnRegistrate, btnOlvidasteContra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        txtCorreo = findViewById(R.id.logTxtCorreo);
        txtContraseña = findViewById(R.id.logTxtContraseña);
        btnIniciar = findViewById(R.id.logBtnIniciar);
        btnRegistrate = findViewById(R.id.logBtnRegistrate);
        btnOlvidasteContra = findViewById(R.id.logLblOlvidarContraseña);

        btnIniciar.setOnClickListener(this);
        btnRegistrate.setOnClickListener(this);
        btnOlvidasteContra.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
      /*  if (view.getId() == R.id.logBtnIniciar){
            IniciarSesion(txtCorreo.getText().toString(), txtContraseña.getText().toString());
        } else if (view.getId() == R.id.logBtnRegistrate) {
            Registrate();
        } else if (view.getId() == R.id.logLblOlvidarContraseña) {
            OlvidarContraseña();;
        }
        */
       switch(view.getId()){
           case R.id.logBtnIniciar:
               login();
               break;
           case R.id.logBtnRegistrate:
               registrate();
               break;
           case R.id.logLblOlvidarContraseña:
               OlvidasteContra();
               break;
       }
    }

    private void login() {
        AsyncHttpClient ahcLogin = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        Hash hash = new Hash();
       String correo= txtCorreo.getText().toString().trim();
       String pass=txtContraseña.getText().toString().trim();
        try{
            jsonParams.put("correo",correo);
            jsonParams.put("contrasena",hash.StringToHash(pass,"SHA1"));
            //Toast.makeText(getApplicationContext(),hash.StringToHash(pass,"SHA1"),Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ahcLogin.post(null,urlController,  entity, "application/json", new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Intent perfilActivity = null;
                Usuario user = new Usuario();

                //Toast.makeText(getApplicationContext(),statusCode+"",Toast.LENGTH_SHORT).show();

                if((statusCode+"").equals("200")){
                    try{
                        /*"id": 0,
                        "nombres": "string",
                        "apellidos": "string",
                        "correo": "string",
                        "contraseña": "string",
                        "tipoDocumentoId": 0,
                        "numeroDocumento": "string",
                        "generoId": 0*/
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.length() > 0){
                            user.setId(jsonArray.getJSONObject(0).getInt("id"));
                            user.setNombres(jsonArray.getJSONObject(0).getString("nombres"));
                            user.setApellidos(jsonArray.getJSONObject(0).getString("apellidos"));
                            user.setCorreo(jsonArray.getJSONObject(0).getString("correo"));
                            user.setContraseña(jsonArray.getJSONObject(0).getString("contrasena"));
                            user.setNumeroDocumetno(jsonArray.getJSONObject(0).getString("numeroDocumento"));
                            user.setTipoDocumentoIt(jsonArray.getJSONObject(0).getInt("tipoDocumento"));
                            user.setGeneroId(jsonArray.getJSONObject(0).getInt("genero"));
                            user.setImagen(jsonArray.getJSONObject(0).getString("imagen"));

                            Toast.makeText(getApplicationContext(),"Usuario logeado!",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),user.getId() + "",Toast.LENGTH_SHORT).show();
                            finish();
                            perfilActivity = new Intent(getApplicationContext(), PerfilActivity.class);
                            perfilActivity.putExtra("id", user.getId() + "");
                            startActivity(perfilActivity);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Hubo un error!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                if((statusCode+"").equals("400")){
                    Toast.makeText(getApplicationContext(),rawJsonData,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
       /*if(correo.isEmpty() || pass.isEmpty()){
           Toast.makeText(this,"Ingresar datos correctamente",Toast.LENGTH_SHORT).show();
           return;
       }*/
       //iniciarSesion(correo,pass);
    }

    private void iniciarSesion(String correo, String pass) {

        correo = correo.toLowerCase().trim();
        pass = pass.toLowerCase().trim();

        if(correo.equals("cliente@gmail.com")&& pass.equals("12345")){
                Intent iPerfil = new Intent(this, PerfilActivity.class);
                iPerfil.putExtra("nombre", "Cliente");
                startActivity(iPerfil);
            }
            else{
                Toast.makeText(this, "Creendeciales incorrectas", Toast.LENGTH_SHORT).show();
            }
    }
    public void registrate(){
        /*Intent iRegistrate = new Intent(this, RegistrarActiviy.class);
        startActivities((iRegistrate));
        finish();*/
        Intent iRegistrar = new Intent(this, RegistroActivity.class);
        startActivity(iRegistrar);
        finish();
    }

    public void OlvidasteContra(){
        Intent iOlvidaste = new Intent(this, OlvidasteContraActivity.class);
        startActivity(iOlvidaste);
        finish();
    }


   /*
    public void IniciarSesion(String correo, String contraseña) {

        correo = correo.toLowerCase().trim();
        contraseña = contraseña.toLowerCase().trim();

        if (correo.equals("admin@gmail.com") && contraseña.equals("12345")) {
            Intent iPerfil = new Intent(this, PerfilActivity.class);
            iPerfil.putExtra("nombre", "Administrator");
            startActivity((iPerfil));
            finish();
        } else {
            Toast.makeText(this, "Las credenciales estan incorrectas.", Toast.LENGTH_SHORT).show();
        }
     }
     */

     public void OlvidarContraseña() {

     }
}