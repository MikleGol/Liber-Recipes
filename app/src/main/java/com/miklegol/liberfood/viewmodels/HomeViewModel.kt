package com.miklegol.liberfood.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miklegol.liberfood.database.MealDatabase
import com.miklegol.liberfood.models.*
import com.miklegol.liberfood.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()
    private var savedStateRandomMeal : Meal ?= null

    fun getRandomMeal(){

        savedStateRandomMeal?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    savedStateRandomMeal = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("ERROR", t.message.toString())
            }

        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("ERROR", t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
               response.body()?.let { categoryList ->
                   categoriesLiveData.postValue(categoryList.categories)
               }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("ERROR", t.message.toString())
            }

        })
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observerPopularItemsLiveData(): LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observerCategoriesLiveData(): LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData() : LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun observeBottomSheetMeal() : LiveData<Meal> = bottomSheetMealLiveData

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun searchMeals(searchQuery : String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealsList = response.body()?.meals
            mealsList?.let{
                searchedMealsLiveData.postValue(it)
            }
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.d("ERROR", t.message.toString())
        }

    })

    fun observeSearchedMealsLiveData() : LiveData<List<Meal>> = searchedMealsLiveData

    fun getMealById(id : String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("ERROR", t.message.toString())
            }

        })
    }
}