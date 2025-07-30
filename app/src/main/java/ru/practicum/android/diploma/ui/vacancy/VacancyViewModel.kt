package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.domain.models.VacancyFull

class VacancyViewModel : ViewModel() {
    private val _screenState = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.LoadingState)
    val screenState: LiveData<VacancyDetailsState> = _screenState
    private var id = -1

    fun loadVacancy(vacancyId: Int): VacancyFull {
        id = vacancyId
        val vacancy = mockVacancy
        _screenState.value = VacancyDetailsState.ContentState(vacancy)
        return vacancy
    }

    val mockVacancy = VacancyFull(
        id = 0,
        name = "Android-разработчик",
        company = "Еда",
        currency = "₽",
        salaryFrom = 100_000,
        salaryTo = null,
        area = "Москва",
        alternateUrl = "https://hh.ru/vacancy/8331228",
        icon = "",
        employment = "Полная занятость, Удаленная работа",
        experience = "От 1 года до 3 лет",
        schedule = "",
        description = "<h3>Обязанности</h3><ul><li>Разрабатывать новую функциональность приложения</li></ul>",
        contact = "",
        email = "",
        phone = "",
        comment = "",
        keySkills = "Знание классических алгоритмов и структуры данных",
        address = ""
    )
}
