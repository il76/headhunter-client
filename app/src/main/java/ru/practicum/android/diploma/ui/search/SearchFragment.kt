package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val onVacancyClickDebounce by lazy {
        debounce<Vacancy>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { vacancy ->
            findNavController().navigate(
                R.id.action_searchFragment_to_vacancyFragment,
                bundleOf("vacancyId" to vacancy.id)
            )
        }
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return // спасибо detekt, который запрещает пустое тело метода
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val value = s?.toString() ?: ""
                // viewModel.searchDebounce(changedText = value)
                binding.searchIconClear.isVisible = value.isNotEmpty()
                binding.searchIconStartSearch.isVisible = value.isEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                return // спасибо detekt, который запрещает пустое тело метода
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        binding.searchEditText.removeTextChangedListener(textWatcher)
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vacancyList.layoutManager = LinearLayoutManager(requireActivity())
        binding.vacancyList.adapter = VacancyListAdapter(viewModel.vacancyList, onVacancyClickDebounce)
        // свайп для обновления
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        // обработка пагинации
        binding.vacancyList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) { // Скролл вниз
                    viewModel.loadNextPage()
                }
            }
        })
        // очистка поискового запроса
        binding.searchIconClear.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager = requireActivity().getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            // viewModel.setSearchText("")
        }

//        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
//
//        }

        // слушаем изменения текстового поля
        binding.searchEditText.addTextChangedListener(textWatcher)

        textWatcher?.let { binding.searchEditText.addTextChangedListener(it) }
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }

}
