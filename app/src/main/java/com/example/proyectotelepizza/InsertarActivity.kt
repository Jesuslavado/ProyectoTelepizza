package com.example.proyectotelepizza

import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.proyectotelepizza.databinding.ActivityInsertarBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class InsertarActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityInsertarBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    // Variable para almacenar la URI de la imagen seleccionada
    private var selectedImageUri: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.iimagen.setImageURI(uri)
            // Almacena la URI de la imagen seleccionada en la variable global
            selectedImageUri = uri
        } else {
            // No se ha seleccionado ninguna imagen
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

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

            Log.d("InsertarActivity", "Nombre: $nombre, ID: $id, Ingredientes: $ingredientes, Tamaño: $tamano, Precio: $precio")

            if (id.isNotEmpty() && nombre.isNotEmpty() && ingredientes.isNotEmpty() && tamano.isNotEmpty() && precio.isNotEmpty()) {
                // Verifica si se seleccionó una imagen antes de intentar almacenarla
                if (selectedImageUri != null) {
                    // Sube la imagen a Firebase Storage y almacena la URL en Firestore
                    subirImagenAFirebase(selectedImageUri!!, id, nombre, ingredientes, tamano, precio)
                } else {
                    // No se ha seleccionado una imagen, continúa con la inserción de datos
                    almacenarProductoFirestore(id, nombre, ingredientes, tamano, precio, "")
                }
            } else {
                showToast("Error: Algún campo está vacío")
            }
        }

        binding.iimagen.setOnClickListener {
            pickMedia.launch("image/*")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun subirImagenAFirebase(imageUri: Uri, id: String, nombre: String, ingredientes: String,
                                     tamano: String, precio: String) {
        // Generar un nombre aleatorio para la imagen
        val nombreImagen = UUID.randomUUID().toString()

        // Referencia que apunta a un objeto dentro del espacio de almacenamiento de Storage
        // y que se encuentra en una carpeta llamada "imagenes" con el nombre ($nombreImagen)
        val referenciaImagen = storageReference.child("imagenes/$nombreImagen")

        // Subir la imagen a Firebase Storage
        referenciaImagen.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Imagen subida exitosamente
                // Obtenemos la URL de descarga por si queremos almacenarla en Firebase Firestore
                obtenerUrlDescarga(referenciaImagen, id, nombre, ingredientes, tamano, precio)
            }
            .addOnFailureListener { exception ->
                // No se ha podido subir la imagen
                Log.e("FirebaseStorage", "Error al subir la imagen: $exception")
            }
    }

    private fun obtenerUrlDescarga(referenciaImagen: StorageReference, id: String, nombre: String,
                                   ingredientes: String, tamano: String, precio: String) {
        referenciaImagen.downloadUrl
            .addOnSuccessListener { urlDescarga ->
                // URL de descarga obtenida exitosamente
                // Almacena la URL de la imagen y los datos en Firestore
                almacenarProductoFirestore(id, nombre, ingredientes, tamano, precio, urlDescarga.toString())
            }
            .addOnFailureListener { exception ->
                // No se ha podido obtener la URL de descarga de la imagen
                Log.e("FirebaseStorage", "Error al obtener la URL de descarga: $exception")
            }
    }

    private fun almacenarProductoFirestore(id: String, nombre: String, ingredientes: String,
                                           tamano: String, precio: String, urlImagen: String) {
        // Almacena los datos en Firestore
        db.collection("Producto").document(id)
            .set(mapOf(
                "Nombre" to nombre,
                "Ingredientes" to ingredientes,
                "Tamaño" to tamano,
                "Precio" to precio,
                "Imagen" to urlImagen
            ))
            .addOnSuccessListener {
                // La imagen y los datos se almacenaron exitosamente en Firestore
                showToast("Producto almacenado exitosamente en Firestore")
                clearFields()
            }
            .addOnFailureListener { exception ->
                // Manejar errores al almacenar en Firestore
                Log.e("InsertarActivity", "Error al almacenar en Firestore: $exception")
                showToast("Error al almacenar el producto en Firestore. Consulta los registros para más detalles.")
            }
    }

    private fun clearFields() {
        binding.Inombre.text.clear()
        binding.Iid.text.clear()
        binding.Iingredientes.text.clear()
        binding.Iprecio.text.clear()
        binding.iimagen.setImageResource(android.R.color.transparent) // Limpiar la imagen
        selectedImageUri = null // Limpiar la URI de la imagen seleccionada
    }
}
