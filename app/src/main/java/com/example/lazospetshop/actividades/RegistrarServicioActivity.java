package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Carrito;
import com.example.lazospetshop.clases.Mascota;
import com.example.lazospetshop.clases.ServicioCarrito;
import com.example.lazospetshop.clases.TipoMascota;
import com.example.lazospetshop.sqlite.LazosPetShop;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RegistrarServicioActivity extends AppCompatActivity implements View.OnClickListener {
    private static String urlControllerObtenerMascota = "https://lazospetshop.azurewebsites.net/api/mascota/mascotas/";
    Spinner spiMascota;

    List<Mascota> tMascotas=null;

    ImageView imaMascota;
    Button btnAgregar;

    RadioGroup rbgTipoServicio;
    DatePicker dpFechaServicio;
    String fechaServicio="";
    Integer idMascotaServi = 0;
    String imgaMascota = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_servicio);
        spiMascota = findViewById(R.id.cboMascota);
        imaMascota = findViewById(R.id.imaMascota);
        btnAgregar = findViewById(R.id.btnAgregarServicio);
        rbgTipoServicio = findViewById(R.id.rbgTipoServicio);
        dpFechaServicio = findViewById(R.id.dtpFecha);
        // Establece un listener para detectar cambios en la fecha seleccionada
        dpFechaServicio.init(dpFechaServicio.getYear(), dpFechaServicio.getMonth(), dpFechaServicio.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Crea un objeto Calendar y establece la fecha seleccionada
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);

                    // Formatea la fecha en el formato deseado ("yyyy-MM-dd")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());

                    // Imprime la fecha formateada (puedes hacer lo que quieras con ella)
                    fechaServicio = formattedDate;
                });
        cargarRaza();
        spiMascota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                int idMascota = spiMascota.getSelectedItemPosition() ;
                //Toast.makeText(adapterView.getContext(), tMascotas.get(idMascota).getImagen(), Toast.LENGTH_SHORT).show();
                idMascotaServi = tMascotas.get(idMascota).getId();
                imgaMascota = tMascotas.get(idMascota).getImagen();
                //new CargarImagenTask(imaMascota).execute(tMascotas.get(idMascota).getImagen());
                // Muestra la imagen (asumiendo que la imagen está en Base64)
                /*String base64Image = "";
                // Convierte la cadena Base64 a un formato que ImageView pueda entender
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imaMascota.setImageBitmap(decodedByte);*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio
            }
        });
        btnAgregar.setOnClickListener(this);
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
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
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
    private void cargarRaza(){
        LazosPetShop bd = new LazosPetShop(getApplicationContext());
        AsyncHttpClient ahcCargarMascota = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        Integer idUsuario = bd.obtenerIdUsuario();
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ahcCargarMascota.get(null,urlControllerObtenerMascota+idUsuario,  entity, "application/json", new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if((statusCode+"").equals("200")){
                    try {
                        Log.d("Respuesta JSON", rawJsonResponse);
                        // Ajusta el tipo de referencia para mapear la lista de objetos
                        List<Mascota> tMascota = new ObjectMapper().readValue(rawJsonResponse, new TypeReference<List<Mascota>>() {});
                        tMascotas = tMascota;
                        // Crea un adaptador para el Spinner
                        ArrayAdapter<Mascota> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tMascota);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Asigna el adaptador al Spinner
                        spiMascota.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        Intent perfil = null;
        LazosPetShop bd = new LazosPetShop(getApplicationContext());
        int idMascota = spiMascota.getSelectedItemPosition() + 1;
        double precioUnitario = 0;
        double subTotal = 0;

        int idServicio = rbgTipoServicio.getCheckedRadioButtonId();
        if(idServicio == R.id.rbBano){
            idServicio = 1;
            precioUnitario = 15;
            subTotal = 15;
        }
        else if(idServicio == R.id.rbCorte){
            idServicio = 2;
            precioUnitario = 20;
            subTotal = 20;
        }
        else if(idServicio == R.id.rbAmbos){
            idServicio = 3;
            precioUnitario = 30;
            subTotal = 30;
        }
        Integer idUsuario = bd.obtenerIdUsuario();
        Integer idCarrito = bd.obtenerIdCarrito(idUsuario);

        bd.agregarDetalleServicio(idCarrito,precioUnitario,idServicio,subTotal,idMascotaServi,fechaServicio,imgaMascota);
        Toast.makeText(getApplicationContext(), "Servicio Registrado!", Toast.LENGTH_SHORT).show();
        bd.actualizarMontoTotalCarrito(idCarrito);
        Carrito carro = bd.obtenerCarritoPorUsuario(idUsuario);
        Toast.makeText(getApplicationContext(), carro.getMontoTotal()+"", Toast.LENGTH_SHORT).show();
        for (ServicioCarrito servCarrito: carro.getDetalleServicio()
             ) {
            Toast.makeText(getApplicationContext(), servCarrito.getIdServicio()+"" ,Toast.LENGTH_SHORT).show();
        }
        finish();
        perfil = new Intent(getApplicationContext(), PerfilActivity.class);
        startActivity(perfil);

    }
}