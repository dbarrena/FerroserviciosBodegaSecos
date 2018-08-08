package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import expertosbd.ferroservicios.ferroserviciosbodegasecos.R
import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.Evento

class EventosAdapter(val context: Context, private val items: MutableList<Evento>,
                     activity: Activity) :
        RecyclerView.Adapter<EventosAdapter.EventoViewHolder>(), Filterable {

    private val listener: EventosAdapter.OnItemClickListener
    private var filteredList: MutableList<Evento>

    init {
        this.listener = activity as EventosAdapter.OnItemClickListener
        filteredList = items
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredList = items
                } else {
                    val filteredEventoList = mutableListOf<Evento>()
                    for (item in items) {
                        // Edit filters here
                        val filterItems = arrayOf(item.placas, item.nombreCliente, item.producto,
                                item.fechaEvento, item.eventoBodegaID.toString())

                        for (filterItem in filterItems) {
                            if (filterItem.toLowerCase().contains(charString.toLowerCase())) {
                                filteredEventoList.add(item)
                            }
                        }
                    }

                    filteredList = filteredEventoList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence,
                                        filterResults: Filter.FilterResults) {
                filteredList = filterResults.values as MutableList<Evento>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }

        holder.moreOptionsBtn.setOnClickListener {
            listener.OnMoreOptionsClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val itemView =
                LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return EventosAdapter.EventoViewHolder(itemView)
    }

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventoID: TextView = itemView.findViewById(R.id.id_evento)
        val placas: TextView = itemView.findViewById(R.id.placas)
        val cliente: TextView = itemView.findViewById(R.id.cliente)
        val producto: TextView = itemView.findViewById(R.id.producto)
        val fechaEvento: TextView = itemView.findViewById(R.id.fechaEvento)
        val moreOptionsBtn: ImageButton = itemView.findViewById(R.id.more_options)
        fun bind(item: Evento) {
            placas.text = item.placas
            cliente.text = item.nombreCliente
            eventoID.text = item.eventoBodegaID.toString()
            producto.text = item.producto
            fechaEvento.text = item.fechaEvento
        }
    }


    interface OnItemClickListener {
        fun onItemClicked(item: Evento)
        fun OnMoreOptionsClicked(item: Evento)
    }

}