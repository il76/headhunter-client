package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
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
                VacancyFragment.createArguments(vacancy.id.toInt())
            )

        }
    }
    private val adapter get() = binding.vacancyList.adapter as VacancyListAdapter

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return // спасибо detekt, который запрещает пустое тело метода
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val value = s?.toString() ?: ""
                viewModel.updateSearchQuery(value)
                binding.searchIconClear.isVisible = value.isNotEmpty()
                binding.searchIconStartSearch.isVisible = value.isEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                return // см. выше
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
        setupRecyclerView()
        setupSwipeRefresh()
        setupSearchView()
        observeState()

        binding.filterIcon.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment,
            )
        }
    }

    // список вакансий
    private fun setupRecyclerView() {
        binding.vacancyList.layoutManager = LinearLayoutManager(requireActivity())
        // binding.vacancyList.adapter = VacancyListAdapter(onVacancyClickDebounce)
        // временно отключил дебаунсер до устранения проблем с неработающим кликом
        binding.vacancyList.adapter = VacancyListAdapter(
            { vacancy ->
                findNavController().navigate(
                    R.id.action_searchFragment_to_vacancyFragment,
                    VacancyFragment.createArguments(vacancy.id.toInt())
                )
            }
        )

        adapter.submitList(emptyList<Vacancy>())

        // обработка пагинации
        binding.vacancyList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) { // Скролл вниз
                    viewModel.loadNextPage()
                }
            }
        })
    }

    // свайп для обновления
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupSearchView() {
        viewModel.renderFilterState()
        // очистка поискового запроса
        binding.searchIconClear.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager = requireActivity().getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            viewModel.updateSearchQuery("")
        }

//        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
//  если не потребуется - убрать
//        }

        // слушаем изменения текстового поля
        binding.searchEditText.addTextChangedListener(textWatcher)
    }

    // разбор состояния
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    handleMainState(state)
                    handlePaginationState(state)
                    handleRefreshState(state)
                }
            }
        }

        viewModel.filterState().observe(viewLifecycleOwner) { isChecked ->
            renderFilterState(isChecked)
        }
    }

    private fun handleMainState(state: SearchUIState) {
        when (state.status) {
            SearchUIState.SearchStatus.LOADING -> {
                if (state.vacancyList.isEmpty()) {
                    showFullscreenLoading()
                }
            }

            SearchUIState.SearchStatus.SUCCESS -> {
                hideFullscreenLoading()
                adapter.submitList(state.vacancyList)
                binding.searchTotalFound.text = getString(R.string.search_total_found, state.totalFound)
            }

            SearchUIState.SearchStatus.ERROR_NET -> {
                hideFullscreenLoading()
                showErrorView()
                binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_no_internet)
                binding.includeErrorBlock.searchResultsPlaceholderCaption.setText(R.string.search_no_internet)
            }

            SearchUIState.SearchStatus.EMPTY_RESULT -> {
                hideFullscreenLoading()
                showEmptyView()
                binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_nothing_found)
                binding.includeErrorBlock.searchResultsPlaceholderCaption.setText(R.string.search_list_fetch_fail)
            }

            SearchUIState.SearchStatus.NONE -> {
                showEmptyView()
                binding.progressBar.isVisible = false
            }
        }
    }

    // загрузка начата
    private fun showFullscreenLoading() {
        binding.progressBar.isVisible = true
        binding.vacancyList.isVisible = false
        binding.includeErrorBlock.errorBlock.isVisible = false
        binding.searchTotalFound.isVisible = false
    }

    // загрузка завершена
    private fun hideFullscreenLoading() {
        binding.progressBar.isVisible = false
        binding.vacancyList.isVisible = true
        binding.searchTotalFound.isVisible = true
        // binding.errorBlock.isVisible = false
    }

    // догрузка начата
    private fun showBottomLoading() {
        binding.progressBarBottom.isVisible = true
    }

    // догрузка завершена
    private fun hideBottomLoading() {
        binding.progressBarBottom.isVisible = false
    }

    // ничего не нашлось
    private fun showEmptyView() {
        binding.vacancyList.isVisible = false
        binding.searchTotalFound.isVisible = false
        binding.includeErrorBlock.errorBlock.isVisible = true
        binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_start_search)
        binding.includeErrorBlock.searchResultsPlaceholderCaption.text = ""
    }

    // ошибка
    private fun showErrorView() {
        binding.vacancyList.isVisible = false
        binding.searchTotalFound.isVisible = false
        binding.includeErrorBlock.errorBlock.isVisible = true
    }

    private fun handlePaginationState(state: SearchUIState) {
        when (state.pagination) {
            SearchUIState.PaginationState.LOADING -> showBottomLoading()
            is SearchUIState.PaginationState.ERROR -> showPaginationError(state.pagination.error)
            SearchUIState.PaginationState.IDLE -> hideBottomLoading()
        }
    }

    private fun showPaginationError(error: Throwable) {
        Toast.makeText(requireContext(), "Ошибка загрузки: ${error.message}", Toast.LENGTH_SHORT).show()
    }

    private fun handleRefreshState(state: SearchUIState) {
        binding.swipeRefreshLayout.isRefreshing = state.isRefreshing
    }

    private fun renderFilterState(isChecked: Boolean) {
        when (isChecked) {
            true -> binding.filterIcon.setImageResource(R.drawable.ic_filters_on)
            false -> binding.filterIcon.setImageResource(R.drawable.ic_filters_off)
        }
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }

}
