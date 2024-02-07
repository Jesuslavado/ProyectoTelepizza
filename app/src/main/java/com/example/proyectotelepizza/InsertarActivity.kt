package com.example.proyectotelepizza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityInsertarBinding
import com.google.firebase.firestore.FirebaseFirestore

class InsertarActivity : ActivityWhitMenus() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityInsertarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db= FirebaseFirestore.getInstance()

        binding.binsertar.setOnClickListener{
            if(binding.Iid.text.isNotEmpty() &&
                binding.Inombre.text.isNotEmpty() &&
                binding.Iingredientes.text.isNotEmpty() &&
                binding.Itamano.text.isNotEmpty() &&
                binding.Iprecio.text.isNotEmpty()){

                db.collection("Producto").document(binding.Iid.text.toString()).set(mapOf(
                    "Nombre" to binding.Inombre.text.toString(),
                    "Ingredientes" to binding.Iingredientes.text.toString(),
                    "Tamaño" to binding.Itamano.text.toString(),
                    "Precio" to binding.Iprecio.text.toString()
                )
                )
                Toast.makeText(this,"Se ha insertado la pizza", Toast.LENGTH_LONG).show()

                startActivity(Intent(this, InsertarActivity:: class.java))
            }else{
                Toast.makeText(this, "Error: Algún campo esta vacio", Toast.LENGTH_LONG).show()
            }
        }
    }
}