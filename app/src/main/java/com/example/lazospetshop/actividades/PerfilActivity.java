package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lazospetshop.R;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btnProductos,btnServicios,btnMascota;
    TextView lblSaludo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        btnProductos = findViewById(R.id.perNuestrosProductos);
        btnServicios = findViewById(R.id.perSolicitaServicios);
        btnMascota = findViewById(R.id.perRegistrarMascota);
        lblSaludo = findViewById(R.id.perLblSaludo);
        lblSaludo.setText("Bienvenido " + getIntent().getStringExtra("nombre"));
        btnProductos.setOnClickListener(this);
        btnServicios.setOnClickListener(this);
        btnMascota.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        System.out.println("entre");
        switch(view.getId()){
            case R.id.perNuestrosProductos:
                Intent iProductoRopa = new Intent(this, ProductoRopaActivity.class);
                iProductoRopa.putExtra("nombre", "Cliente");
                startActivity(iProductoRopa);
                break;
            case R.id.perSolicitaServicios:
                Intent iServicio = new Intent(this, RegistrarServicioActivity.class);
                startActivity(iServicio);
            case R.id.perRegistrarMascota:
                Intent iMascota = new Intent(this, RegistraMascotaActivity.class);
                startActivity(iMascota);
        }
    }
}