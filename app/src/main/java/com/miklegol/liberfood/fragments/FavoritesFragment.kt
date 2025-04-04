package com.miklegol.liberfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.miklegol.liberfood.R
import com.miklegol.liberfood.activities.MainActivity
import com.miklegol.liberfood.activities.MealActivity
import com.miklegol.liberfood.adapters.MealsAdapter
import com.miklegol.liberfood.databinding.FragmentFavoritesBinding
import com.miklegol.liberfood.models.Meal
import com.miklegol.liberfood.viewmodels.HomeViewModel
import io.github.muddz.styleabletoast.StyleableToast


class FavoritesFragment : Fragment() {
    private lateinit var binding : FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeFavorites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.differ.currentList.getOrNull(position)

                if (deletedMeal != null) {
                    viewModel.deleteMeal(deletedMeal)

                    val snackbar = Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG)
                    val snackbarView = snackbar.view

                    snackbarView.setBackgroundResource(R.drawable.beige_dark_snack_bar)

                    val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.beige))
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

                    snackbar.setAction("Undo") {
                        viewModel.insertMeal(deletedMeal)
                    }.show()
                } else {
                    StyleableToast.makeText(requireContext(), "Meal not found", R.style.BeigeToast).show()
                }
            }
         }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }



    private fun prepareRecyclerView() {
        favoritesAdapter = MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }

        favoritesAdapter.setOnItemClickListener(object : MealsAdapter.OnItemMealClicked {
            override fun onClickListener(meal: Meal) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer{ meals ->
            favoritesAdapter.differ.submitList(meals)
        })
    }


}