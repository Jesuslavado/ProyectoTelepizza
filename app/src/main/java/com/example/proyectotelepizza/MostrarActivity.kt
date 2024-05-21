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
    // Lista para almacenar los productos a mostrar
    private lateinit var listaProductos: ArrayList<Producto>
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Agrega una línea divisoria entre los elementos del RecyclerView
        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recycler.addItemDecoration(decoration)

        // Inicialización de la lista de productos y configuración del RecyclerView
        listaProductos = ArrayList()
        recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)

        // Inicialización del adaptador y asignación al RecyclerView
        adapter = ProductoAdapter(listaProductos,emptyList())
        recycler.adapter = adapter

        // Agrega un listener al campo de filtro para realizar búsquedas dinámicas
        binding.filtro.addTextChangedListener { filtro ->
            // Actualiza la lista de productos en el adaptador según el filtro ingresado
            adapter.actualizarOfertas(listaProductos.filter { producto ->
                producto.Tamaño.lowercase().contains(filtro.toString().lowercase())
            })
        }

        // Llamada al método para cargar los datos de productos
        cargarDatos()
    }

    // Método para cargar los datos de productos desde Firestore
    private fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Producto").get().addOnSuccessListener { carga ->
            // Limpia la lista antes de agregar los nuevos elementos
            listaProductos.clear()

            // Itera sobre los documentos obtenidos y los agrega a la lista de productos
            carga.forEach { document ->
                val producto = document.toObject(Producto::class.java)
                listaProductos.add(producto)
            }

            // Notifica al adaptador que se han actualizado los datos
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            // Maneja el error en caso de falla al obtener los productos desde Firestore
            Log.e("Cargar", "Error en la obtención de productos", exception)
        }
    }
}
