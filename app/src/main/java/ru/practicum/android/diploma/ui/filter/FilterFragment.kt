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
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.FILTER_INDUSTRY
import ru.practicum.android.diploma.util.FILTER_SALARY

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilterViewModel by viewModel()

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
        val currentFilter = viewModel.currentFilter.value

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupIndustryView(currentFilter?.industry)
        setupSalaryField(currentFilter?.salary)
        setupSalaryCheckbox(currentFilter?.onlyWithSalary)

        viewModel.updatedFilter.observe(viewLifecycleOwner) { updatedFilter ->
            binding.btnApplyFilter.isVisible = updatedFilter != currentFilter
            binding.btnResetFilter.isVisible = updatedFilter != Filter()
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

        setupListeners()
    }

    private fun handleSelectedIndustries(industry: Industry?) {
        if (industry != null) {
            binding.industryContainer.value.text = industry.name
        } else {
            binding.industryContainer.value.isVisible = false
        }
        viewModel.updateFilter(Filter(industry = industry))
    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshUpdatedFilter()
        val currentFilter = viewModel.currentFilter.value
        val updatedFilter = viewModel.updatedFilter.value
        setupIndustryView(updatedFilter?.industry)
        setupSalaryField(updatedFilter?.salary)
        setupSalaryCheckbox(updatedFilter?.onlyWithSalary)
        viewModel.updatedFilter.observe(viewLifecycleOwner) { updatedFilter ->
            binding.btnApplyFilter.isVisible = updatedFilter != currentFilter
            binding.btnResetFilter.isVisible = updatedFilter != Filter()
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.industryContainer.elementButton.setOnClickListener { navigateToIndustryFragment() }
        binding.industryContainer.filterItem.setOnClickListener { navigateToIndustryFragment() }
        binding.btnResetFilter.setOnClickListener { resetButtonClickListener() }
        setupInputSalaryListeners()
        binding.btnApplyFilter.setOnClickListener { submitFilter() }
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
            if (text != null) {
                val newSalary = text.toString().toIntOrNull()
                val currentSalary = viewModel.currentFilter.value?.salary

                if (newSalary != currentSalary) {
                    viewModel.updateFilter(Filter(salary = newSalary))
                }

                binding.clearSalary.isVisible = isFocused && text.isNotBlank()
            }
        }

        binding.salaryEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            isFocused = hasFocus
            binding.salaryHint.isActivated = hasFocus
            binding.clearSalary.isVisible = hasFocus && binding.salaryEditText.text?.isNotBlank() == true
        }
    }

    private fun submitFilter() {
        findNavController().navigateUp()
    }

    private fun setupIndustryView(industry: Industry?) {
        updateIndustryView(industry) { industry ->
            binding.industryContainer.elementButton.setOnClickListener {
                viewModel.clearFilterField(FILTER_INDUSTRY)
                updateIndustryView(null)
            }
        }
    }

    private fun updateIndustryView(industry: Industry?, onClear: ((Industry?) -> Unit)? = null) {
        val industryText = industry?.name
        with(binding) {
            industryContainer.value.text = industryText
            industryContainer.value.visibility = if (industryText != null) View.VISIBLE else View.GONE
            industryContainer.elementButton.setImageResource(
                if (industryText != null) R.drawable.ic_clear else R.drawable.ic_arrow_forward
            )
            if (industryText == null) {
                industryContainer.elementButton.setOnClickListener { navigateToIndustryFragment() }
            } else {
                onClear?.invoke(industry)
            }
        }
    }

    private fun setupSalaryField(salary: Int?) {
        salary?.let { binding.salaryEditText.setText(it.toString()) }

        binding.salaryEditText.doAfterTextChanged { text ->
            viewModel.updateFilter(Filter(salary = text.toString().toIntOrNull()))
        }

        binding.clearSalary.setOnClickListener {
            binding.salaryEditText.setText("")
            binding.salaryEditText.clearFocus()
            hideKeyboard(binding.root, requireContext())
            viewModel.clearFilterField(FILTER_SALARY)
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

    private fun setupSalaryCheckbox(withSalary: Boolean?) {
        withSalary?.let { binding.salaryCheckbox.isChecked = it }

        binding.salaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateFilter(Filter(onlyWithSalary = true))
            } else {
                viewModel.clearFilterField("withSalary")
            }
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
