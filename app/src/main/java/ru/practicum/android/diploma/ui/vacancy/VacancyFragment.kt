package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.util.Converter

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vacancyId = arguments?.getInt(VACANCY_ID, 0) ?: 0
        viewModel.loadVacancy(vacancyId)
        initializeObservers()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is VacancyDetailsState.ContentState -> showContent(screenState)
                VacancyDetailsState.ConnectionError -> TODO()
                VacancyDetailsState.EmptyState -> TODO()
                VacancyDetailsState.LoadingState -> showLoading()
                VacancyDetailsState.ServerError -> TODO()
                is VacancyDetailsState.NetworkErrorState -> TODO()
            }
        }
    }

    private fun showContent(screenState: VacancyDetailsState.ContentState) {
        val vacancyFull = screenState.vacancy
        binding.progressBar.isVisible = false

        binding.jobTitle.text = vacancyFull.name
        binding.salary.text =
            Converter.formatSalaryString(vacancyFull.salaryFrom, vacancyFull.salaryTo, vacancyFull.currency, requireContext())
        binding.companyName.text = vacancyFull.company
        binding.descTitle.text = Html.fromHtml(vacancyFull.description, Html.FROM_HTML_MODE_COMPACT)

        showExperience(vacancyFull.experience)
        showLogo(vacancyFull.icon)
        showKeySkills(vacancyFull.keySkills)
        showEmploymentAndSchedule(vacancyFull.employment, vacancyFull.schedule)
    }

    private fun showErrorVacancyNotFound() {
        TODO()
    }

    private fun showErrorServer() {
        TODO()
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.container.isVisible = false
    }

    private fun showKeySkills(keySkills: String) {
        if (keySkills.isNotEmpty()) {
            binding.skillsText.text = Html.fromHtml(keySkills, Html.FROM_HTML_MODE_COMPACT)
            binding.skillsText.isVisible = true
            binding.skillsTitle.isVisible = true
        } else {
            binding.skillsText.isVisible = false
            binding.skillsTitle.isVisible = false
        }
    }

    private fun showExperience(experience: String) {
        if (experience.isNotEmpty()) {
            binding.experienceTitle.isVisible = true
            binding.experienceText.text = experience
        } else {
            binding.experienceTitle.isVisible = false
        }
    }

    private fun showLogo(image: String) {
        Glide.with(requireActivity())
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_vacancy_placeholder)
            .centerInside()
            .into(binding.companyLogo)

        binding.companyLogo.clipToOutline = true
    }

    private fun showEmploymentAndSchedule(employment: String, schedule: String) {
        if (employment.isEmpty()) {
            if (schedule.isEmpty()) {
                binding.employmentText.isVisible = false
            } else {
                binding.employmentText.isVisible = true
                binding.employmentText.text = schedule
            }
        } else {
            if (schedule.isEmpty()) {
                binding.employmentText.isVisible = true
                binding.employmentText.text = employment
            } else {
                binding.employmentText.isVisible = true
                binding.employmentText.text = "$employment, $schedule"
            }
        }
    }

    companion object {
        private const val VACANCY_ID = "VACANCY_ID"
        fun createArguments(vacancyId: Int): Bundle = bundleOf(VACANCY_ID to vacancyId)
    }
}
