package com.example.proyectotelepizza

import android.os.Bundle
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityEliminarBinding
import com.google.firebase.firestore.FirebaseFirestore

class EliminarActivity : ActivityWhitMenus() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEliminarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        binding.beliminar.setOnClickListener {
            val productId = binding.Eid.text.toString()

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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
