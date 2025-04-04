package com.miklegol.liberfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miklegol.liberfood.databinding.MealItemBinding
import com.miklegol.liberfood.models.Meal

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {

    private var mealList : List<Meal> = ArrayList()
    private lateinit var setOnMealClickListener: SetOnMealClickListener

    fun setMealsList(mealList:  List<Meal>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    fun setOnMealClickListener(setOnMealClickListener: SetOnMealClickListener) {
        this.setOnMealClickListener = setOnMealClickListener
    }

    inner class CategoryMealsViewModel(val binding : MealItemBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(MealItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        holder.binding.apply {
            tvMealName.text = mealList[position].strMeal
            Glide.with(holder.itemView)
                .load(mealList[position].strMealThumb)
                .into(imgMeal)
        }


        holder.itemView.setOnClickListener {
            setOnMealClickListener.setOnClickListener(mealList[position])
        }

    }



    override fun getItemCount(): Int {
        return mealList.size
    }
    interface SetOnMealClickListener {
        fun setOnClickListener(meal: Meal)
    }

}