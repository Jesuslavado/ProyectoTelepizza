package com.example.proyectotelepizza

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
    }

    private fun setupRecyclerView() {
        binding.rvListaCarro.layoutManager = LinearLayoutManager(this)
        adapter = ClienteAdapter(productosEnCarrito)
        binding.rvListaCarro.adapter = adapter
    }

    // Método para agregar un producto al carrito
    fun agregarProductoAlCarrito(producto: Producto) {
        // Agregar el nuevo producto a la lista
        productosEnCarrito.add(producto)

        // Guardar la lista actualizada en SharedPreferences
        val gson = Gson()
        val carritoJson = gson.toJson(productosEnCarrito)
        val editor = sharedPreferences.edit()
        editor.putString("carritoProductos", carritoJson)
        editor.apply()

        // Actualizar el adaptador para mostrar el nuevo producto
        adapter.actualizarProductos(productosEnCarrito)
    }
}