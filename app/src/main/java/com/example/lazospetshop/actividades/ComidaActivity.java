package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComidaActivity extends AppCompatActivity {

    private LinearLayout lineasLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);

        lineasLayout = findViewById(R.id.layoutComida);

        new ObtenerProductosTask().execute("https://lazospetshop.azurewebsites.net/api/producto/obtenertodos");
    }

    private class ObtenerProductosTask extends AsyncTask<String, Void, List<Producto>> {

        @Override
        protected List<Producto> doInBackground(String... urls) {
            List<Producto> listaProductos = new ArrayList<>();

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonProducto = jsonArray.getJSONObject(i);
                        int id = jsonProducto.getInt("id");
                        String nombre = jsonProducto.getString("nombre");
                        double precio = jsonProducto.getDouble("precio");
                        String imagen = jsonProducto.getString("imagen");

                        Producto producto = new Producto(id, nombre, precio, imagen);
                        listaProductos.add(producto);
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return listaProductos;
        }

        @Override
        protected void onPostExecute(List<Producto> listaProductos) {
            super.onPostExecute(listaProductos);

            mostrarProductosEnLayout(listaProductos);
        }
    }

    private void mostrarProductosEnLayout(List<Producto> listaProductos) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(2);
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        int totalProductos = listaProductos.size();
        int columnas = 2;
        int filas = (totalProductos + columnas - 1) / columnas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int index = i * columnas + j;
                if (index < totalProductos) {
                    Producto producto = listaProductos.get(index);

                    LinearLayout productoLayout = new LinearLayout(this);
                    productoLayout.setOrientation(LinearLayout.VERTICAL);
                    productoLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                    Bitmap imagenBitmap = decodeBase64(producto.getImagen());

                    ImageView imageView = new ImageView(this);
                    int width = 450;
                    int height = 450;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    imageView.setImageBitmap(imagenBitmap);

                    TextView nombreTextView = new TextView(this);
                    nombreTextView.setText(producto.getNombre());
                    nombreTextView.setGravity(Gravity.CENTER);
                    nombreTextView.setPadding(0,0, 0, 20);

                    Button precioButton = new Button(this);
                    precioButton.setText("S/. " + producto.getPrecio());
                    precioButton.setGravity(Gravity.CENTER);
                    precioButton.setTextColor(Color.WHITE);

                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(getResources().getColor(R.color.purple_500));
                    gradientDrawable.setCornerRadius(20);

                    precioButton.setBackground(gradientDrawable);
                    precioButton.setOnClickListener(this::handlePrecioButtonClick);
                    productoLayout.addView(imageView);
                    productoLayout.addView(nombreTextView);
                    productoLayout.addView(precioButton);

                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
                    layoutParams.setMargins(110, 40, 110, 40);
                    productoLayout.setLayoutParams(layoutParams);

                    gridLayout.addView(productoLayout);
                }
            }
        }

        linearLayout.addView(gridLayout);
        lineasLayout.addView(linearLayout);
    }


    private void handlePrecioButtonClick(View view) {
        // Logica para manejar el clic del boton de precio

    }


    private Bitmap decodeBase64(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}