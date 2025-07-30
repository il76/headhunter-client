package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.VacancySearchItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyListAdapter(
    private val clickListener: (Vacancy) -> Unit,
) : ListAdapter<Vacancy, VacancyListViewHolder>(VacancyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return VacancyListViewHolder(VacancySearchItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: VacancyListViewHolder, position: Int) {
        val vacancy = getItem(position)
        holder.bind(vacancy)
        holder.itemView.setOnClickListener {
            clickListener(vacancy)
        }
    }

}

// DiffUtil для сравнения элементов
class VacancyDiffCallback : DiffUtil.ItemCallback<Vacancy>() {
    override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy) = oldItem == newItem
}
