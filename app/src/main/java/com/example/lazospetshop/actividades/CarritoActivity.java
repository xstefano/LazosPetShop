package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lazospetshop.R;

public class CarritoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnVolCarrito;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        btnVolCarrito = findViewById(R.id.BtnSeguirCompra);
        btnVolCarrito.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.BtnSeguirCompra:
                Intent iSegCompra = new Intent(this, PerfilActivity.class);
                //iProductos.putExtra("nombre", "Cliente");
                startActivity(iSegCompra);
                break;
        }
    }
}