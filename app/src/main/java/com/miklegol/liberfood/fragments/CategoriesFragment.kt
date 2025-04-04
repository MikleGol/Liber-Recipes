package com.miklegol.liberfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.miklegol.liberfood.activities.CategoryMealsActivity
import com.miklegol.liberfood.activities.MainActivity
import com.miklegol.liberfood.adapters.CategoriesAdapter
import com.miklegol.liberfood.databinding.FragmentCategoriesBinding
import com.miklegol.liberfood.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.miklegol.liberfood.models.Category
import com.miklegol.liberfood.viewmodels.HomeViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeCategories()

        onCategoryClick()


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

    private fun observeCategories() {
        viewModel.observerCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }


}