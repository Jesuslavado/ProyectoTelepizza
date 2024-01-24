package com.example.proyectotelepizza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bregistrar.setOnClickListener{
            // COMPROBAMOS QUE NO ESTA VACIO
            val db= FirebaseFirestore.getInstance()

            if(binding.usuario.text.isNotEmpty() && binding.contrasenia.text.isNotEmpty() && binding.telefono.text.isNotEmpty() && binding.direccion.text.isNotEmpty())
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.direccion.text.toString(), binding.contrasenia.text.toString()
                )
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            db.collection("Users").document(binding.direccion.text.toString())
                                .set(
                                    mapOf(
                                    "usuario" to binding.usuario.text.toString()
                                ))


                            startActivity(Intent(this, InicioActivity::class.java))
                        }
                        else{
                            Toast.makeText(this,"No se ha  podido registrar el usuario", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else {
                Toast.makeText(this, "Algun campo esta vacio", Toast.LENGTH_LONG).show()
            }
        }

    }
}