import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.databinding.ItemOfertasBinding

// Clase que representa un elemento en la lista de productos
class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // El binding nos permite acceder a los elementos de la interfaz gráfica
    private val binding = ItemOfertasBinding.bind(view)

    // Método para mostrar los datos de un producto en la vista
    fun render(ofertasModel: Producto) {
        // Establece el nombre del producto en el TextView correspondiente
        binding.nombre.text = ofertasModel.Nombre
        // Muestra los ingredientes en el TextView respectivo
        binding.ingredientes.text = ofertasModel.Ingredientes
        // Pone el precio en el TextView adecuado
        binding.precio.text = ofertasModel.Precio
        // Muestra el tamaño en el TextView correspondiente
        binding.tamano.text = ofertasModel.Tamaño

        // Utiliza Glide para cargar la imagen desde la URL en el ImageView
        Glide.with(itemView)
            .load(ofertasModel.Imagen)
            .into(binding.fotooferta)
    }
}
