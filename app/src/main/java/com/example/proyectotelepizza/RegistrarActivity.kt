package com.example.proyectotelepizza

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectotelepizza.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        val radioGroupSexo = findViewById<RadioGroup>(R.id.radioGroupSexo)

        binding.bregistrar.setOnClickListener {
            val selectedSexo = when (radioGroupSexo.checkedRadioButtonId) {
                R.id.rbMujer -> "Mujer"
                R.id.rbhombre -> "Hombre"
                R.id.rbotro -> "Prefiero no responder"
                else -> ""
            }

            if (binding.usuario.text.isNotEmpty() &&
                binding.telefono.text.isNotEmpty() &&
                binding.direccion.text.isNotEmpty() &&
                binding.contrasenia.text.isNotEmpty() &&
                selectedSexo.isNotEmpty()
            ) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.usuario.text.toString(),
                    binding.contrasenia.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val usuario = hashMapOf(
                            "Usuario" to binding.usuario.text.toString(),
                            "Constraseña" to binding.contrasenia.text.toString(),
                            "Sexo" to selectedSexo,
                            "Direccion" to binding.direccion.text.toString()
                        )

                        db.collection("Registro")
                            .document(binding.telefono.text.toString())
                            .set(usuario)
                            .addOnSuccessListener {
                                showToast("Usuario registrado correctamente")
                                startActivity(Intent(this, MostrarActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                showToast("Error al guardar en la base de datos: $exception")
                            }
                    } else {
                        showToast("No se ha podido registrar el usuario: ${task.exception?.message}")
                    }
                }
            } else {
                showToast("Todos los campos deben ser completados")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
