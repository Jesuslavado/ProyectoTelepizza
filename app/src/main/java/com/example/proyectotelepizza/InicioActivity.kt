package com.example.proyectotelepizza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectotelepizza.adapter.OfertasAdapter
import com.example.proyectotelepizza.databinding.ActivityInicioBinding

class InicioActivity : ActivityWhitMenus() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val decoration= DividerItemDecoration(this,LinearLayoutManager.VERTICAL)


        binding.recycler.layoutManager= LinearLayoutManager(this)
        binding.recycler.adapter= OfertasAdapter(OfertasProvider.pizzaList)
        binding.recycler.addItemDecoration(decoration)
        var adapter=OfertasAdapter(OfertasProvider.pizzaList)
        binding.recycler.adapter= adapter

        // Vamos a realizar el codigo para crear el filtro
       binding.filtro.addTextChangedListener { filto->
           val filtroOfertas= OfertasProvider.pizzaList.filter { ofertas ->
               ofertas.nombre.contains(filto.toString())
           }
       }
        binding.filtro.addTextChangedListener {filtro ->
            val filtroOfertas = OfertasProvider.pizzaList.filter { ofertas ->
                ofertas.nombre.contains(filtro.toString()) }
            adapter.actualizarOfertas(filtroOfertas)

        }

        binding.filtro.addTextChangedListener {filtro ->
            val filtroOfertas = OfertasProvider.pizzaList.filter { ofertas ->
                ofertas.nombre.lowercase().contains(filtro.toString().lowercase()) }
            adapter.actualizarOfertas(filtroOfertas)
        }
    }
    }
