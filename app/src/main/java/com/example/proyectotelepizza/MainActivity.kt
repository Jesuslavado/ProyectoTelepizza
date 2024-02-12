package com.example.proyectotelepizza

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectotelepizza.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos la función del botón iniciar sesión
        binding.biniciarsesion.setOnClickListener {
            iniciarSesion()
        }

        // Configuramos la función del botón registrar
        binding.bregistrar.setOnClickListener {
            abrirRegistro()
        }

        // Configuramos la función para salir de la aplicación al hacer clic en la ImageView
        binding.salirApp.setOnClickListener {
            mostrarDialogoConfirmacionSalir()
        }
    }

    private fun abrirRegistro() {
        startActivity(Intent(this, RegistrarActivity::class.java))
    }

    private fun iniciarSesion() {
        // Verificamos que el correo y la contraseña no estén vacíos
        if (binding.usuario.text.isNotEmpty() && binding.contrasenia.text.isNotEmpty()) {
            // Iniciamos sesión con el método signInWithEmailAndPassword de Firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.usuario.text.toString(),
                binding.contrasenia.text.toString()
            )
                .addOnCompleteListener { task ->
                    // Si la operación es exitosa
                    if (task.isSuccessful) {
                        val intent = Intent(this, MostrarActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Mensaje que se mostrará si hay problemas durante el inicio de sesión
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Mensaje que se mostrará si algún campo está vacío
            Toast.makeText(this, "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoConfirmacionSalir() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir")
        builder.setMessage("¿Estás seguro de que quieres salir?")
        builder.setPositiveButton("Sí") { dialog, which ->
            // Cierra todas las actividades y finaliza la aplicación
            finishAffinity()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // No hacer nada, simplemente cerrar el cuadro de diálogo
        }
        builder.show()
    }
}
