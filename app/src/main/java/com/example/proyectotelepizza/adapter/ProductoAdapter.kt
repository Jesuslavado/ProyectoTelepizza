package com.example.proyectotelepizza.adapter

import ProductoViewHolder
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.CarroComprasActivity
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductoAdapter(
    var ofertaslist: List<Producto>,
    var carroCompras: List<Producto>
) : RecyclerView.Adapter<ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(layoutInflater.inflate(R.layout.item_ofertas, parent, false))
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val item = ofertaslist[position]
        holder.render(item)

        holder.binding.bcomprar.setOnClickListener {
            val context = holder.itemView.context
            val sharedPreferences = context.getSharedPreferences("carrito", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()

            // Obtener la lista actual de productos en el carrito
            val carritoJson = sharedPreferences.getString("carritoProductos", "[]")
            val carritoListType = object : TypeToken<MutableList<Producto>>() {}.type
            val carrito: MutableList<Producto> = gson.fromJson(carritoJson, carritoListType) ?: mutableListOf()

            // Añadir el nuevo producto al carrito
            carrito.add(ofertaslist[position])

            // Guardar la lista actualizada en SharedPreferences
            editor.putString("carritoProductos", gson.toJson(carrito))
            editor.commit() // Usa commit() en lugar de apply() para asegurar que se guarde antes de proceder
            Toast.makeText(context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return ofertaslist.size
    }

    // Actualiza la lista de ofertas y notifica al adaptador para que refresque la vista
    fun actualizarOfertas(listaOfertas: List<Producto>) {
        this.ofertaslist = listaOfertas
        notifyDataSetChanged()
    }
}
