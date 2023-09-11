package com.example.bookapp.filters

import android.widget.Filter
import com.example.bookapp.adapters.AdapterPdfAdmin
import com.example.bookapp.models.ModelPdf

class FilterPdfAdmin: Filter {
    var filterList: ArrayList<ModelPdf>
    var adapterPdfAdmin: AdapterPdfAdmin

    constructor(filterList: ArrayList<ModelPdf>, adapterPdfAdmin: AdapterPdfAdmin) {
        this.filterList = filterList
        this.adapterPdfAdmin = adapterPdfAdmin
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        var results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().lowercase()
            var filteredModels = ArrayList<ModelPdf>()
            for (i in filterList.indices) {
                if (filterList[i].title.lowercase().contains(constraint)) {
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else {
            results.count = filterList.size
            results.values = filterList
        }

        return  results
    }

    override fun publishResults(constraint: CharSequence?, resulsts: FilterResults) {
        adapterPdfAdmin.pdfArrayList = resulsts!!.values as ArrayList<ModelPdf>

        adapterPdfAdmin.notifyDataSetChanged()
    }
}