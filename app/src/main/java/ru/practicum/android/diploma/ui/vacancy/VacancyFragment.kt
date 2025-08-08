package ru.practicum.android.diploma.ui.vacancy

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Phone
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
        val useDB = arguments?.getBoolean(ARG_USE_LOCAL_DB) ?: false
        initializeObservers()
        viewModel.getVacancy(vacancyId, useDB)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.favButton.setOnClickListener {
            viewModel.favoriteAction()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is VacancyDetailsState.ContentState -> showContent(screenState)
                VacancyDetailsState.ConnectionError -> showErrorVacancyNotFound()
                is VacancyDetailsState.EmptyState -> showErrorVacancyNotFound()
                VacancyDetailsState.LoadingState -> showLoading()
                VacancyDetailsState.ServerError -> showErrorServer()
                is VacancyDetailsState.NetworkErrorState -> showErrorServer()
            }
        }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.favButton.setImageResource(
                if (isFavorite) {
                    R.drawable.ic_favorite_active
                } else {
                    R.drawable.ic_favorite
                }
            )
        }
    }

    private fun showContent(screenState: VacancyDetailsState.ContentState) {
        val vacancyFull = screenState.vacancy
        binding.container.isVisible = true
        binding.progressBar.isVisible = false

        binding.jobTitle.text = vacancyFull.name
        binding.salary.text =
            Converter.formatSalaryString(
                vacancyFull.salaryFrom,
                vacancyFull.salaryTo,
                vacancyFull.salaryCurrency,
                requireContext()
            )
        binding.companyName.text = vacancyFull.employerName
        binding.descriptionText.text = Html.fromHtml(vacancyFull.description, Html.FROM_HTML_MODE_COMPACT)

        showExperience(vacancyFull.experience)
        showLogo(vacancyFull.logoUrl)
        showKeySkills(vacancyFull.keySkills)
        showEmploymentAndSchedule(vacancyFull.employment, vacancyFull.schedule)
        showContacts(vacancyFull.contacts?.email, vacancyFull.contacts?.phones)
        binding.shareButton.setOnClickListener {
            // возможно нужно использовать поле из ответа? Но пока у нас его нет
            viewModel.shareVacancy("https://hh.ru/vacancy/${vacancyFull.id}")
        }
    }

    private fun showErrorVacancyNotFound() {
        hideContent()
        binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_nothing_found)
        binding.includeErrorBlock.searchResultsPlaceholderCaption.setText(R.string.search_no_such_vacancy)
        binding.includeErrorBlock.errorBlock.isVisible = true
    }

    private fun showErrorServer() {
        hideContent()
        binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_server_error_vacancy)
        binding.includeErrorBlock.searchResultsPlaceholderCaption.setText(R.string.search_error)
        binding.includeErrorBlock.errorBlock.isVisible = true
    }

    private fun hideContent() {
        // мне очень не нравится этот код, но без перевёрстывания иначе было быстро не сделать
        binding.container.isVisible = true
        binding.progressBar.isVisible = false
        binding.companyCard.isVisible = false
        binding.experienceTitle.isVisible = false
        binding.experienceText.isVisible = false
        binding.contactsTitle.isVisible = false
        binding.emailText.isVisible = false
        binding.phonesText.isVisible = false
        binding.employmentText.isVisible = false
        binding.descriptionText.isVisible = false
        binding.descriptionTitle.isVisible = false
        binding.emailTitle.isVisible = false
        binding.phonesTitle.isVisible = false
        binding.skillsTitle.isVisible = false
        binding.skillsText.isVisible = false
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.container.isVisible = false
    }

    private fun showKeySkills(keySkills: List<String>?) {
        if (!keySkills.isNullOrEmpty()) {
            val htmlString = buildString {
                append("<ul>")
                keySkills.forEach { skill ->
                    append("<li>$skill</li>")
                }
                append("</ul>")
            }
            binding.skillsText.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
            binding.skillsText.isVisible = true
            binding.skillsTitle.isVisible = true
        } else {
            binding.skillsText.isVisible = false
            binding.skillsTitle.isVisible = false
        }
    }

    private fun showExperience(experience: String?) {
        if (!experience.isNullOrEmpty()) {
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

    private fun showEmploymentAndSchedule(employment: String?, schedule: String?) {
        if (employment.isNullOrEmpty()) {
            if (schedule.isNullOrEmpty()) {
                binding.employmentText.isVisible = false
            } else {
                binding.employmentText.isVisible = true
                binding.employmentText.text = schedule
            }
        } else {
            if (schedule.isNullOrEmpty()) {
                binding.employmentText.isVisible = true
                binding.employmentText.text = employment
            } else {
                binding.employmentText.isVisible = true
                binding.employmentText.text = "$employment, $schedule"
            }
        }
    }

    private fun showContacts(email: String?, phones: List<Phone>?) {
        if (email.isNullOrEmpty()) {
            binding.emailTitle.isVisible = false
            binding.emailText.isVisible = false
        } else {
            binding.emailTitle.isVisible = true
            binding.emailText.isVisible = true
            binding.emailText.text = email
            binding.emailText.setOnClickListener {
                viewModel.openEmailApp(email)
            }
        }
        if (phones.isNullOrEmpty()) {
            binding.phonesTitle.isVisible = false
            binding.phonesText.isVisible = false
        } else {
            binding.phonesTitle.isVisible = true
            binding.phonesText.isVisible = true
            showClickablePhones(phones)
        }
        binding.contactsTitle.isVisible = !(email.isNullOrEmpty() && phones.isNullOrEmpty())
    }

    private fun showClickablePhones(phones: List<Phone>) {
        val phonesText = SpannableStringBuilder()

        phones.forEach { phone ->
            val commentPart = phone.comment?.let { " ($it)" } ?: ""
            val phoneNumber = "+${phone.country} ${phone.city} ${phone.number}"
            val phoneText = "$phoneNumber$commentPart"

            val start = phonesText.length
            phonesText.append(phoneText)
            val end = phonesText.length

            phonesText.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        viewModel.openCallerApp(phoneNumber)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = ds.linkColor
                        ds.isUnderlineText = false
                    }
                },
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (phone != phones.last()) {
                phonesText.append("\n")
            }
        }

        binding.phonesText.text = phonesText
        binding.phonesText.movementMethod = LinkMovementMethod.getInstance()
        binding.phonesText.highlightColor = Color.TRANSPARENT
    }

    companion object {
        private const val VACANCY_ID = "VACANCY_ID"
        private const val ARG_USE_LOCAL_DB = "ARG_USE_LOCAL_DB"

        fun createArguments(vacancyId: Int, fromDB: Boolean = false): Bundle = bundleOf(
            VACANCY_ID to vacancyId,
            ARG_USE_LOCAL_DB to fromDB
        )
    }
}
