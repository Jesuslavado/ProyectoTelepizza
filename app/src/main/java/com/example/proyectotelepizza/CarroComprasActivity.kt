package com.example.proyectotelepizza

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectotelepizza.adapter.ProductoAdapter
import com.example.proyectotelepizza.databinding.ActivityCarroComprasBinding

class CarroComprasActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityCarroComprasBinding
    private lateinit var adapter: ProductoAdapter

    private val carroCompras = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarroComprasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si el producto se pasó correctamente a través del Intent
        val producto = intent.getSerializableExtra("producto") as? Producto
        if (producto != null) {
            // Agregar el producto al carrito
            carroCompras.add(producto)
        }

        // Configurar el RecyclerView
        binding.rvListaCarro.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapter(carroCompras)
        binding.rvListaCarro.adapter = adapter
    }
}