package com.example.proyectotelepizza

import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.adapter.ProductoAdapter
import com.example.proyectotelepizza.databinding.ActivityInicioBinding
import com.google.firebase.firestore.FirebaseFirestore

class MostrarActivity : ActivityWhitMenus() {
    private lateinit var listaOfertas: ArrayList<Producto>
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recycler.addItemDecoration(decoration)

        listaOfertas = ArrayList()
        recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(listaOfertas)
        recycler.adapter = adapter

        binding.filtro.addTextChangedListener { filtro ->
            adapter.actualizarOfertas(listaOfertas.filter { oferta ->
                oferta.Nombre.lowercase().contains(filtro.toString().lowercase())
            })
        }

        // Llamada al método para cargar los datos de ofertas
        cargarDatos()
    }

    private fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Producto").get().addOnSuccessListener { cargar ->
            listaOfertas.clear() // Limpiar la lista antes de agregar los nuevos elementos
            cargar.forEach { document ->
                val pizza = document.toObject(Producto::class.java)
                listaOfertas.add(pizza)
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.e("Cargar", "Error en la obtención de personas", exception)
        }
    }
}

