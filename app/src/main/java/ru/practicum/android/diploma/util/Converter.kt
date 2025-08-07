package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R

object Converter {
    fun formatSalaryString(salaryFrom: Int?, salaryTo: Int?, currency: String, context: Context): String {
        return when {
            salaryFrom != null && salaryTo != null && salaryFrom > 0 && salaryTo > 0 -> {
                context.getString(
                    R.string.salary_from_to,
                    salaryFrom,
                    formatCurrency(currency),
                    salaryTo,
                    formatCurrency(currency)
                )
            }

            salaryFrom != null && salaryFrom > 0 -> {
                context.getString(
                    R.string.salary_from,
                    salaryFrom,
                    formatCurrency(currency)
                )
            }

            salaryTo != null && salaryTo > 0 -> {
                context.getString(
                    R.string.salary_to,
                    salaryTo,
                    formatCurrency(currency)
                )
            }

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

//    private fun formatNumber(number: Int): String {
//        return NumberFormat.getInstance().format(number)
//    }
}
