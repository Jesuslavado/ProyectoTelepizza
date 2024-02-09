package com.example.proyectotelepizza.adapter

import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectotelepizza.Ofertas
import com.example.proyectotelepizza.databinding.ItemOfertasBinding
import com.google.firebase.firestore.FirebaseFirestore

class OfertasViewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val binding= ItemOfertasBinding.bind(view)
            fun render(ofertasModel:Ofertas)
            {
                binding.nombre.text = ofertasModel.Nombre
                binding.ingredientes.text = ofertasModel.Ingredientes
                binding.precio.text = ofertasModel.Precio
                binding.tamano.text = ofertasModel.Tamaño
            }
}