import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.databinding.ItemOfertasBinding

class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemOfertasBinding.bind(view)

    fun render(ofertasModel: Producto) {
        binding.nombre.text = ofertasModel.Nombre
        binding.ingredientes.text = ofertasModel.Ingredientes
        binding.precio.text = ofertasModel.Precio
        binding.tamano.text = ofertasModel.Tamaño

        // Utiliza Glide para cargar la imagen desde la URL en el ImageView
        Glide.with(itemView)
            .load(ofertasModel.Imagen)
            .into(binding.fotooferta)
    }
}
