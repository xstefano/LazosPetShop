package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.lazospetshop.R;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btnProductos,btnServicios,btnMascota, btnConfiguracion, btnUbicacion;
    TextView lblSaludo;

    ImageView imaUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        btnProductos = findViewById(R.id.perNuestrosProductos);
        btnServicios = findViewById(R.id.perSolicitaServicios);
        btnMascota = findViewById(R.id.perRegistrarMascota);
        btnConfiguracion = findViewById(R.id.perSolicitaConfig);
        btnUbicacion = findViewById(R.id.perUbicacion);
        lblSaludo = findViewById(R.id.perLblSaludo);
        imaUsuario = findViewById(R.id.imaUsuario);
        btnProductos.setOnClickListener(this);
        btnServicios.setOnClickListener(this);
        btnMascota.setOnClickListener(this);
        btnConfiguracion.setOnClickListener(this);
        btnUbicacion.setOnClickListener(this);

        String idString = getIntent().getStringExtra("id");
        if (idString != null) {
            int idUsuario = Integer.parseInt(idString);
            obtenerInformacionUsuario(idUsuario);
        } else {
            // Manejo de caso en que el "id" es nulo
            // Puedes mostrar un mensaje de error, iniciar otra actividad, etc.
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.perNuestrosProductos:
                Intent iProductos = new Intent(this, ProductosActivity.class);
                //iProductos.putExtra("nombre", "Cliente");
                startActivity(iProductos);
                break;
            case R.id.perSolicitaServicios:
                Intent iServicio = new Intent(this, RegistrarServicioActivity.class);
                startActivity(iServicio);
                break;
            case R.id.perRegistrarMascota:
                Intent iMascota = new Intent(this, RegistraMascotaActivity.class);
                startActivity(iMascota);
                break;
            case R.id.perUbicacion:
                Intent iUbicacion = new Intent(this, UbicacionActivity.class);
                startActivity(iUbicacion);
                break;
            case R.id.perSolicitaConfig:
                Intent iConfiguracion = new Intent(this, ConfiguracionActivity.class);
                startActivity(iConfiguracion);
                break;
        }
    }

    private void obtenerInformacionUsuario(int idUsuario) {
        String url = "https://lazospetshop.azurewebsites.net/api/usuario/obtenerporid/" + idUsuario;
        System.out.println(idUsuario);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Configura la solicitud
                    connection.setRequestMethod("GET");
                    connection.connect();

                    // Lee la respuesta
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    inputStream.close();
                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String jsonResponse) {
                super.onPostExecute(jsonResponse);

                if (jsonResponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Obtén los valores del usuario desde el JSON
                        String nombres = jsonObject.getString("nombres");
                        String apellidos = jsonObject.getString("apellidos");
                        String correo = jsonObject.getString("correo");
                        // ... obtén otros valores

                        // Muestra la información del usuario en tu actividad
                        lblSaludo.setText("Bienvenido " + nombres + " " + apellidos);
                        // ... configura otros TextView con la información obtenida

                        // Muestra la imagen directamente desde la URL
                        String imageUrl = jsonObject.getString("imagen");
                        new CargarImagenTask(imaUsuario).execute(imageUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(url);
    }

    public class CargarImagenTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public CargarImagenTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                return Glide.with(imageView.getContext())
                        .asBitmap()
                        .load(imageUrl)
                        .apply(new RequestOptions().override(450, 450))
                        .submit()
                        .get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}