package com.example.bookapp.filters

import android.widget.Filter
import com.example.bookapp.adapters.AdapterCategory
import com.example.bookapp.models.ModelCategory

class FilterCategory:Filter {
    private var filerList: ArrayList<ModelCategory>

    private var adapterCategory: AdapterCategory

    constructor(filerList: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) : super() {
        this.filerList = filerList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()
            val filteredModels: ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until  filerList.size) {
                if (filerList[i].category.uppercase().contains(constraint)){
                    filteredModels.add(filerList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else {
            results.count = filerList.size
            results.values = filerList
        }

        return results
    }

    override fun publishResults(p0: CharSequence?, results: FilterResults) {

        adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategory>

        adapterCategory.notifyDataSetChanged()
    }
}

