package com.example.proyectotelepizza

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.proyectotelepizza.databinding.ActivityModificarBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ModificarActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityModificarBinding
    private val db = FirebaseFirestore.getInstance()
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lista de tamaños
        val tamanos = arrayOf("Pequeña", "Mediana", "Familiar")

        // Adaptador para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tamanos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Configurar el adaptador en el Spinner
        binding.mtamano.adapter = adapter

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
                            val urlImagen = documentSnapshot.getString("Imagen") ?: ""

                            binding.mingredientes.setText(ingredientes)
                            binding.mnombre.setText(nombre)
                            binding.mprecio.setText(precio)

                            // Seleccionar el valor del Spinner según el valor en Firebase
                            val tamanoIndex = tamanos.indexOf(tamano)
                            if (tamanoIndex != -1) {
                                binding.mtamano.setSelection(tamanoIndex)
                            }

                            // Cargar la imagen utilizando Glide o cualquier biblioteca de carga de imágenes
                            // Asegúrate de haber agregado las dependencias necesarias para Glide
                            Glide.with(this)
                                .load(urlImagen)
                                .into(binding.mimagen)

                            showToast("Producto encontrado y cargado correctamente")
                        } else {
                            showToast("No se encontró el producto con el ID proporcionado")
                        }
                    }
                    .addOnFailureListener { exception ->
                        showToast("Error al obtener el producto: $exception")
                    }
            } else {
                showToast("Por favor, ingresa un ID de producto válido")
            }
        }

        // Agregar lógica para seleccionar una nueva imagen
        binding.mimagen.setOnClickListener {
            // Puedes abrir la galería o utilizar un selector de archivos según tus necesidades
            // Aquí estoy utilizando un intent para abrir la galería
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.bmodificar.setOnClickListener {
            val productId = binding.mid.text.toString()

            if (productId.isNotEmpty()) {
                val nombre = binding.mnombre.text.toString()
                val ingredientes = binding.mingredientes.text.toString()
                val tamano = binding.mtamano.selectedItem.toString()
                val precio = binding.mprecio.text.toString()

                // Lógica para subir la nueva imagen si se ha seleccionado una
                if (selectedImageUri != null) {
                    subirNuevaImagenAFirebase(productId, nombre, ingredientes, tamano, precio)
                } else {
                    // Si no se ha seleccionado una nueva imagen, actualizar otros campos solamente
                    actualizarProductoEnFirestore(productId, nombre, ingredientes, tamano, precio, "")
                }
            } else {
                showToast("Por favor, ingresa un ID de producto válido")
            }
        }
    }

    private fun subirNuevaImagenAFirebase(productId: String, nombre: String, ingredientes: String, tamano: String, precio: String) {
        if (selectedImageUri != null) {
            // Sube la nueva imagen al almacenamiento de Firebase
            // Reemplaza "tu_ruta_en_el_almacenamiento" con la ruta deseada en tu almacenamiento de Firebase
            val referenciaAlmacenamiento = FirebaseStorage.getInstance().getReference("modificaciones/${System.currentTimeMillis()}.jpg")

            referenciaAlmacenamiento.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    // Operación de subida exitosa
                    // Obtiene la URL de descarga de la imagen
                    referenciaAlmacenamiento.downloadUrl.addOnSuccessListener { uri ->
                        val urlImagen = uri.toString()

                        // Llama a actualizarProductoEnFirestore con la nueva URL de la imagen
                        actualizarProductoEnFirestore(productId, nombre, ingredientes, tamano, precio, urlImagen)
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("Error al subir la imagen: $exception")
                }
        }
    }

    private fun actualizarProductoEnFirestore(productId: String, nombre: String, ingredientes: String, tamano: String, precio: String, urlImagen: String) {
        // Actualiza los datos en Firestore
        db.collection("Producto").document(productId).update(
            mapOf(
                "Nombre" to nombre,
                "Ingredientes" to ingredientes,
                "Tamaño" to tamano,
                "Precio" to precio,
                "Imagen" to urlImagen
            )
        ).addOnSuccessListener {
            showToast("Producto modificado correctamente")

            // Limpia los campos después de la modificación exitosa
            binding.mid.text = null
            binding.mnombre.text = null
            binding.mingredientes.text = null
            binding.mtamano.setSelection(0) // Establece la selección del Spinner a la primera posición
            binding.mprecio.text = null
            binding.mimagen.setImageDrawable(null) // Limpia la imagen del ImageView
        }.addOnFailureListener { exception ->
            showToast("Error al modificar el producto: $exception")
        }
    }

    // Agrega este método para manejar el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            // Carga la nueva imagen en el ImageView (mfotooferta)
            Glide.with(this)
                .load(selectedImageUri)
                .into(binding.mimagen)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
