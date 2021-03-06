package com.example.mixologic.features.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixologic.R

class CreateFragment : Fragment() {
    private lateinit var liquorAdapter: CreateAdapter
    private lateinit var ingredientAdapter: CreateAdapter

    private var liquorRecyclerView: RecyclerView? = null
    private var ingredientRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViews()
    }

    private fun initRecyclerViews() {
        ingredientRecyclerView = view?.findViewById(R.id.ingredientsRecyclerView)
        ingredientRecyclerView?.layoutManager = GridLayoutManager(ingredientRecyclerView?.context, 3, GridLayoutManager.HORIZONTAL, false)
        ingredientAdapter = CreateAdapter(false)
        ingredientRecyclerView?.adapter = ingredientAdapter

        liquorRecyclerView = view?.findViewById(R.id.liquorsRecyclerView)
        liquorRecyclerView?.layoutManager = GridLayoutManager(ingredientRecyclerView?.context, 3, GridLayoutManager.HORIZONTAL, false)
        liquorAdapter = CreateAdapter(true)
        liquorRecyclerView?.adapter = liquorAdapter

        liquorAdapter.addDummyData()
        ingredientAdapter.addDummyData()
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}