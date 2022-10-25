package com.example.billboard.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.billboard.R

class CountryRecycleViewAdapter(var dialog: AlertDialog, var tvSelection: TextView) : RecyclerView.Adapter<CountryRecycleViewAdapter.SpinnerViewHolder>() {

    val countryList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_recycle_view_item, parent, false)
        return SpinnerViewHolder(view, dialog, tvSelection)
    }

    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) = holder.setData(countryList[position])

    override fun getItemCount(): Int = countryList.size

    class SpinnerViewHolder(itemView: View, var dialog: AlertDialog, var tvSelection: TextView): ViewHolder(itemView), View.OnClickListener {
        val tvItem: TextView = itemView.findViewById(R.id.country_recycle_view_item)
        private var itemText = ""

        fun setData(text: String) {
            tvItem.text = text
            itemText = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            tvSelection.text = itemText
            dialog.dismiss()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: ArrayList<String>) {
        countryList.clear()
        countryList.addAll(list)
        notifyDataSetChanged()
    }
}