package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel : ViewModel() {
    var vacancyList: List<Vacancy> = mutableListOf()

    init {
        vacancyList = listOf(
            Vacancy(
                id = "122105390",
                name = "Курьер-видеооператор",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Москва",
                employerName = "Работодатель 1",
                salaryCurrency = "RUR",
                salaryFrom = 999,
                salaryTo = 1084,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "122774772",
                name = "Продавец бытовой техники",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Петербург",
                employerName = "Работодатель 2",
                salaryCurrency = "RUR",
                salaryFrom = null,
                salaryTo = null,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "122697402",
                name = "Продавец-консультант (Pядом с домом)",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Сочи",
                employerName = "Работодатель 3",
                salaryCurrency = "RUR",
                salaryFrom = 999,
                salaryTo = 0,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf()
            ),
            Vacancy(
                id = "123",
                name = "Название вакансии 4",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Псков",
                employerName = "Работодатель 4",
                salaryCurrency = "RUR",
                salaryFrom = 0,
                salaryTo = 1084,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "123",
                name = "Название вакансии 5",
                logoUrl = "",
                areaName = "Новгород",
                employerName = "Работодатель 5",
                salaryCurrency = "RUR",
                salaryFrom = 0,
                salaryTo = 0,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "122105390",
                name = "Курьер-видеооператор",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Москва",
                employerName = "Работодатель 1",
                salaryCurrency = "RUR",
                salaryFrom = 999,
                salaryTo = 1084,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "122774772",
                name = "Продавец бытовой техники",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Петербург",
                employerName = "Работодатель 2",
                salaryCurrency = "RUR",
                salaryFrom = null,
                salaryTo = null,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "122697402",
                name = "Продавец-консультант (Pядом с домом)",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Сочи",
                employerName = "Работодатель 3",
                salaryCurrency = "RUR",
                salaryFrom = 999,
                salaryTo = 0,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf()
            ),
            Vacancy(
                id = "123",
                name = "Название вакансии 4",
                logoUrl = "https://hh.ru/employer-logo/7177004.png",
                areaName = "Псков",
                employerName = "Работодатель 4",
                salaryCurrency = "RUR",
                salaryFrom = 0,
                salaryTo = 1084,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
            Vacancy(
                id = "123",
                name = "Название вакансии 5",
                logoUrl = "",
                areaName = "Новгород",
                employerName = "Работодатель 5",
                salaryCurrency = "RUR",
                salaryFrom = 0,
                salaryTo = 0,
                experience = "да",
                employment = "полная",
                description = "Требуются рабочие для работы на работе. Оплата деньгами",
                keySkills = listOf("Умение работать", "Умение хорошо работать")
            ),
        )
    }
}
