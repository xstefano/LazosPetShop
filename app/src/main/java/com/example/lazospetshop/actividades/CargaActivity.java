package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lazospetshop.R;

public class CargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        Thread tCarga = new Thread(){
            @Override
            public void run(){
                super.run();
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    Intent iInicioSesion = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                    startActivity(iInicioSesion);
                    finish();
                }
            }
        };
        tCarga.start();
    }
}