package com.example.proyectotelepizza.adapter

import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectotelepizza.Ofertas
import com.example.proyectotelepizza.databinding.ItemOfertasBinding

class OfertasViewHolder(view: View):RecyclerView.ViewHolder(view) {
    val binding= ItemOfertasBinding.bind(view)
            fun render(ofertasmodel:Ofertas)
            {
                binding.nombreOferta.text= ofertasmodel.nombre
                binding.productoOferta.text= ofertasmodel.productos
                binding.PrecioOferta.text= ofertasmodel.precio.toString()
                binding.diaOferta.text=ofertasmodel.dia_oferta

                Glide.with(binding.fotooferta.context).load(ofertasmodel.photo).into(binding.fotooferta)
                // EVENTO HACER CLICK SOBRE LA IMAGEN DE UNA PELICULA
                binding.fotooferta.setOnClickListener{
                    Toast.makeText(binding.fotooferta.context, ofertasmodel.nombre, Toast.LENGTH_LONG).show()
                }
                // EVENTO HACER CLICK SOBRE TODA LA PELICULA
                itemView.setOnClickListener{
                    Toast.makeText(binding.fotooferta.context, ofertasmodel.nombre, Toast.LENGTH_LONG).show()
                }

            }
}