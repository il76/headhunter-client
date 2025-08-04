package ru.practicum.android.diploma.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        // observeState()
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
                    VacancyFragment.createArguments(vacancy.id.toInt(), false)
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
