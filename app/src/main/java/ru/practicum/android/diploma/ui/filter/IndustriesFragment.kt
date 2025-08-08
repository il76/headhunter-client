package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.data.IndustriesState
import ru.practicum.android.diploma.databinding.FragmentIndustriesBinding
import ru.practicum.android.diploma.domain.models.Industry

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class IndustriesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var industryList: ArrayList<Industry> = arrayListOf()

    private var _binding: FragmentIndustriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<IndustriesViewModel>()
    private val adapter get() = binding.rvIndustryList.adapter as IndustriesAdapter

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val value = s?.toString() ?: ""

                industryList.clear()
                industryList.addAll(viewModel.filterIndustries(value))
                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIndustriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        binding.searchIndustryEditText.removeTextChangedListener(textWatcher)
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvIndustryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIndustryList.adapter = IndustriesAdapter(
            industryList,
            onItemClick = { item ->
                viewModel.industryClicked(item)
                binding.btnApplyIndustries.isVisible = viewModel.hasSelectedIndustries()
            })
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.loadIndustries()

        binding.btnApplyIndustries.setOnClickListener {
            setFragmentResult(
                "industry_selection_result",
                bundleOf("selected_industries" to viewModel.selectedIndustry)
            )

            findNavController().popBackStack()
        }
        binding.searchIndustryEditText.addTextChangedListener(textWatcher)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun render(state: IndustriesState) {
        when (state) {
            is IndustriesState.Content -> showContent(state.industries)
            is IndustriesState.Error -> showError(getString(state.errorMessageId))
            is IndustriesState.Loading -> showLoading()
        }
    }

    private fun showContent(industries: List<Industry>) {
        binding.progressBar.isVisible = false
        industryList.clear()
        industryList.addAll(industries)
        adapter.notifyDataSetChanged()
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.isVisible = false
        industryList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IndustriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
