package com.example.billboard.fragments

/*
Фрагмент для выбора страны и города
 */

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billboard.databinding.FragmentNewBillBinding
import com.example.billboard.dialogs.SpinnerDialogHelper
import com.example.billboard.utils.CityHelper

class NewBillFragment : Fragment() {

    private lateinit var binding: FragmentNewBillBinding
    val spinnerDialogHelper = SpinnerDialogHelper()
    lateinit var countryTitle: String
    private lateinit var cont: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cont = inflater.context
        binding = FragmentNewBillBinding.inflate(inflater, container, false)

        binding.selectedCountry.setOnClickListener { countryOnClick() }

        binding.selectedCity.setOnClickListener { cityOnClick() }

        binding.goBackButton.setOnClickListener { goBack() }

        return binding.root
    }

    private fun countryOnClick() {
        spinnerDialogHelper.showSpinnerDialog(
            cont,
            CityHelper.getAllCountries(cont),
            binding.selectedCountry
        )
    }

    private fun cityOnClick() {
        countryTitle = binding.selectedCountry.text.toString()
        spinnerDialogHelper.showSpinnerDialog(
            cont,
            CityHelper.getAllCities(countryTitle, cont),
            binding.selectedCity
        )
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewBillFragment()
    }
}