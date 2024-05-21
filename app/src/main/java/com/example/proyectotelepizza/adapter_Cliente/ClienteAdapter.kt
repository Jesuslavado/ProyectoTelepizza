package com.example.proyectotelepizza.adapter_Cliente

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.databinding.ItemCarroCompraBinding

class ClienteAdapter(var productosEnCarrito: List<Producto>) : RecyclerView.Adapter<ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCarroCompraBinding.inflate(layoutInflater, parent, false)
        return ClienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val item = productosEnCarrito[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return productosEnCarrito.size
    }

    // Método para actualizar la lista de productos en el adaptador
    fun actualizarProductos(productos: List<Producto>) {
        productosEnCarrito = productos
        notifyDataSetChanged()
    }
}
