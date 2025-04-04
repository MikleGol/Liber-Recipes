package com.miklegol.liberfood.viewmodels

import com.miklegol.liberfood.models.MealsResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miklegol.liberfood.models.*
import com.miklegol.liberfood.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response


class CategoryMealsViewModel : ViewModel() {
    private var mutableMeal = MutableLiveData<List<Meal>>()

    fun getMealsByCategory(category:String){
        RetrofitInstance.api.getMealsByCategory(category).enqueue(object : Callback<MealsResponse>{
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMeal.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.d("123123",t.message.toString())
            }

        })
    }

    fun observeMeal():LiveData<List<Meal>>{
        return mutableMeal
    }
}