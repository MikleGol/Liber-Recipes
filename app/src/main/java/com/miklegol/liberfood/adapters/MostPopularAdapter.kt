package com.miklegol.liberfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miklegol.liberfood.databinding.PopularItemsBinding
import com.miklegol.liberfood.models.MealsByCategory

class MostPopularAdapter() : RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick:((MealsByCategory) -> Unit)
    private var mealList = ArrayList<MealsByCategory>()
    var onLongItemClick:((MealsByCategory) -> Unit)? = null

    fun setMeals(mealsList: ArrayList<MealsByCategory>){
        this.mealList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener{
            onLongItemClick?.invoke(mealList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class PopularMealViewHolder(val binding : PopularItemsBinding) : RecyclerView.ViewHolder(binding.root)
}