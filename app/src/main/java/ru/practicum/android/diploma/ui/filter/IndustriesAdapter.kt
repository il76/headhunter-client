package ru.practicum.android.diploma.ui.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesAdapter(
    private val items: List<Industry>,
    private val onItemClick: (industry: Industry) -> Unit,
    private val onItemLongClick: (industry: Industry) -> Unit = {}
) : RecyclerView.Adapter<IndustriesViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.insutry_item, parent, false)
        return IndustriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClick(items[holder.adapterPosition])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(items[holder.adapterPosition])
            true
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
