package com.example.proyectotelepizza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectotelepizza.databinding.ActivityEliminarBinding
import com.example.proyectotelepizza.databinding.ActivityInsertarBinding
import com.google.firebase.firestore.FirebaseFirestore

class EliminarActivity : ActivityWhitMenus(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEliminarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db= FirebaseFirestore.getInstance()
        binding.beliminar.setOnClickListener {
            db.collection("Producto")
                .document(binding.Eid.text.toString())
                .delete()
        }
    }
}