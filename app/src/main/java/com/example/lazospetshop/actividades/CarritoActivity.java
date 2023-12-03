package com.example.lazospetshop.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazospetshop.R;
import com.example.lazospetshop.adaptadores.ProductoCarritoAdapter;
import com.example.lazospetshop.clases.ProductosCarrito;
import com.example.lazospetshop.clases.ServicioCarrito;
import com.example.lazospetshop.sqlite.LazosPetShop;

import java.util.List;

public class CarritoActivity extends AppCompatActivity implements View.OnClickListener {
    LazosPetShop bd;
    private RecyclerView recyclerView;
    private ProductoCarritoAdapter adapter;
    private List<ProductosCarrito> listaProductosCarrito;
    private List<ServicioCarrito> listaServiciosCarrito;

    private Button btnVolCarrito,btnComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        bd = new LazosPetShop(getApplicationContext());

        btnVolCarrito = findViewById(R.id.BtnSeguirCompra);
        btnComprar = findViewById(R.id.BtnOrdenarCompra);

        btnVolCarrito.setOnClickListener(this);
        btnComprar.setOnClickListener(this);

        // Inicializar el RecyclerView y su adaptador
        recyclerView = findViewById(R.id.recyclerViewCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int idUsuario = obtenerIdUsuario();

        int idCarrito = obtenerIdCarrito(idUsuario);

        listaProductosCarrito = obtenerProductosCarrito(idCarrito);
        listaServiciosCarrito = obtenerServiciosCarrito(idCarrito);

        for (ServicioCarrito serv: listaServiciosCarrito
             ) {
            ProductosCarrito prod = new ProductosCarrito();
            prod.setNombreProducto(serv.getNombreServicio());
            prod.setCantidad(1);
            prod.setSubTotal(serv.getSubTotal());
            prod.setImagen(serv.getImagen());
            listaProductosCarrito.add(prod);
        }

        // Configurar el adaptador
        adapter = new ProductoCarritoAdapter(listaProductosCarrito, this);
        recyclerView.setAdapter(adapter);
    }

    private int obtenerIdUsuario() {
        return bd.obtenerIdUsuario();
    }

    private int obtenerIdCarrito(int idUsuario) {
        return bd.obtenerIdCarrito(idUsuario);
    }

    private List<ProductosCarrito> obtenerProductosCarrito(int idCarrito) {
        return bd.obtenerProductosCarrito(idCarrito);
    }

    private List<ServicioCarrito> obtenerServiciosCarrito(int idCarrito) {
        return bd.obtenerServiciosCarrito(idCarrito);
    }

    @Override
    public void onClick(View view) {
        Intent dPagoActivity = null;
        switch (view.getId()){
            case R.id.BtnSeguirCompra:
                Intent iSegCompra = new Intent(this, PerfilActivity.class);
                //iProductos.putExtra("nombre", "Cliente");
                startActivity(iSegCompra);
                break;
            case R.id.BtnOrdenarCompra:
                finish();
                dPagoActivity = new Intent(getApplicationContext(), DetallePagoActivity.class);
                startActivity(dPagoActivity);
        }
    }
}
