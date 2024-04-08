package com.example.proyectotelepizza

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
    private var selectedImageUri: Uri? = null
    private var nextPizzaId: Int = 0

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.iimagen.setImageURI(uri)
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

        // Mapa que asocia los precios a los tamaños de pizza
        val precios = mapOf(
            "Pequeña" to 6.95,
            "Mediana" to 8.95,
            "Familiar" to 12.95
        )

        // Listener para actualizar el precio cuando se selecciona un tamaño
        binding.Itamano.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSize = parent?.getItemAtPosition(position).toString()
                val precio = precios[selectedSize]
                binding.Iprecio.setText(precio?.toString() ?: "")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario hacer nada aquí
            }
        }

        binding.binsertar.setOnClickListener {
            val nombre = binding.Inombre.text.toString()
            val id = nextPizzaId.toString() // Utilizar el ID autoincrementado
            val ingredientes = binding.Iingredientes.text.toString()
            val tamano = binding.Itamano.selectedItem.toString()
            val precio = binding.Iprecio.text.toString()

            Log.d("InsertarActivity", "Nombre: $nombre, ID: $id, Ingredientes: $ingredientes, Tamaño: $tamano, Precio: $precio")

            if (nombre.isNotEmpty() && ingredientes.isNotEmpty() && tamano.isNotEmpty() && precio.isNotEmpty()) {
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

        // Obtener el próximo ID de pizza autoincrementado
        obtenerProximoId()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun obtenerProximoId() {
        // Consultar el número total de pizzas en Firestore
        db.collection("Producto")
            .get()
            .addOnSuccessListener { querySnapshot ->
                nextPizzaId = querySnapshot.size() + 1 // Incrementar el número total y asignarlo al próximo ID
                Log.d("InsertarActivity", "Próximo ID: $nextPizzaId")
            }
            .addOnFailureListener { exception ->
                Log.e("InsertarActivity", "Error al obtener el próximo ID: $exception")
            }
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
                showToast("Producto almacenado")
                clearFields()
            }
            .addOnFailureListener { exception ->
                // Manejar errores al almacenar en Firestore
                Log.e("InsertarActivity", "Error al almacenar en Firestore: $exception")
                showToast("Error al almacenar el producto")
            }
    }

    private fun clearFields() {
        binding.Inombre.text.clear()
        binding.Iingredientes.text.clear()
        binding.Iprecio.text.clear()
        binding.iimagen.setImageResource(android.R.color.transparent) // Limpiar la imagen
        selectedImageUri = null // Limpiar la URI de la imagen seleccionada

        // Incrementar el próximo ID para la siguiente inserción
        nextPizzaId++
    }
}
