package com.example.lazospetshop.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazospetshop.R;

public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtCorreo, txtContraseña;
    Button btnIniciar, btnRegistrate;
    TextView lblOlvidarContraseña;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        txtCorreo = findViewById(R.id.logTxtCorreo);
        txtContraseña = findViewById(R.id.logTxtContraseña);
        btnIniciar = findViewById(R.id.logBtnIniciar);
        btnRegistrate = findViewById(R.id.logBtnRegistrate);
        lblOlvidarContraseña = findViewById(R.id.logLblOlvidarContraseña);

        btnIniciar.setOnClickListener(this);
        btnRegistrate.setOnClickListener(this);
        lblOlvidarContraseña.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
      /*  if (view.getId() == R.id.logBtnIniciar){
            IniciarSesion(txtCorreo.getText().toString(), txtContraseña.getText().toString());
        } else if (view.getId() == R.id.logBtnRegistrate) {
            Registrate();
        } else if (view.getId() == R.id.logLblOlvidarContraseña) {
            OlvidarContraseña();;
        }
        */
       switch(view.getId()){
           case R.id.logBtnIniciar:
               login();
               break;
           case R.id.logBtnRegistrate:
               registrate();
               break;
       }
    }

    private void login() {
       String correo= txtCorreo.getText().toString().trim();
       String pass=txtContraseña.getText().toString().trim();
       if(correo.isEmpty() || pass.isEmpty()){
           Toast.makeText(this,"Ingresar datos correctamente",Toast.LENGTH_SHORT).show();
           return;
       }
       iniciarSesion(correo,pass);
    }

    private void iniciarSesion(String correo, String pass) {

        correo = correo.toLowerCase().trim();
        pass = pass.toLowerCase().trim();

        if(correo.equals("admin@gmail.com")&& pass.equals("12345")){
                Intent iPerfil = new Intent(this, PerfilActivity.class);
                iPerfil.putExtra("nombre", "Administrador");
                startActivity(iPerfil);
            }
            else{
                Toast.makeText(this, "Creendeciales incorrectas", Toast.LENGTH_SHORT).show();
            }
    }
    public void registrate(){
        /*Intent iRegistrate = new Intent(this, RegistrarActiviy.class);
        startActivities((iRegistrate));
        finish();*/
        Intent iRegistrar = new Intent(this, RegistroActivity.class);
        startActivity(iRegistrar);
        finish();
    }


   /*
    public void IniciarSesion(String correo, String contraseña) {

        correo = correo.toLowerCase().trim();
        contraseña = contraseña.toLowerCase().trim();

        if (correo.equals("admin@gmail.com") && contraseña.equals("12345")) {
            Intent iPerfil = new Intent(this, PerfilActivity.class);
            iPerfil.putExtra("nombre", "Administrator");
            startActivity((iPerfil));
            finish();
        } else {
            Toast.makeText(this, "Las credenciales estan incorrectas.", Toast.LENGTH_SHORT).show();
        }
     }
     */

     public void OlvidarContraseña() {

     }
}