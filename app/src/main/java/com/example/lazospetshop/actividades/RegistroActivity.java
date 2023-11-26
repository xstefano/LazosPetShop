package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.provider.MediaStore;
import android.app.Activity;
import android.widget.ImageView;
import android.net.Uri;

import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Hash;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String urlController = "https://lazospetshop.azurewebsites.net/api/usuario/registrar";
    //private final static String urlController = "http://veterinaria-upn.atwebpages.com/ws/agregarUsuario.php";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private String Convertidor;
    EditText txtNombres,txtApellidos,txtNumeroDocumento,txtCorreo,txtContrasena;
    Button btnRegistrar, btnVolver;
    RadioGroup rgrSexo;
    RadioButton rbtSinDefinir;
    RadioButton rbMasculino;
    RadioButton rbFemenino;

    Spinner cboDocumento;
    String[] valores = {"SELECCIONE","DNI","CARNE EXTRANJERIA","PASAPORTE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtNumeroDocumento = findViewById(R.id.txtnumeroDocumento);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtContra);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);

        cboDocumento = findViewById(R.id.tipoDocumento);

        cboDocumento.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));

        rgrSexo = findViewById(R.id.rbTipoConsulta);
        btnRegistrar.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);

        // Abre la galería de imágenes al hacer clic en el botón "Subir Imagen"
        findViewById(R.id.btnSubirImagen).setOnClickListener(v -> openGallery());
        cboDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtiene la URI de la imagen seleccionada
            Uri uri = data.getData();

            // Puedes cargar la imagen en un ImageView o realizar otras operaciones según tus necesidades
            imageView.setImageURI(uri);

            // Convierte la imagen a Base64
            Convertidor = convertImageToBase64(uri);

            // Puedes usar base64Image según tus necesidades (por ejemplo, enviarlo al servidor)
            Toast.makeText(this, "Imagen convertida a Base64", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertImageToBase64(Uri uri) {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    private String getImagePath(Uri uri) {
        // TODO: Implementa la lógica para obtener la ruta real de la imagen desde la URI
        // Puedes utilizar un ContentResolver para resolver la URI y obtener la ruta real.
        return null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnRegistrar){
            registrar();
        }
        else if(v.getId() == R.id.btnVolver){
            Intent iVolver = new Intent(this, IniciarSesionActivity.class);
            startActivity(iVolver);
        }
    }

    private void registrar(){
        AsyncHttpClient ahcRegistrar = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        Hash hash = new Hash();
        int rbtSexo;
        /*{
            "id": 0,
                "nombres": "string",
                "apellidos": "string",
                "correo": "string",
                "contraseña": "string",
                "tipoDocumentoId": 0,
                "numeroDocumento": "string",
                "generoId": 0
        }*/
        //params.add("id","23");
        try{
            jsonParams.put("nombres",txtNombres.getText().toString());
            jsonParams.put("apellidos",txtApellidos.getText().toString());
            jsonParams.put("correo",txtCorreo.getText().toString());
            jsonParams.put("contrasena",hash.StringToHash(txtContrasena.getText().toString(),"SHA1"));
            jsonParams.put("numeroDocumento",txtNumeroDocumento.getText().toString());
            jsonParams.put("tipoDocumentoId","1");//String.valueOf(cboDocumento.getSelectedItemPosition()));
            rbtSexo = rgrSexo.getCheckedRadioButtonId();

            if(rbtSexo == R.id.rbFemenino){
                jsonParams.put("generoId","3");
            }
            else if(rbtSexo == R.id.rbMasculino){
                jsonParams.put("generoId","2");
            }
            else if(rbtSexo == R.id.rbnoBinario){
                jsonParams.put("generoId","1");
            }
            jsonParams.put("imagen",Convertidor);//String.valueOf(cboDocumento.getSelectedItemPosition()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*try{
            jsonParams.put("nombres","test");
            jsonParams.put("apellidos","test");
            jsonParams.put("correo","test");
            jsonParams.put("contraseña","20231117");
            jsonParams.put("tipoDocumentoId","1");//String.valueOf(cboDocumento.getSelectedItemPosition()));
            jsonParams.put("numeroDocumento","20231117");
            jsonParams.put("generoId","2");
         */



        /*params.add("sexo","X");
        params.add("idDistrito","1");
        params.add("clave","123321");*/




        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ahcRegistrar.post(null,urlController,  entity, "application/json", new BaseJsonHttpResponseHandler() {
            Intent iniciarSecion = null;
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {

                iniciarSecion = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                startActivity(iniciarSecion);
                if(statusCode == 200){
                    Toast.makeText(getApplicationContext(),"Usuario registrado!",Toast.LENGTH_SHORT).show();
                    finish();
                    iniciarSecion = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                    startActivity(iniciarSecion);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error al registrar!",Toast.LENGTH_SHORT).show();
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