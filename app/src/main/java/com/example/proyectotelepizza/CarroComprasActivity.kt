package com.example.proyectotelepizza

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
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

        // Configurar el OnClickListener para el botón de comprar
        binding.bcomprar.setOnClickListener {
            realizarPago()
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

    // Método para realizar el pago
    private fun realizarPago() {
        val precioTotal = binding.tvTotal.text.toString().replace("Total: ", "").toFloatOrNull()

        val url = when {
            precioTotal == 6.95f -> "https://buy.stripe.com/test_8wMeXDgmBeKOfHqfZ0"
            precioTotal == 8.95f -> "https://buy.stripe.com/test_9AQ6r76M1fOS9j2bIJ"
            precioTotal == 12.95f -> "https://buy.stripe.com/test_5kA4iZ2vL8mqeDm8ww"
            precioTotal ?: 0f > 12.95f -> "https://revolut.me/jesuspg42"
            else -> null
        }

        if (url != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else {
            Toast.makeText(this, "No hay un URL de pago disponible para el total: $precioTotal", Toast.LENGTH_SHORT).show()
        }
    }
}
