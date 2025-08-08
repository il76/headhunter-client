package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvIndustryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIndustryList.adapter = IndustriesAdapter(
            industryList,
            onItemClick = {
            })
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.loadIndustries()
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
