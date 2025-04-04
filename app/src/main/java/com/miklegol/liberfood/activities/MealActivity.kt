package com.miklegol.liberfood.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.miklegol.liberfood.R
import com.miklegol.liberfood.databinding.ActivityMealBinding
import com.miklegol.liberfood.database.MealDatabase
import com.miklegol.liberfood.fragments.HomeFragment
import com.miklegol.liberfood.models.Meal
import com.miklegol.liberfood.viewmodels.HomeViewModel
import com.miklegol.liberfood.viewmodels.HomeViewModelFactory
import com.miklegol.liberfood.viewmodels.MealViewModel
import com.miklegol.liberfood.viewmodels.MealViewModelFactory
import io.github.muddz.styleabletoast.StyleableToast

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel
    private lateinit var youtubeLink: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: HomeViewModel by lazy {
            val mealDatabase = MealDatabase.getInstance(this)
            val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
            ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
        }
        viewModel.observeFavoritesMealsLiveData().observe(this, Observer{ meals ->
            for(i in meals){
                if(mealId.equals(i.idMeal)){
                    binding.btnSave.setImageResource(R.drawable.ic_favorites_saved)
                    break
                }
            }
        })

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()
        onYoutubeImageClick()
        onFavoriteClick()

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

        binding.icArrowBack.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun onFavoriteClick() {
        binding.btnSave.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                StyleableToast.makeText(this, "Meal Saved!", R.style.BeigeToast).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null

    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal) {
                onResponseCase()
                val meal = t
                mealToSave = meal
                binding.tvCategoryInfo.text = "Category : ${meal!!.strCategory}"
                binding.tvAreaInfo.text = "Area : ${meal!!.strArea}"
                binding.tvInstructions.text = meal.strInstructions
                binding.tvInstructions.textSize = 14F
                youtubeLink = meal.strYoutube!!
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.titleTextView.text = mealName
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.beige_dark))
        binding.collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
        binding.toolbar.fitsSystemWindows = true
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvAreaInfo.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnSave.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}
