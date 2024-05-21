// ClienteViewHolder.kt
package com.example.proyectotelepizza.adapter_Cliente

import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.databinding.ItemCarroCompraBinding

class ClienteViewHolder(private val binding: ItemCarroCompraBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(producto: Producto) {
        binding.tvNomProducto.text = producto.Nombre
        binding.tvPrecio.text = producto.Precio

    }
}
