package com.example.proyectotelepizza

import android.content.Intent
import android.net.Uri
import com.google.firebase.storage.StorageReference
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.proyectotelepizza.databinding.ActivityModificarBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ModificarActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityModificarBinding
    private val db = FirebaseFirestore.getInstance()
    private var selectedImageUri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        // Cargar los IDs y nombres de las pizzas en el Spinner
        cargarPizzas()

        // Lista de tamaños
        val tamanos = arrayOf("Pequeña", "Mediana", "Familiar")

        // Adaptador para el Spinner de tamaños
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tamanos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mtamano.adapter = adapter

        binding.mtamano.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Obtener el tamaño seleccionado
                val tamanoSeleccionado = tamanos[position]

                // Obtener el precio correspondiente al tamaño seleccionado
                val precio = obtenerPrecioPorTamano(tamanoSeleccionado)

                // Mostrar el precio en el EditText correspondiente
                binding.mprecio.setText(precio)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se necesita ninguna acción si no se selecciona ningún elemento
            }
        }

        binding.bmodificar.setOnClickListener {
            val producto = binding.midSpinner.selectedItem as Pair<String, String>
            val productId = producto.first

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
                showToast("Por favor, selecciona una pizza válida")
            }
        }

        // Click listener for selecting image
        binding.mimagen.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
        }
    }

    // Método para cargar los IDs y nombres de las pizzas en el Spinner
    private fun cargarPizzas() {
        db.collection("Producto")
            .get()
            .addOnSuccessListener { result ->
                val pizzasList = mutableListOf<Pair<String, String>>()

                for (document in result) {
                    val id = document.id
                    val nombre = document.getString("Nombre")
                    if (nombre != null) {
                        pizzasList.add(Pair(id, nombre))
                    }
                }

                // Crear un adaptador para el Spinner con los IDs y nombres de las pizzas
                val adapter = ArrayAdapter<Pair<String, String>>(
                    this,
                    android.R.layout.simple_spinner_item,
                    pizzasList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Configurar el adaptador en el Spinner
                binding.midSpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                showToast("Error al cargar las pizzas: $exception")
            }
    }

    // Función para obtener el precio basado en el tamaño seleccionado
    private fun obtenerPrecioPorTamano(tamano: String): String {
        // Define el mapa de precios por tamaño
        val preciosPorTamano = mapOf(
            "Pequeña" to "6,95",
            "Mediana" to "8,95",
            "Familiar" to "12,95"
        )
        // Retorna el precio correspondiente al tamaño
        return preciosPorTamano[tamano] ?: ""
    }

    // Método para subir una nueva imagen a Firebase Storage
    // Método para subir una nueva imagen a Firebase Storage
    // Método para subir una nueva imagen a Firebase Storage
    private fun subirNuevaImagenAFirebase(productId: String, nombre: String, ingredientes: String, tamano: String, precio: String) {
        if (selectedImageUri != null) {

            val nombreImagen = UUID.randomUUID().toString()

            // Subir la nueva imagen al almacenamiento de Firebase
            // Reemplaza "tu_ruta_en_el_almacenamiento" con la ruta deseada en tu almacenamiento de Firebase
            val referenciaAlmacenamiento = storageReference.child("Imagenes/$nombreImagen")

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



    // Método para actualizar los datos de un producto en Firestore
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
            binding.midSpinner.setSelection(0) // Establece la selección del Spinner a la primera posición
            binding.mnombre.text = null
            binding.mingredientes.text = null
            binding.mtamano.setSelection(0) // Establece la selección del Spinner a la primera posición
            binding.mprecio.text = null
            binding.mimagen.setImageDrawable(null) // Limpia la imagen del ImageView
        }.addOnFailureListener { exception ->
            showToast("Error al modificar el producto: $exception")
        }
    }

    // Método para mostrar un Toast con un mensaje
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Método para manejar el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            // Carga la nueva imagen en el ImageView (mimagen)
            Glide.with(this)
                .load(selectedImageUri)
                .into(binding.mimagen)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}