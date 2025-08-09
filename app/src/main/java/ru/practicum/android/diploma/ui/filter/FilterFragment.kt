package ru.practicum.android.diploma.ui.filter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.search.SearchFragment

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilterViewModel by viewModel()

    private var ignoreTextChanges = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentFilter.observe(viewLifecycleOwner) { currentFilter ->
            val areButtonsVisible =
                currentFilter.salary != null || currentFilter.onlyWithSalary != null || currentFilter.industry != null
            binding.btnApplyFilter.isVisible = areButtonsVisible
            binding.btnResetFilter.isVisible = areButtonsVisible
            updateIndustryView(currentFilter.industry)
            currentFilter.onlyWithSalary?.let { binding.salaryCheckbox.isChecked = it }
            ignoreTextChanges = true
            if (currentFilter.salary.toString() != binding.salaryEditText.text.toString()) {
                currentFilter.salary?.let { binding.salaryEditText.setText(it.toString()) }
            }
            ignoreTextChanges = false
        }
        setupSalaryField()
        setupListeners()
    }

    private fun handleSelectedIndustries(industry: Industry?) {
        if (industry != null) {
            binding.industryContainer.value.text = industry.name
        } else {
            binding.industryContainer.value.isVisible = false
        }
        viewModel.updateFilter(industry = industry)
    }

    private fun setupListeners() {
        binding.industryContainer.elementButton.setOnClickListener { navigateToIndustryFragment() }
        binding.industryContainer.filterItem.setOnClickListener { navigateToIndustryFragment() }
        binding.btnResetFilter.setOnClickListener { resetButtonClickListener() }
        setupInputSalaryListeners()
        binding.btnApplyFilter.setOnClickListener { submitFilter() }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        parentFragmentManager.setFragmentResultListener(
            PARAM_INDUSTRIES_SELECTION,
            viewLifecycleOwner
        ) { _, result ->
            @Suppress("DEPRECATION")
            val selectedIndustry = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.getParcelable(PARAM_INDUSTRIES, Industry::class.java)
            } else {
                result.getParcelable(PARAM_INDUSTRIES) as? Industry
            }
            handleSelectedIndustries(selectedIndustry)
        }
        binding.salaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateFilter(onlyWithSalary = true)
            } else {
                viewModel.updateFilter(onlyWithSalary = null)
            }
        }
    }

    private fun navigateToIndustryFragment() {
        findNavController().navigate(
            R.id.action_filterFragment_to_industriesFragment,
            bundleOf(PARAM_INDUSTRIES to viewModel.currentFilter.value?.industry)
        )
    }

    private fun resetButtonClickListener() {
        viewModel.clearFilter()
        binding.salaryEditText.text = null
        binding.salaryCheckbox.isChecked = false
        viewModel.refreshUpdatedFilter()
        binding.btnApplyFilter.isVisible = false
        handleSelectedIndustries(null)
    }

    private fun setupInputSalaryListeners() {
        var isFocused = false

        binding.salaryEditText.doAfterTextChanged { text ->
            val newSalary = text.toString().toIntOrNull()
            val currentSalary = viewModel.currentFilter.value?.salary

            if (newSalary != currentSalary) {
                viewModel.updateFilter(salary = newSalary)
            }

            binding.clearSalary.isVisible = isFocused && !text.isNullOrEmpty()
        }

        binding.salaryEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            isFocused = hasFocus
            binding.salaryHint.isActivated = hasFocus
            binding.clearSalary.isVisible = hasFocus && binding.salaryEditText.text?.isNotBlank() == true
        }
    }

    private fun submitFilter() {
        setFragmentResult(SearchFragment.REQUEST_KEY, bundleOf(SearchFragment.KEY_SHOULD_REFRESH to true))
        findNavController().navigateUp()
    }

    private fun updateIndustryView(industry: Industry?) {
        val industryText = industry?.name
        with(binding) {
            industryContainer.value.text = industryText
            industryContainer.value.isVisible = industryText != null
            industryContainer.elementButton.isVisible = industryText == null
            industryContainer.clearElementButton.isVisible = industryText != null
            industryContainer.elementButton.setOnClickListener { navigateToIndustryFragment() }
            industryContainer.clearElementButton.setOnClickListener {
                viewModel.updateFilter(industry = null)
            }
        }
    }

    private fun setupSalaryField() {
        binding.clearSalary.setOnClickListener {
            binding.salaryEditText.setText("")
            binding.salaryEditText.clearFocus()
            hideKeyboard(binding.root, requireContext())
            viewModel.updateFilter(salary = null)
        }

        binding.salaryEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root, requireContext())
                binding.salaryEditText.clearFocus()
                true
            }
            false
        }
    }

    private fun hideKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val PARAM_INDUSTRIES = "selected_industries"
        const val PARAM_INDUSTRIES_SELECTION = "industry_selection_result"
    }
}
