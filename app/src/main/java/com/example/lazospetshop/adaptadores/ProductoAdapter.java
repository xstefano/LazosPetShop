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
import com.example.lazospetshop.clases.Producto;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private Context context;

    public ProductoAdapter(List<Producto> listaProductos, Context context) {
        this.listaProductos = listaProductos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProducto;
        private TextView textViewNombreProducto;
        private Button buttonPrecio;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProducto = itemView.findViewById(R.id.imageViewProducto);
            textViewNombreProducto = itemView.findViewById(R.id.textViewNombreProducto);
            buttonPrecio = itemView.findViewById(R.id.buttonPrecio);
        }

        public void bind(Producto producto) {
            // Configura la vista con los datos del producto
            textViewNombreProducto.setText(producto.getNombre());
            buttonPrecio.setText("S/. " + producto.getPrecio());

            // Carga la imagen usando Glide (asegúrate de agregar las dependencias en build.gradle)
            Glide.with(context)
                    .load(producto.getImagen())
                    .apply(new RequestOptions().override(450, 450))
                    .into(imageViewProducto);
        }
    }
}
