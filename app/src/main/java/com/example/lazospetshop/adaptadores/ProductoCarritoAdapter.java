package com.example.lazospetshop.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazospetshop.R;
import com.example.lazospetshop.clases.ProductosCarrito;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class ProductoCarritoAdapter extends RecyclerView.Adapter<ProductoCarritoAdapter.ProductoCarritoViewHolder> {

    private List<ProductosCarrito> listaProductosCarrito;
    private Context context;

    public ProductoCarritoAdapter(List<ProductosCarrito> listaProductosCarrito, Context context) {
        this.listaProductosCarrito = listaProductosCarrito;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoCarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito_producto, parent, false);
        return new ProductoCarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoCarritoViewHolder holder, int position) {
        ProductosCarrito producto = listaProductosCarrito.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return listaProductosCarrito.size();
    }

    public class ProductoCarritoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProducto;
        private TextView textViewNombreProducto;
        private TextView textViewCantidad;
        private TextView textViewPrecio;
        private Button buttonDisminuir;
        private Button buttonAumentar;

        public ProductoCarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProducto = itemView.findViewById(R.id.imageViewProductoCarrito);
            textViewNombreProducto = itemView.findViewById(R.id.textViewNombreProductoCarrito);
            textViewCantidad = itemView.findViewById(R.id.textViewCantidadProductoCarrito);
            textViewPrecio = itemView.findViewById(R.id.textPrecio);
            buttonDisminuir = itemView.findViewById(R.id.buttonDisminuir);
            buttonAumentar = itemView.findViewById(R.id.buttonAumentar);
        }

        public void bind(ProductosCarrito producto) {
            textViewNombreProducto.setText(producto.getNombreProducto());
            textViewCantidad.setText("Cantidad: " + producto.getCantidad());
            textViewPrecio.setText("Precio: S/. " + producto.getSubTotal());

            // Cargar la imagen del producto desde la URL utilizando Glide
            Glide.with(context)
                    .load(producto.getImagen())
                    .apply(new RequestOptions().override(450, 450))
                    .into(imageViewProducto);

            // Configurar clics de botones si es necesario
            buttonDisminuir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lógica para disminuir la cantidad
                }
            });

            buttonAumentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lógica para aumentar la cantidad
                }
            });
        }
    }
}
