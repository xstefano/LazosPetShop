package com.example.lazospetshop.actividades;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazospetshop.R;
import com.example.lazospetshop.adaptadores.ProductoAdapter;
import com.example.lazospetshop.clases.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductoComplementosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_complementos);

        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        new ObtenerProductosTask().execute("https://lazospetshop.azurewebsites.net/api/producto/obtenertodos");
    }

    private class ObtenerProductosTask extends AsyncTask<String, Void, List<Producto>> {

        @Override
        protected List<Producto> doInBackground(String... urls) {
            List<Producto> listaProductos = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonProducto = jsonArray.getJSONObject(i);
                    int categoriaId = jsonProducto.getInt("categoriaId");

                    // Filtrar por categoria
                    if (categoriaId == 4) {
                        int id = jsonProducto.getInt("id");
                        String nombre = jsonProducto.getString("nombre");
                        double precio = jsonProducto.getDouble("precio");
                        String imagen = jsonProducto.getString("imagen");

                        Producto producto = new Producto(id, nombre, precio, imagen);
                        listaProductos.add(producto);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return listaProductos;
        }

        @Override
        protected void onPostExecute(List<Producto> listaProductos) {
            super.onPostExecute(listaProductos);

            adapter = new ProductoAdapter(listaProductos, ProductoComplementosActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }
}
