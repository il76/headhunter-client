package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancySearchItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Converter

class VacancyListViewHolder(private val binding: VacancySearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(vacancy: Vacancy) {
        val context = binding.root.context

        Glide.with(context)
            .load(vacancy.logoUrl)
            .placeholder(R.drawable.ic_vacancy_placeholder)
            .transform(RoundedCorners(binding.root.context.resources.getDimensionPixelSize(R.dimen.logo_border_radius)))
            .transform(
                RoundedCorners(
                    context.resources.getDimensionPixelSize(R.dimen.logo_border_radius)
                )
            )
            .into(binding.vacancyListLogo)

        binding.vacancyListTitle.text = vacancy.name
        binding.vacancyListCompany.text = vacancy.employerName
        val salaryText =
            Converter.formatSalaryString(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency, context)
        binding.vacancyListSalary.text = salaryText
    }
}
