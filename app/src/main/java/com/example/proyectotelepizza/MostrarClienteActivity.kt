package com.example.proyectotelepizza

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.adapter.ProductoAdapter
import com.example.proyectotelepizza.databinding.ActivityInicioBinding
import com.google.firebase.firestore.FirebaseFirestore


class MostrarClienteActivity : ActivityWhitMenus() {
    private lateinit var listaProductos: ArrayList<Producto>
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("carrito", Context.MODE_PRIVATE)

        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recycler.addItemDecoration(decoration)

        listaProductos = ArrayList()
        recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(listaProductos, emptyList())
        recycler.adapter = adapter

        binding.filtro.addTextChangedListener { filtro ->
            adapter.actualizarOfertas(listaProductos.filter { producto ->
                producto.Tamaño.lowercase().contains(filtro.toString().lowercase())
            })
        }

        cargarDatos()
    }

    private fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Producto").get().addOnSuccessListener { carga ->
            listaProductos.clear()

            carga.forEach { document ->
                val producto = document.toObject(Producto::class.java)
                listaProductos.add(producto)
            }

            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.e("Cargar", "Error en la obtención de productos", exception)
        }
    }



}