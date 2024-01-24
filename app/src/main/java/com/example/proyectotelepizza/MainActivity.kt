package com.example.proyectotelepizza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectotelepizza.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    public lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView((binding.root))

        // Hacemos la función de nuestro boton iniciar sesión
        binding.biniciarsesion.setOnClickListener{
            login()
        }
        binding.bregistrar.setOnClickListener {
            registro()
        }

    }
    private fun registro() {
        startActivity(Intent(this, RegistrarActivity::class.java))
    }

    private fun login(){
        // El correo y la contraseña no son campos vacios
        if(binding.usuario.text.isNotEmpty() && binding.contrasenia.text.isNotEmpty()){
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.usuario.text.toString(),
                binding.contrasenia.text.toString()
            )
                .addOnCompleteListener {
                    // Si tenemos exito
                    if(it.isSuccessful)
                    {
                        val intent=Intent(this, InicioActivity::class.java)
                        startActivity(intent)
                    }else{
                        // Mensaje que aparecera si hubo  problemas
                        Toast.makeText(this,"Correo o Contraseña incorrecto/a", Toast.LENGTH_SHORT).show()
                    }

                }
        }else{Toast.makeText(this, "Algún campo esta vacío", Toast.LENGTH_SHORT).show()}
    }

}