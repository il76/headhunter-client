package ru.practicum.android.diploma.ui.filter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val industryName: TextView = itemView.findViewById(R.id.industry_list_item_title)
    private val industryIcon: ImageView = itemView.findViewById(R.id.industry_item_selection_icon)
    fun bind(industry: Industry) {
        industryName.text = industry.name
        industryIcon.setImageResource(
            if (industry.selected) {
                R.drawable.industry_check_box_on
            } else {
                R.drawable.industry_check_box_off
            }
        )
    }
}
