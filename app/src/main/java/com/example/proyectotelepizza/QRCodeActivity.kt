package com.example.proyectotelepizza

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import com.example.proyectotelepizza.databinding.ActivityQrcodeBinding

class QRCodeActivity : ActivityWhitMenus() {
    private lateinit var binding: ActivityQrcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Habilitar el modo de pantalla completa de borde a borde
        enableEdgeToEdge()

        // Configurar el OnClickListener para el botón de enlace
        binding.benlace.setOnClickListener {
            abrirEnlaceRevolut()
        }
    }

    private fun abrirEnlaceRevolut() {
        val url = "https://revolut.me/jesuspg42"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    // Método para habilitar el modo de pantalla completa de borde a borde
    private fun enableEdgeToEdge() {
        // Código para habilitar el modo de pantalla completa de borde a borde
    }
}
