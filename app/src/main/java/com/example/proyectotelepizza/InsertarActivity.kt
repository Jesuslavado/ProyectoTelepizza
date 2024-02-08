package com.example.proyectotelepizza

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityInsertarBinding
import com.google.firebase.firestore.FirebaseFirestore

class InsertarActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityInsertarBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lista de tamaños
        val tamanos = arrayOf("Pequeña", "Mediana", "Familiar")

        // Adaptador para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tamanos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Configurar el adaptador en el Spinner
        binding.Itamano.adapter = adapter

        binding.binsertar.setOnClickListener {
            val nombre = binding.Inombre.text.toString()
            val id = binding.Iid.text.toString()
            val ingredientes = binding.Iingredientes.text.toString()
            val tamano = binding.Itamano.selectedItem.toString()
            val precio = binding.Iprecio.text.toString()

            // Log para verificar valores
            Log.d("InsertarActivity", "Nombre: $nombre, ID: $id, Ingredientes: $ingredientes, Tamaño: $tamano, Precio: $precio")

            // Verificar que el Spinner no esté vacío (seleccionado)
            if (id.isNotEmpty() && nombre.isNotEmpty() && ingredientes.isNotEmpty() && tamano.isNotEmpty() && precio.isNotEmpty()) {
                db.collection("Producto").document(id).set(
                    mapOf(
                        "Nombre" to nombre,
                        "Ingredientes" to ingredientes,
                        "Tamaño" to tamano,
                        "Precio" to precio
                    )
                ).addOnSuccessListener {
                    showToast("Se ha insertado la pizza")
                    clearFields()
                }.addOnFailureListener { exception ->
                    // Manejar errores al insertar en Firebase Firestore
                    Log.e("InsertarActivity", "Error al insertar en Firestore: $exception")
                    showToast("Error al insertar la pizza. Consulta los registros para más detalles.")
                }
            } else {
                showToast("Error: Algún campo está vacío")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun clearFields() {
        binding.Inombre.text.clear()
        binding.Iid.text.clear()
        binding.Iingredientes.text.clear()
        // No es necesario limpiar el Spinner, ya que se selecciona nuevamente en cada inserción
        binding.Iprecio.text.clear()
    }
}
