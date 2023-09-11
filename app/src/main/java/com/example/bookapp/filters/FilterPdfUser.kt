package com.example.bookapp.filters

import android.widget.Filter
import com.example.bookapp.adapters.AdapterPdfUser
import com.example.bookapp.models.ModelPdf

class FilterPdfUser: Filter {
    var filterList: ArrayList<ModelPdf>
    var adapterPdfUser: AdapterPdfUser

    constructor(filterList: ArrayList<ModelPdf>, adapterPdfUser: AdapterPdfUser) {
        this.filterList = filterList
        this.adapterPdfUser = adapterPdfUser
    }

    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
        var constraint: CharSequence? = constraint
        var results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()
            var filteredModels = ArrayList<ModelPdf>()
            for (i in filterList.indices) {
                if (filterList[i].title.uppercase().contains(constraint)) {
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

        return results
    }

    override fun publishResults(constraint: CharSequence?, resulsts: Filter.FilterResults) {
        adapterPdfUser.pdfArrayList = resulsts!!.values as ArrayList<ModelPdf>

        adapterPdfUser.notifyDataSetChanged()
    }
}