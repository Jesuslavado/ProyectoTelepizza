import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotelepizza.CarroComprasActivity
import com.example.proyectotelepizza.Producto
import com.example.proyectotelepizza.R
import com.google.gson.Gson


class AdaptadorProducto(
    var context: Context,
    var tvCantProductos: TextView,
    var btnVerCarro: Button,
    var listaProductos: ArrayList<Producto>,
    var carroCompras: ArrayList<Producto>
): RecyclerView.Adapter<AdaptadorProducto.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNomProducto = itemView.findViewById(R.id.tvNomProducto) as TextView
        val cbCarro = itemView.findViewById(R.id.bcomprar) as Button
        val tvPrecio = itemView.findViewById(R.id.tvPrecio) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var vista = LayoutInflater.from(parent.context).inflate(R.layout.item_ofertas, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = listaProductos[position]

        tvCantProductos.text = carroCompras.size.toString()

        holder.tvNomProducto.text = producto.Nombre
        holder.tvPrecio.text = producto.Precio



        btnVerCarro.setOnClickListener {
            val intent = Intent(context, CarroComprasActivity::class.java)
            intent.putExtra("carro_compras", carroCompras)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    fun guardarSharedPreferences() {
        val sp: SharedPreferences = context.getSharedPreferences("carro_compras", MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear().apply()

        val jsonString = Gson().toJson(carroCompras)

        editor.putString("productos", jsonString)

        editor.apply()
    }


}