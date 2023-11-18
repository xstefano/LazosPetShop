package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lazospetshop.R;

public class ProductosActivity extends AppCompatActivity implements View.OnClickListener{
    TextView lblProducto;
    Button proBtnComida, proBtnJuguetes, proBtnRopa, proBtnComplementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        lblProducto = findViewById(R.id.perLblProductos);
        proBtnComida = findViewById(R.id.prBtnComida);
        proBtnJuguetes = findViewById(R.id.prBtnJuguete);
        proBtnRopa = findViewById(R.id.prBtnRopa);
        proBtnComplementos = findViewById(R.id.prBtnComplementos);
        lblProducto.setText("Productos");

        proBtnComida.setOnClickListener(this);
        proBtnJuguetes.setOnClickListener(this);
        proBtnRopa.setOnClickListener(this);
        proBtnComplementos.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.prBtnComida:
                Intent iComida = new Intent(this, ComidaActivity.class);
                //iProductos.putExtra("nombre", "Cliente");
                startActivity(iComida);
                break;
            case R.id.prBtnJuguete:
                Intent iJuguete = new Intent(this, ProductoJugueteActivity.class);
                //iServicio.putExtra("nombre", "Cliente");
                startActivity(iJuguete);
                break;
            case R.id.prBtnRopa:
                Intent iRopa = new Intent(this, ProductoRopaActivity.class);
                //iProductos.putExtra("nombre", "Cliente");
                startActivity(iRopa);
                break;
            case R.id.prBtnComplementos:
                Intent iComplemento = new Intent(this, ProductoComplementosActivity.class);
                //iServicio.putExtra("nombre", "Cliente");
                startActivity(iComplemento);
                break;
        }
    }


}