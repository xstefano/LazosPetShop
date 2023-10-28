package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lazospetshop.R;

public class ProductosActivity extends AppCompatActivity{
    TextView lblProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        lblProducto = findViewById(R.id.perLblProductos);
        lblProducto.setText("Productos " + getIntent().getStringExtra("nombre"));


    }


}