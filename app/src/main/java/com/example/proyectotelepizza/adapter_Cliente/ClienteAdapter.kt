import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.adapter_Cliente.ClienteViewHolder
import com.example.proyectotelepizza.databinding.ItemCarroCompraBinding

class ClienteAdapter(private var productosEnCarrito: MutableList<Producto>, private val onItemRemoved: () -> Unit) : RecyclerView.Adapter<ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCarroCompraBinding.inflate(layoutInflater, parent, false)
        return ClienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val item = productosEnCarrito[position]
        holder.render(item)

        // Configurar el OnClickListener para el botón de eliminar producto
        holder.binding.beliminarp.setOnClickListener {
            eliminarProducto(position)
            onItemRemoved.invoke()
        }
    }

    override fun getItemCount(): Int {
        return productosEnCarrito.size
    }

    // Método para actualizar la lista de productos en el adaptador
    fun actualizarProductos(productos: MutableList<Producto>) {
        productosEnCarrito.clear()
        productosEnCarrito.addAll(productos)
        notifyDataSetChanged()
    }

    // Método para eliminar un producto del carrito
    private fun eliminarProducto(position: Int) {
        productosEnCarrito.removeAt(position)
        notifyItemRemoved(position)
    }
}
