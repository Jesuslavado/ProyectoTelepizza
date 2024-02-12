package com.example.proyectotelepizza

import android.os.Bundle
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

        // Configurar el botón de eliminación
        binding.beliminar.setOnClickListener {
            // Obtener el ID del producto a eliminar desde el campo de texto
            val productId = binding.Eid.text.toString()

            // Eliminar el producto de Firestore
            db.collection("Producto")
                .document(productId)
                .delete()
                .addOnSuccessListener {
                    showToast("Producto eliminado correctamente")
                    // Limpiar el texto después de eliminar
                    binding.Eid.text.clear()
                }
                .addOnFailureListener { exception ->
                    showToast("Error al eliminar el producto: $exception")
                }
        }
    }

    // Método para mostrar mensajes de notificación
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
