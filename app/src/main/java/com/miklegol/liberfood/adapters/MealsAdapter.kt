package com.miklegol.liberfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miklegol.liberfood.databinding.MealItemBinding
import com.miklegol.liberfood.models.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoritesMealsAdapterViewHolder>() {

    private var mealsList = ArrayList<Meal>()
    lateinit var onMealClickListener: OnItemMealClicked

    fun setMealList(mealsList: List<Meal>) {
        this.mealsList = mealsList as ArrayList<Meal>
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemMealClicked) {
        this.onMealClickListener = onItemClickListener
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesMealsAdapterViewHolder {
        return FavoritesMealsAdapterViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FavoritesMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onMealClickListener.onClickListener(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemMealClicked {
        fun onClickListener(meal: Meal)
    }

    class FavoritesMealsAdapterViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)
}
