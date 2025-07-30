package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancySearchItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyListViewHolder(private val binding: VacancySearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(vacancy: Vacancy) {
        Glide.with(binding.root)
            .load(vacancy.logoUrl)
            .placeholder(R.drawable.ic_vacancy_placeholder)
            .transform(RoundedCorners(binding.root.context.resources.getDimensionPixelSize(R.dimen.logo_border_radius)))
            .into(binding.vacancyListLogo)

        binding.vacancyListTitle.text = vacancy.name
        binding.vacancyListCompany.text = vacancy.employerName
        var salaryText = ""
        if (vacancy.salaryFrom != null && vacancy.salaryFrom > 0) {
            salaryText = "от ${vacancy.salaryFrom} ${vacancy.salaryCurrency}"
        }
        if (vacancy.salaryTo != null && vacancy.salaryTo > 0) {
            salaryText += " до ${vacancy.salaryTo} ${vacancy.salaryCurrency}"
        }
        salaryText = salaryText.trim()
        if (salaryText.isBlank()) {
            salaryText = binding.root.context.getString(R.string.empty_salary)
        }
        binding.vacancyListSalary.text = salaryText
    }
}
