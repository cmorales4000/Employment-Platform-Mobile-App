package co.work.papp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.work.papp.data.OfertaDato
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

var url = "http://192.168.1.2:8000"

class RecyclerAdapter(val list_oferta:List<OfertaDato>, var clickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titulo: TextView = itemView.findViewById(R.id.titulo)
        val salario: TextView = itemView.findViewById(R.id.salario)
        val ciudad: TextView = itemView.findViewById(R.id.ciudad)
        val logo: ImageView = itemView.findViewById(R.id.logo)




        fun initialize(item: OfertaDato, action: OnItemClickListener) {


            titulo.text = item.titulo
            salario.text = item.salario.toString()
            ciudad.text = item.ciudad.toString()
            val logodeempresa = url.plus(item.logoempresa)

            Log.d("logo", logodeempresa)



            Glide.with(itemView.context).load(logodeempresa).into(logo)
            //Picasso.with(itemView.context).load(item.logoempresa.toString()).into(logo);


            itemView.setOnClickListener {
                action.OnItemClick(item, adapterPosition)
            }
        }

        init {
            itemView.setOnClickListener { v:View ->
                val position: Int = adapterPosition
                //Toast.makeText(itemView.context,"Has seleccionado la posición ${position + 1}", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.oferta_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      //  holder.titulo.text = list_oferta.get(position).titulo
       // holder.salario.text = list_oferta.get(position).salary
       // holder.ciudad.text = list_oferta.get(position).city
       //holder.logo.setImageResource(list_oferta.get(position).image)

        holder.initialize(list_oferta.get(position), clickListener)
    }

    override fun getItemCount(): Int {
        return list_oferta.size
    }
}

interface OnItemClickListener{
    fun OnItemClick(item: OfertaDato, position: Int)

}