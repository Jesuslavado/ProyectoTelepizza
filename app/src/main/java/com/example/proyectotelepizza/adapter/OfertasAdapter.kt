package com.example.proyectotelepizza.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.Ofertas
import com.example.proyectotelepizza.R

class OfertasAdapter(var ofertaslist:List<Ofertas>): RecyclerView.Adapter<OfertasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertasViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return OfertasViewHolder(layoutInflater.inflate(R.layout.item_ofertas,parent,false))
    }

    override fun onBindViewHolder(holder: OfertasViewHolder, position: Int) {
        val item= ofertaslist[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return ofertaslist.size
    }
    fun actualizarOfertas(listaOfertas: List<Ofertas>){
        this.ofertaslist=listaOfertas
        notifyDataSetChanged()
    }

}