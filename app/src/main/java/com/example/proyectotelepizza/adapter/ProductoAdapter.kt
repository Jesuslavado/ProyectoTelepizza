package com.example.proyectotelepizza.adapter

import ProductoViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.R


class ProductoAdapter(var ofertaslist:List<Producto>): RecyclerView.Adapter<ProductoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return ProductoViewHolder(layoutInflater.inflate(R.layout.item_ofertas,parent,false))
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val item= ofertaslist[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return ofertaslist.size
    }
    fun actualizarOfertas(listaOfertas: List<Producto>){
        this.ofertaslist=listaOfertas
        notifyDataSetChanged()
    }

}