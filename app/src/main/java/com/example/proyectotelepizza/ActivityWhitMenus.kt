package com.example.proyectotelepizza

import android.content.Intent
import android.icu.text.IDNA.Info
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


open class ActivityWhitMenus: AppCompatActivity() {
    companion object {
        var actividadActual =0;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Relacionamos la clase con el layout del menú que hemos creado:
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        //Desactivar la opción de la actividad en la que ya estamos:
        for (i in 0 until   menu.size()) {
            if (i== actividadActual) menu.getItem(i).isEnabled =false
            else menu.getItem(i).isEnabled= true
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mostrar ->{
                actividadActual = 0;
                //Hacemos que se habra la pantalla del listado de productos
                val intent = Intent(this, InicioActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.insertar ->{
                actividadActual = 0;
                //Hacemos que se habra la pantalla del listado de productos
                val intent = Intent(this, InsertarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.eliminar ->{
                actividadActual = 0;
                //Hacemos que se habra la pantalla del listado de productos
                val intent = Intent(this, EliminarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.modificar ->{
                actividadActual = 0;
                //Hacemos que se habra la pantalla del listado de productos
                val intent = Intent(this, ModificarActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.salir -> {
                mostrarDialogoConfirmacionSalir()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
    // Nos mostrara un mensaje para confirmar si queremos salir o no
    private fun mostrarDialogoConfirmacionSalir() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Está seguro de salir de la aplicación?")
            .setPositiveButton("Sí") { dialog, id ->
                // Si el usuario selecciona "Sí", cierra la aplicación
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, id ->
                // Si el usuario selecciona "No", simplemente cierra el cuadro de diálogo
                dialog.dismiss()
            }
        // Crea y muestra el cuadro de diálogo
        val dialog = builder.create()
        dialog.show()
    }

}