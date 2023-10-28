package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lazospetshop.R;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btnProductos;
    TextView lblSaludo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        btnProductos = findViewById(R.id.perNuestrosProductos);
        lblSaludo = findViewById(R.id.perLblSaludo);
        lblSaludo.setText("Bienvenido " + getIntent().getStringExtra("nombre"));
        btnProductos.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        System.out.println("entre");
        switch(view.getId()){
            case R.id.perNuestrosProductos:
                Intent iProducto = new Intent(this, ProductosActivity.class);
                iProducto.putExtra("nombre", "Cliente");
                startActivity(iProducto);
                break;
        }
    }
}