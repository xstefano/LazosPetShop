package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.lazospetshop.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegistraMascotaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    Button btnRegistrarMascota, btnSubirImagenMascota;
    private ImageView imagenView;
    private String ConvertidorMascota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_mascota);

        btnRegistrarMascota = findViewById(R.id.rgBtnMascota);
        btnSubirImagenMascota = findViewById(R.id.BtnSubirImagenMascota);

        btnRegistrarMascota.setOnClickListener(this);
        btnSubirImagenMascota.setOnClickListener(this);
        imagenView = findViewById(R.id.imagenView);

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
                Intent iRegistraMascota = new Intent(this, PerfilActivity.class);
                startActivity(iRegistraMascota);
                break;
        }
    }
}