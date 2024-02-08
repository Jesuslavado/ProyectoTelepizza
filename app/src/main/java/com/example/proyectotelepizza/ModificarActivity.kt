package com.example.proyectotelepizza

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityModificarBinding
import com.google.firebase.firestore.FirebaseFirestore

class ModificarActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityModificarBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Lista de tamaños
        val tamanos = arrayOf("Pequeña", "Mediana", "Familiar")

        // Adaptador para el Spinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, tamanos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Configurar el adaptador en el Spinner
        binding.mtamano.adapter = adapter
        binding.bmodificar.setOnClickListener {
            val productId = binding.mid.text.toString()

            binding.bmodificar.setOnClickListener {
                val productId = binding.mid.text.toString()

                if (productId.isNotEmpty()) {
                    db.collection("Producto")
                        .document(productId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val ingredientes = documentSnapshot.getString("Ingredientes") ?: ""
                                val nombre = documentSnapshot.getString("Nombre") ?: ""
                                val precio = documentSnapshot.getString("Precio") ?: ""
                                val tamano = documentSnapshot.getString("Tamaño") ?: ""

                                binding.mingredientes.setText(ingredientes)
                                binding.mnombre.setText(nombre)
                                binding.mprecio.setText(precio)

                                // Seleccionar el valor del Spinner según el valor en Firebase
                                val tamanoIndex = tamanos.indexOf(tamano)
                                if (tamanoIndex != -1) {
                                    binding.mtamano.setSelection(tamanoIndex)
                                }

                                showToast("Producto encontrado y cargado correctamente")
                            } else {
                                showToast("No se encontró el producto con el ID proporcionado")
                            }
                        }
                        .addOnFailureListener { exception ->
                            showToast("Error al obtener el producto: $exception")
                        }
                } else {
                    showToast("Por favor, ingrese un ID de producto válido")
                }
            }
        }

        binding.bmodificar.setOnClickListener {
            val productId = binding.mid.text.toString()

            if (productId.isNotEmpty()) {
                val nombre = binding.mnombre.text.toString()
                val ingredientes = binding.mingredientes.text.toString()
                val tamano = binding.mtamano.selectedItem.toString()
                val precio = binding.mprecio.text.toString()

                db.collection("Producto").document(productId).update(
                    mapOf(
                        "Nombre" to nombre,
                        "Ingredientes" to ingredientes,
                        "Tamaño" to tamano,
                        "Precio" to precio
                    )
                ).addOnSuccessListener {
                    showToast("Producto modificado correctamente")
                }.addOnFailureListener { exception ->
                    showToast("Error al modificar el producto: $exception")
                }
            } else {
                showToast("Por favor, ingrese un ID de producto válido")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
