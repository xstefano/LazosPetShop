package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lazospetshop.R;

public class PerfilActivity extends AppCompatActivity {


    TextView lblSaludo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //lblSaludo = findViewById(R.id.);
        lblSaludo.setText("Bienvenido " + getIntent().getStringExtra("nombre"));
    }
}