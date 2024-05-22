package com.example.proyectotelepizza

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectotelepizza.adapter_Cliente.ClienteAdapter
import com.example.proyectotelepizza.databinding.ActivityCarroComprasBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarroComprasActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityCarroComprasBinding
    private lateinit var adapter: ClienteAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var productosEnCarrito = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarroComprasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("carrito", Context.MODE_PRIVATE)

        // Obtener los productos del carrito desde SharedPreferences
        val gson = Gson()
        val carritoJson = sharedPreferences.getString("carritoProductos", "[]")
        val carritoListType = object : TypeToken<MutableList<Producto>>() {}.type
        productosEnCarrito = gson.fromJson(carritoJson, carritoListType) ?: arrayListOf()

        setupRecyclerView()

        // Configurar el OnClickListener para el botón de eliminar todos los productos
        binding.beliminarproducto.setOnClickListener {
            eliminarTodosLosProductos()
        }

        binding.bcomprar.setOnClickListener {
            // Abrir la actividad para mostrar la imagen del código QR
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivity(intent)
        }
        // Calcular y mostrar el precio total
        mostrarPrecioTotal()
    }

    private fun setupRecyclerView() {
        binding.rvListaCarro.layoutManager = LinearLayoutManager(this)
        adapter = ClienteAdapter(productosEnCarrito)
        binding.rvListaCarro.adapter = adapter
    }

    // Método para eliminar todos los productos del carrito
    private fun eliminarTodosLosProductos() {
        if (productosEnCarrito.isNotEmpty()) {
            // Limpiar la lista de productos en el carrito
            productosEnCarrito.clear()
            // Notificar al adaptador del cambio
            adapter.actualizarProductos(productosEnCarrito)
            // Guardar la lista vacía en SharedPreferences
            val gson = Gson()
            val carritoJson = gson.toJson(productosEnCarrito)
            val editor = sharedPreferences.edit()
            editor.putString("carritoProductos", carritoJson)
            editor.apply()

            // Actualizar el precio total después de eliminar todos los productos
            mostrarPrecioTotal()
        }
    }

    // Método para mostrar el precio total de los productos en el carrito
    private fun mostrarPrecioTotal() {
        var precioTotal = 0f
        for (producto in productosEnCarrito) {
            producto.Precio.toFloatOrNull()?.let {
                precioTotal += it
            }
        }
        binding.tvTotal.text = "Total: $precioTotal" // Mostrar el precio total en el TextView
    }



}
