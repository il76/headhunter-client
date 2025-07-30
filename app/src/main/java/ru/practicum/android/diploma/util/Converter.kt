package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import java.text.NumberFormat

object Converter {
    fun formatSalaryString(salaryFrom: Int?, salaryTo: Int?, currency: String, context: Context): String {
        return when {
            salaryFrom != null && salaryTo != null -> context.getString(
                R.string.salary_from_to,
                formatNumber(salaryFrom),
                formatNumber(salaryTo),
                formatCurrency(currency)
            )

            salaryFrom != null && salaryTo == null -> context.getString(
                R.string.salary_from,
                formatNumber(salaryFrom),
                formatCurrency(currency)
            )

            salaryFrom == null && salaryTo != null -> context.getString(
                R.string.salary_to,
                formatNumber(salaryTo),
                formatCurrency(currency)
            )

            else -> context.getString(R.string.empty_salary)
        }
    }

    private fun formatCurrency(currency: String): String {
        return when (currency) {
            "RUR", "RUB" -> " ₽"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "UZS" -> "Soʻm"
            "GEL" -> "₾"
            "KGS" -> "с"
            "BYR" -> "Br"
            "USD" -> "$"
            else -> {
                currency
            }
        }
    }

    private fun formatNumber(number: Int): String {
        return NumberFormat.getInstance().format(number)
    }
}
