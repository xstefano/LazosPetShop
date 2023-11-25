package com.example.lazospetshop.actividades;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.Hash;
import com.example.lazospetshop.sqlite.LazosPetShop;

public class ConfiguracionActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtNuevaClave, txtConfirmarClave;
    Button btnCerrarSesion, btnActualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        txtNuevaClave = findViewById(R.id.CfgtxtNuevaClave);
        txtConfirmarClave = findViewById(R.id.CfgtxtConfirmarClave);
        btnCerrarSesion = findViewById(R.id.CfgBtnCerrarSesion);
        btnActualizar = findViewById(R.id.CfgBtnActualizar);

        btnCerrarSesion.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.CfgBtnCerrarSesion)
            cerrarSesion();
        else if(view.getId() == R.id.CfgBtnActualizar)
            actualizar();
    }

    private void cerrarSesion() {
        //elimina el usuario de sqlite
        LazosPetShop lazosPetShop = new LazosPetShop(getApplicationContext());
        lazosPetShop.eliminarUsuario(1);
        //eliminar el historial de actividades
        finish();
        //cargamos la actividad de inicio de sesión
        Intent inicioSesion = new Intent(getApplicationContext(), IniciarSesionActivity.class);
        startActivity(inicioSesion);
    }

    private void actualizar() {
        LazosPetShop lazosPetShop = new LazosPetShop(getApplicationContext());
        Hash hash = new Hash();
        String nuevaClave;
        if(txtNuevaClave.getText().toString().isEmpty() || txtConfirmarClave.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Las claves no pueden ser vacias", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!txtNuevaClave.getText().toString().equals(txtConfirmarClave.getText().toString())){
            Toast.makeText(getApplicationContext(), "Las claves deben ser iguales", Toast.LENGTH_SHORT).show();
            return;
        }
        nuevaClave = hash.StringToHash(txtNuevaClave.getText().toString(), "SHA1");
        lazosPetShop.actualizarDatoUsuario(1, "CLAVE", nuevaClave);

        Intent bienvenida = new Intent(getApplicationContext(), PerfilActivity.class);
        bienvenida.putExtra("nombre", "Administrador");
        startActivity(bienvenida);
    }
}