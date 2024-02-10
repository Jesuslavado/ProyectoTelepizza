package com.example.proyectotelepizza

import android.app.AlertDialog
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.proyectotelepizza.databinding.ActivityInicioBinding

open class ActivityWhitMenus : AppCompatActivity() {
    companion object {
        var actividadActual = 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        for (i in 1 until menu.size()) {
            if (i == actividadActual) menu.getItem(i).isEnabled = false
            else menu.getItem(i).isEnabled = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mostrar -> {
                actividadActual = 0
                val intent = Intent(this, MostrarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.insertar -> {
                actividadActual = 0
                val intent = Intent(this, InsertarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.eliminar -> {
                actividadActual = 0
                val intent = Intent(this, EliminarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.modificar -> {
                actividadActual = 0
                val intent = Intent(this, ModificarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.cerra_sesion -> {
                mostrarDialogoConfirmacionCerrarSesion()
                true
            }
            R.id.salir -> {
                mostrarDialogoConfirmacionSalir()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun mostrarDialogoConfirmacionCerrarSesion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Está seguro de cerrar sesión?")
            .setPositiveButton("Sí") { dialog, id ->
                cerrarSesion()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun mostrarDialogoConfirmacionSalir() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Está seguro de salir de la aplicación?")
            .setPositiveButton("Sí") { dialog, id ->
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }
}
