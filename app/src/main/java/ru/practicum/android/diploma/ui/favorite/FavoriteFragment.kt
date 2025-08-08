package ru.practicum.android.diploma.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.VacancyListAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModel()
    private val adapter get() = binding.vacancyList.adapter as VacancyListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.search()
                viewModel.state.collect { state ->
                    handleMainState(state)
                }
            }
        }
    }

    private fun handleMainState(state: FavoriteUIState) {
        when (state.status) {
            FavoriteUIState.FavoriteStatus.LOADING -> {
                if (state.vacancyList.isEmpty()) {
                    showFullscreenLoading()
                }
            }
            FavoriteUIState.FavoriteStatus.SUCCESS -> {
                hideFullscreenLoading()
                adapter.submitList(state.vacancyList)
            }
            FavoriteUIState.FavoriteStatus.ERROR -> {
                hideFullscreenLoading()
                showErrorView()
                binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_error_get_list)
                binding.includeErrorBlock.searchResultsPlaceholderCaption.setText(R.string.search_error)
            }
            FavoriteUIState.FavoriteStatus.EMPTY_RESULT -> {
                hideFullscreenLoading()
                showEmptyView()
                binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_nothing_found)
                binding.includeErrorBlock.searchResultsPlaceholderCaption.setText(R.string.search_list_fetch_fail)
            }
            FavoriteUIState.FavoriteStatus.NONE -> {
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
    }

    // загрузка завершена
    private fun hideFullscreenLoading() {
        binding.progressBar.isVisible = false
        binding.vacancyList.isVisible = true
        // binding.errorBlock.isVisible = false
    }

    // ничего не нашлось
    private fun showEmptyView() {
        binding.vacancyList.isVisible = false
        binding.includeErrorBlock.errorBlock.isVisible = true
        binding.includeErrorBlock.searchResultsPlaceholder.setImageResource(R.drawable.ph_start_search)
        binding.includeErrorBlock.searchResultsPlaceholderCaption.text = ""
    }

    // ошибка
    private fun showErrorView() {
        binding.vacancyList.isVisible = false
        binding.includeErrorBlock.errorBlock.isVisible = true
    }

    // список вакансий
    private fun setupRecyclerView() {
        binding.vacancyList.layoutManager = LinearLayoutManager(requireActivity())
        // binding.vacancyList.adapter = VacancyListAdapter(onVacancyClickDebounce)
        // временно отключил дебаунсер до устранения проблем с неработающим кликом
        binding.vacancyList.adapter = VacancyListAdapter(
            { vacancy ->
                findNavController().navigate(
                    R.id.action_favoriteFragment_to_vacancyFragment,
                    VacancyFragment.createArguments(vacancy.id.toInt(), true)
                )
            }
        )
        adapter.submitList(emptyList<Vacancy>())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
