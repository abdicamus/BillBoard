package com.example.billboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billboard.utils.CountryRecycleViewAdapter
import com.example.billboard.R
import com.example.billboard.utils.CityHelper

class SpinnerDialogHelper {

    fun showSpinnerDialog(context: Context, list: ArrayList<String>, tvSelection: TextView) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = CountryRecycleViewAdapter(dialog, tvSelection)
        val recycleView = rootView.findViewById<RecyclerView>(R.id.dialog_country_rv)
        val searchView = rootView.findViewById<SearchView>(R.id.dialog_country_sv)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = adapter
        adapter.updateAdapter(list)
        dialog.setView(rootView)
        setSearchView(adapter, list, searchView, recycleView)
        dialog.show()
    }

    private fun setSearchView(adapter: CountryRecycleViewAdapter, list: ArrayList<String>, searchView: SearchView?, recycleView: RecyclerView) {
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CityHelper.filterListData(list, newText)
                if (newText.isNullOrEmpty()) {
                    recycleView.visibility = View.GONE
                } else {
                    recycleView.visibility = View.VISIBLE
                }
                adapter.updateAdapter(tempList)
                return true
            }
        })
    }

}