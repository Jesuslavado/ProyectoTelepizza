package com.example.proyectotelepizza

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectotelepizza.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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
                val contrasenia = binding.contrasenia.text.toString()
                if (contrasenia.length in 8..16) {
                    // Verificar si el usuario ya está registrado por nombre o teléfono
                    db.collection("Registro")
                        .whereEqualTo("Usuario", binding.usuario.text.toString())
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (!task.result?.isEmpty!!) {
                                    showToast("Este nombre de usuario ya está registrado")
                                    return@addOnCompleteListener
                                }

                                // Verificar si el usuario ya está registrado por teléfono
                                db.collection("Registro")
                                    .document(binding.telefono.text.toString())
                                    .get()
                                    .addOnCompleteListener { telTask ->
                                        if (telTask.isSuccessful) {
                                            val document: DocumentSnapshot? = telTask.result
                                            if (document != null && document.exists()) {
                                                showToast("Este teléfono ya está registrado")
                                            } else {
                                                // Crear nuevo usuario
                                                registrarNuevoUsuario(selectedSexo)
                                            }
                                        } else {
                                            showToast("Error al verificar teléfono: ${telTask.exception?.message}")
                                        }
                                    }
                            } else {
                                showToast("Error al verificar usuario: ${task.exception?.message}")
                            }
                        }
                } else {
                    showToast("La contraseña debe tener entre 8 y 16 caracteres")
                }
            } else {
                showToast("Todos los campos deben ser completados")
            }
        }
    }

    private fun registrarNuevoUsuario(selectedSexo: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.usuario.text.toString(),
            binding.contrasenia.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser

                user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        showToast("Se ha enviado un correo electrónico de verificación a ${user.email}")

                        // Cerrar la sesión del usuario actual
                        FirebaseAuth.getInstance().signOut()

                        // Redirigir al usuario a la pantalla principal (MainActivity)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        showToast("Error al enviar el correo electrónico de verificación: ${emailTask.exception?.message}")
                    }
                }

                val usuario = hashMapOf(
                    "Usuario" to binding.usuario.text.toString(),
                    "Contraseña" to binding.contrasenia.text.toString(),
                    "Sexo" to selectedSexo,
                    "Direccion" to binding.direccion.text.toString()
                )

                FirebaseFirestore.getInstance().collection("Registro")
                    .document(binding.telefono.text.toString())
                    .set(usuario)
                    .addOnSuccessListener {
                        // showToast("Usuario registrado correctamente")
                        // No mostramos el Toast aquí para evitar confusión, ya que el usuario debe verificar su correo electrónico antes de acceder
                    }
                    .addOnFailureListener { exception ->
                        showToast("Error al guardar en la base de datos: $exception")
                    }
            } else {
                showToast("No se ha podido registrar el usuario: ${task.exception?.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}