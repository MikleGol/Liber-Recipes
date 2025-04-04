package com.miklegol.liberfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miklegol.liberfood.databinding.CategoryItemBinding
import com.miklegol.liberfood.models.Category

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoriesList = ArrayList<Category>()
    lateinit var onItemClick: OnItemCategoryClicked

    fun setCategoryList(categoriesList: List<Category>){
        this.categoriesList = categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    fun onItemClicked(onItemClick: OnItemCategoryClicked){
        this.onItemClick = onItemClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context)))

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            tvCategoryName.text = categoriesList[position].strCategory

            Glide.with(holder.itemView)
                .load(categoriesList[position].strCategoryThumb)
                .into(imgCategory)
        }

        holder.itemView.setOnClickListener{
            onItemClick.onClickListener(categoriesList[position])
            }
        }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    interface OnItemCategoryClicked{
        fun onClickListener(category:Category)
    }

     class CategoryViewHolder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)
}