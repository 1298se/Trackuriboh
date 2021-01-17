package tang.song.edu.yugiohcollectiontracker.ui_inventory.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import tang.song.edu.yugiohcollectiontracker.data.types.StringResourceEnum

class EnumArrayAdapter<T: StringResourceEnum>(
    context: Context,
    private val itemList: List<T>
) : ArrayAdapter<T>(context, android.R.layout.simple_dropdown_item_1line), Filterable {
    private val filteredList = ArrayList<T>(itemList)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        view.findViewById<TextView>(android.R.id.text1).text = parent.context.getString(filteredList[position].getResourceId())

        view.tag = filteredList[position]
        return view
    }

    override fun getFilter(): Filter {
        return EnumFilter()
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getItem(position: Int): T {
        return filteredList[position]
    }

    inner class EnumFilter : Filter() {
        private val suggestions =  ArrayList<T>()

        override fun performFiltering(constraints: CharSequence?): FilterResults {
            suggestions.clear()

            if (constraints.isNullOrEmpty()) {
                suggestions.addAll(itemList)
            } else {
                for (item in itemList) {
                    if (context.getString(item.getResourceId()).startsWith(constraints)) {
                        suggestions.add(item)
                    }
                }
            }

            return FilterResults().apply {
                values = suggestions
                count = suggestions.size
            }
        }

        override fun publishResults(constraints: CharSequence?, results: FilterResults?) {
            filteredList.apply {
                clear()
                addAll(results?.values as Collection<T>)
            }
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return if (resultValue is StringResourceEnum) {
                context.getString(resultValue.getResourceId())
            } else {
                super.convertResultToString(resultValue)
            }
        }
    }
}
