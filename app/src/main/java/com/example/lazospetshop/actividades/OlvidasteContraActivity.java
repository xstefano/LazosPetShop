package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lazospetshop.R;

public class OlvidasteContraActivity extends AppCompatActivity implements View.OnClickListener {

    Button OlvBtnVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidaste_contra);

        OlvBtnVolver = findViewById(R.id.BtnOlvVolver);
        OlvBtnVolver.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.BtnOlvVolver:
                Intent iVolverP = new Intent(this, IniciarSesionActivity.class);
                startActivity(iVolverP);
                break;
        }
    }
}