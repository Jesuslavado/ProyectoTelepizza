package com.example.proyectotelepizza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.adapter.ProductoAdapter
import com.example.proyectotelepizza.databinding.ActivityInicioBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.example.proyectotelepizza.databinding.ItemOfertasBinding


class MostrarClienteActivity : ActivityWhitMenus() {
    private lateinit var listaProductos: ArrayList<Producto>
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recycler.addItemDecoration(decoration)

        listaProductos = ArrayList()
        recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(listaProductos)
        recycler.adapter = adapter

        binding.filtro.addTextChangedListener { filtro ->
            adapter.actualizarOfertas(listaProductos.filter { producto ->
                producto.Tamaño.lowercase().contains(filtro.toString().lowercase())
            })
        }

        cargarDatos()

        // Obtener una referencia al botón bcomprar desde el archivo XML "activity_inicio.xml"
        val botonComprarBinding = obtenerReferenciaBoton(binding)
        // Configurar el OnClickListener para el botón bcomprar
        botonComprarBinding.bcomprar.setOnClickListener {
            // Verificar si hay productos en la lista
            if (listaProductos.isNotEmpty()) {
                // Obtener el producto seleccionado (por ejemplo, el primer elemento de la lista)
                val productoSeleccionado = listaProductos[0]
                // Agregar el producto al carrito
                addToCart(productoSeleccionado)
            } else {
                // Manejar el caso en el que no haya productos disponibles
                Log.e("MostrarClienteActivity", "No hay productos disponibles para agregar al carrito")
            }
        }
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

    private fun addToCart(producto: Producto) {
        val intent = Intent(this, CarroComprasActivity::class.java)
        // Agregar el producto al intent
        intent.putExtra("producto", producto)
        startActivity(intent)
    }

    // Método para obtener referencia al botón desde el archivo XML "activity_inicio.xml"
    private fun obtenerReferenciaBoton(binding: ActivityInicioBinding): ItemOfertasBinding {
        // Inflar el archivo XML "activity_inicio.xml"
        val binding = ItemOfertasBinding.inflate(layoutInflater)
        return binding
    }
}