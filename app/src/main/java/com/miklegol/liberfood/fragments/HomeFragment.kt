package com.miklegol.liberfood.fragments



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


import com.bumptech.glide.Glide
import com.miklegol.liberfood.R
import com.miklegol.liberfood.activities.CategoryMealsActivity
import com.miklegol.liberfood.activities.MainActivity
import com.miklegol.liberfood.activities.MealActivity
import com.miklegol.liberfood.adapters.CategoriesAdapter
import com.miklegol.liberfood.adapters.MostPopularAdapter
import com.miklegol.liberfood.databinding.FragmentHomeBinding
import com.miklegol.liberfood.fragments.MealBottomSheetFragment
import com.miklegol.liberfood.models.Category
import com.miklegol.liberfood.models.MealsByCategory
import com.miklegol.liberfood.models.Meal
import com.miklegol.liberfood.viewmodels.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.miklegol.liberfood.fragments.idMeal"
        const val MEAL_NAME = "com.miklegol.liberfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.miklegol.liberfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.miklegol.liberfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel


        popularItemsAdapter = MostPopularAdapter()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        preparePopularItemsRecyclerView()
        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClicked(object : CategoriesAdapter.OnItemCategoryClicked{
            override fun onClickListener(category: Category) {
                val intent = Intent(context, CategoryMealsActivity::class.java)
                intent.putExtra(CATEGORY_NAME,category.strCategory)
                startActivity(intent)
            }
        })
    }




    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observerCategoriesLiveData().observe(viewLifecycleOwner, Observer{ categories ->
                categoriesAdapter.setCategoryList(categories)

        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observerPopularItemsLiveData().observe(viewLifecycleOwner, object : Observer<List<MealsByCategory>>{
            override fun onChanged(mealList: List<MealsByCategory>) {
                popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
            }

        })
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener{
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)

            startActivity(intent)
        }
    }



    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal> {

            override fun onChanged(meal: Meal) {
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                randomMeal = meal
            }
        })
    }


}