package com.miklegol.liberfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.miklegol.liberfood.R
import com.miklegol.liberfood.adapters.CategoryMealsAdapter
import com.miklegol.liberfood.databinding.ActivityCategoryMealsBinding
import com.miklegol.liberfood.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.miklegol.liberfood.fragments.HomeFragment.Companion.MEAL_ID
import com.miklegol.liberfood.fragments.HomeFragment.Companion.MEAL_NAME
import com.miklegol.liberfood.fragments.HomeFragment.Companion.MEAL_THUMB
import com.miklegol.liberfood.models.Meal
import com.miklegol.liberfood.viewmodels.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private var categoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val window = window
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this).get(CategoryMealsViewModel::class.java)
        startLoading()
        categoryMealsViewModel.getMealsByCategory(getCategory())

        categoryMealsViewModel.observeMeal().observe(this, object : Observer<List<Meal>> {
            override fun onChanged(t: List<Meal>) {
                if (t == null) {
                    hideLoading()
                    Toast.makeText(applicationContext, "No meals in this category", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    categoryMealsAdapter.setMealsList(t)
                    binding.tvCategoryCount.text = categoryName + " : " + t.size.toString()
                    hideLoading()
                }
            }
        })

        categoryMealsAdapter.setOnMealClickListener(object : CategoryMealsAdapter.SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, MealActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_NAME, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)


                startActivity(intent)
            }
        })

        binding.icArrowBack.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        }
    }

    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.g_black))
        }
    }

    private fun getCategory(): String {
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryName = x
        return x
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}
