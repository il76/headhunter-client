package ru.practicum.android.diploma.data.dto

data class VacancyDto(
    val arguments: List<Argument>,
    val clusters: Any?,
    val fixes: Any?,
    val found: Int,
    val items: List<VacancyItem>,
    val page: Int,
    val pages: Int,
    val per_page: Int,
    val suggests: Any?
)

data class Argument(
    val argument: String,
    val cluster_group: ClusterGroup?,
    val disable_url: String,
    val value: String,
    val value_description: String?
)

data class ClusterGroup(
    val id: String,
    val name: String
)

data class VacancyItem(
    val accept_incomplete_resumes: Boolean,
    val alternate_url: String,
    val apply_alternate_url: String,
    val area: Area,
    val employer: Employer,
    val has_test: Boolean,
    val id: String,
    val name: String,
    val published_at: String,
    val relations: List<Any>,
    val response_letter_required: Boolean,
    val response_url: String?,
    val salary: Salary?,
    val show_logo_in_search: Boolean,
    val sort_point_distance: Double?,
    val url: String
)
