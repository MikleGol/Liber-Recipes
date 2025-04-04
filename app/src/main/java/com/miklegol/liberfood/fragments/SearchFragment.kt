package com.miklegol.liberfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.miklegol.liberfood.activities.MainActivity
import com.miklegol.liberfood.activities.MealActivity
import com.miklegol.liberfood.adapters.MealsAdapter

import com.miklegol.liberfood.databinding.FragmentSearchBinding
import com.miklegol.liberfood.models.Meal
import com.miklegol.liberfood.viewmodels.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var searchRecyclerViewAdapter : MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.icSearch.setOnClickListener{
            sarchMeals()
        }

        observeSearchedMealsLiveData()

        var searchJob : Job? = null
        binding.edSearch.addTextChangedListener{ searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch{
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }

        binding.icArrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner, Observer{ mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun sarchMeals() {
        val searchQuery = binding.edSearch.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }


    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()

        searchRecyclerViewAdapter.setOnItemClickListener(object : MealsAdapter.OnItemMealClicked {
            override fun onClickListener(meal: Meal) {
                // Обробка кліку на рецепт, наприклад, відкриття MealsActivity з обраним рецептом
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })

        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }




}