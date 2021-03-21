package com.example.mixologic.features.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.mixologic.R
import org.w3c.dom.Text

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var profileTitle: TextView
    private lateinit var pantryInfo: TextView
    private lateinit var recipeInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileTitle = view.findViewById(R.id.usernameTextView)
        pantryInfo = view.findViewById(R.id.pantryInfoTextView)
        recipeInfo = view.findViewById(R.id.recipeInfoTextView)

        profileTitle.text = profileViewModel.username
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}