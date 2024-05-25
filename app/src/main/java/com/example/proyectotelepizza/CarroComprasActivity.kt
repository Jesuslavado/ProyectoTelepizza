package com.example.proyectotelepizza

import ClienteAdapter
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectotelepizza.databinding.ActivityCarroComprasBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Calendar

class CarroComprasActivity : ActivityWhitMenus() {

        private lateinit var binding: ActivityCarroComprasBinding
        private lateinit var adapter: ClienteAdapter
        private lateinit var sharedPreferences: SharedPreferences
        private var productosEnCarrito = ArrayList<Producto>()
        private lateinit var db: FirebaseFirestore
        private lateinit var currentUserEmail: String



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

            // Inicializar Firebase Firestore
            db = FirebaseFirestore.getInstance()

            // Obtener correo electrónico del usuario actual
            currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

            setupRecyclerView()

            binding.bcomprar.setOnClickListener {
                realizarPedido()
            }

            // Configurar el OnClickListener para el botón de eliminar todos los productos
            binding.beliminarproducto.setOnClickListener {
                eliminarTodosLosProductos()
            }

            // Configurar el OnClickListener para el botón de comprar
            binding.bcomprar.setOnClickListener {
                realizarPago()
                realizarPedido()

            }

            // Calcular y mostrar el precio total
            mostrarPrecioTotal()

        }




    private fun setupRecyclerView() {
            binding.rvListaCarro.layoutManager = LinearLayoutManager(this)
            adapter = ClienteAdapter(productosEnCarrito) {
                mostrarPrecioTotal()
            }
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

    private fun realizarPedido() {
        // Calcular el total del pedido
        val total = productosEnCarrito.sumByDouble { it.Precio.toDouble() }.toFloat()

        // Crear un nuevo pedido con el correo del usuario actual
        val pedido = hashMapOf(
            "correoUsuario" to currentUserEmail,
            "total" to total,
            "productos" to productosEnCarrito,
            "fecha" to obtenerFechaActual() // Agregar fecha y hora actual al pedido
        )

        // Generar un ID basado en la fecha actual
        val fechaActual = obtenerFechaActualSinEspacios() // Eliminar espacios para usar como ID
        // Guardar el pedido en Firestore con la fecha como ID
        db.collection("Pedidos")
            .document(fechaActual) // Usar la fecha como ID del documento
            .set(pedido)
            .addOnSuccessListener {
                Toast.makeText(this, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show()
                // Limpiar el carrito después de realizar el pedido
                productosEnCarrito.clear()
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al realizar el pedido", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para obtener la fecha y hora actual en el formato deseado
    private fun obtenerFechaActual(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    // Método para obtener la fecha y hora actual sin espacios
    private fun obtenerFechaActualSinEspacios(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd-(HH:mm:ss)", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    // Método para limpiar el carrito
     fun limpiarCarrito() {
        // Limpiar la lista de productos en el carrito
        productosEnCarrito.clear()
        // Guardar la lista vacía en SharedPreferences
        val gson = Gson()
        val carritoJson = gson.toJson(productosEnCarrito)
        val editor = sharedPreferences.edit()
        editor.putString("carritoProductos", carritoJson)
        editor.apply()

        // Actualizar el precio total después de limpiar el carrito
        mostrarPrecioTotal()
    }
    }
