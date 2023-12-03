package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.TipoMascota;
import com.example.lazospetshop.sqlite.LazosPetShop;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RegistraMascotaActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String urlControllerObtenerRaza = "https://lazospetshop.azurewebsites.net/api/tipomascota/obtenertodos";
    private final static String urlControllerMascota = "https://lazospetshop.azurewebsites.net/api/mascota/registrar";
    private static final int PICK_IMAGE_REQUEST = 1;
    Button btnRegistrarMascota, btnSubirImagenMascota;
    private ImageView imagenView;
    private String ConvertidorMascota;

    EditText txtNombreMascota;

    RadioGroup rgrSexo;

    private Spinner spiTipoRaza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_mascota);

        btnRegistrarMascota = findViewById(R.id.rgBtnMascota);
        btnSubirImagenMascota = findViewById(R.id.BtnSubirImagenMascota);
        spiTipoRaza = findViewById(R.id.spiTipoRaza);
        txtNombreMascota = findViewById(R.id.txtNombreMascota);
        rgrSexo = findViewById(R.id.rbgSexo);
        btnRegistrarMascota.setOnClickListener(this);
        btnSubirImagenMascota.setOnClickListener(this);
        imagenView = findViewById(R.id.imagenView);
        cargarRaza();
        // Abre la galería de imágenes al hacer clic en el botón "Subir Imagen"
        findViewById(R.id.BtnSubirImagenMascota).setOnClickListener(v -> openGallery());

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
            imagenView.setImageURI(uri);

            // Convierte la imagen a Base64
            ConvertidorMascota = convertImageToBase64(uri);

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
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.rgBtnMascota:
                /*Intent iRegistraMascota = new Intent(this, PerfilActivity.class);
                startActivity(iRegistraMascota);*/
                registrarMascota();
                break;
        }
    }

    private void registrarMascota(){
        AsyncHttpClient ahcRegistrarMascota = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        LazosPetShop bd = new LazosPetShop(getApplicationContext());
        Integer idUsuario = bd.obtenerIdUsuario();
        int rbtSexo;
        int idRaza = spiTipoRaza.getSelectedItemPosition() + 1;
        //Toast.makeText(getApplicationContext(),idRaza+"",Toast.LENGTH_SHORT).show();
        try{
            jsonParams.put("nombre",txtNombreMascota.getText().toString());
            jsonParams.put("tipoMascotaId",String.valueOf(idRaza));
            jsonParams.put("usuarioId",idUsuario);

            rbtSexo = rgrSexo.getCheckedRadioButtonId();

            if(rbtSexo == R.id.radio_femenino){
                jsonParams.put("sexo","Hembra");
            }
            else if(rbtSexo == R.id.radio_masculino){
                jsonParams.put("sexo","Macho");
            }
            jsonParams.put("imagen",ConvertidorMascota);//String.valueOf(cboDocumento.getSelectedItemPosition()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ahcRegistrarMascota.post(null,urlControllerMascota,  entity, "application/json", new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if((statusCode+"").equals("200")){
                    Toast.makeText(getApplicationContext(),"Mascota registrada!",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent iPerfil = new Intent(getApplicationContext(), PerfilActivity.class);
                    startActivity(iPerfil);
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
    private void cargarRaza(){
        AsyncHttpClient ahcCargarRaza = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ahcCargarRaza.get(null,urlControllerObtenerRaza,  entity, "application/json", new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if((statusCode+"").equals("200")){
                    try {
                        Log.d("Respuesta JSON", rawJsonResponse);
                        // Ajusta el tipo de referencia para mapear la lista de objetos
                        List<TipoMascota> tMascota = new ObjectMapper().readValue(rawJsonResponse, new TypeReference<List<TipoMascota>>() {});

                        // Crea un adaptador para el Spinner
                        ArrayAdapter<TipoMascota> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tMascota);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Asigna el adaptador al Spinner
                        spiTipoRaza.setAdapter(adapter);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

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