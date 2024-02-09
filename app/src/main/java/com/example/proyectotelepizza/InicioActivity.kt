package com.example.proyectotelepizza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.adapter.OfertasAdapter
import com.example.proyectotelepizza.databinding.ActivityInicioBinding
import com.google.firebase.firestore.FirebaseFirestore

class InicioActivity : ActivityWhitMenus() {
    private lateinit var listaOfertas: ArrayList<Ofertas>
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: OfertasAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val decoration= DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        binding.recycler.addItemDecoration(decoration)

        binding.filtro.addTextChangedListener {filtro ->
            val filtroOfertas = OfertasProvider.pizzaList.filter { ofertas ->
                ofertas.Nombre.lowercase().contains(filtro.toString().lowercase())
            }
            adapter.actualizarOfertas(filtroOfertas)

        }

        listaOfertas = ArrayList()
        recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)

        adapter= OfertasAdapter(listaOfertas)
        recycler.adapter = adapter

        // Llamada al método para cargar los datos de ofertas
        cargarDatos()
    }

    private fun cargarDatos() {
        val db= FirebaseFirestore.getInstance()

        db.collection("Producto").get().addOnSuccessListener {
            cargar-> cargar.forEach { document->
            val pizza = document.toObject(Ofertas::class.java)
            listaOfertas.add(pizza)
        }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener{
            exception->
            Log.e("Cargar", "Error en la obtención de personas",exception)
        }
    }
}
