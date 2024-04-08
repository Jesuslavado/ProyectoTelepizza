package com.example.proyectotelepizza

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityEliminarBinding
import com.google.firebase.firestore.FirebaseFirestore

// Actividad para eliminar productos
class EliminarActivity : ActivityWhitMenus() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEliminarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        // Configurar el Spinner con los IDs y nombres de las pizzas
        cargarPizzas(db, binding)

        // Configurar el botón de eliminación
        binding.beliminar.setOnClickListener {
            // Obtener el ID del producto a eliminar desde el campo de texto
            val productoId = binding.Eid.selectedItem.toString().split(" - ")[0]

            // Eliminar el producto de Firestore
            db.collection("Producto")
                .document(productoId)
                .delete()
                .addOnSuccessListener {
                    showToast("Producto eliminado correctamente")
                    // Limpiar el texto después de eliminar
                    cargarPizzas(db, binding) // Actualizar la lista después de eliminar
                }
                .addOnFailureListener { exception ->
                    showToast("Error al eliminar el producto: $exception")
                }
        }
    }

    // Método para cargar los IDs y nombres de las pizzas en el Spinner
    private fun cargarPizzas(db: FirebaseFirestore, binding: ActivityEliminarBinding) {
        db.collection("Producto")
            .get()
            .addOnSuccessListener { result ->
                val productosList = mutableListOf<String>()

                for (document in result) {
                    val id = document.id
                    val nombre = document.getString("Nombre")
                    if (nombre != null) {
                        val productoInfo = "$id - $nombre"
                        productosList.add(productoInfo)
                    }
                }

                // Crear un adaptador para el Spinner con los IDs y nombres de las pizzas
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    productosList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Configurar el adaptador en el Spinner
                binding.Eid.adapter = adapter
            }
            .addOnFailureListener { exception ->
                showToast("Error al cargar los productos: $exception")
            }
    }

    // Método para mostrar mensajes de notificación
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
