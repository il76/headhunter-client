package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancySearchItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyListAdapter(
    private val vacancyList: List<Vacancy>,
    private val clickListener: (Vacancy) -> Unit,
) : RecyclerView.Adapter<VacancyListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return VacancyListViewHolder(VacancySearchItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    override fun onBindViewHolder(holder: VacancyListViewHolder, position: Int) {
        val vacancy = vacancyList[holder.adapterPosition]
        holder.bind(vacancy)
        holder.itemView.setOnClickListener {
            clickListener(vacancy)
        }
    }

}
