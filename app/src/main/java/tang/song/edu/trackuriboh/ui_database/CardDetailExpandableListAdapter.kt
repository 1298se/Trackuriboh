package tang.song.edu.trackuriboh.ui_database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import tang.song.edu.trackuriboh.R
import tang.song.edu.trackuriboh.databinding.ItemCardSetBinding
import tang.song.edu.trackuriboh.databinding.ListHeaderBinding

class CardDetailExpandableListAdapter(
    private val listHeaders: List<String>,
    private val listDetails: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): Any {
        return listHeaders[groupPosition]
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val headerTitle = getGroup(groupPosition) as String
        var curConvertView = convertView

        if (curConvertView == null) {
            curConvertView = ListHeaderBinding.inflate(LayoutInflater.from(parent?.context), parent, false).root
        }

        curConvertView.findViewById<TextView>(R.id.header_title_textview)?.text = headerTitle

        return curConvertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listDetails[listHeaders[groupPosition]]?.size ?: 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listDetails[listHeaders[groupPosition]]?.get(childPosition) ?: ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val detailText = getChild(groupPosition, childPosition) as String
        var curConvertView = convertView

        if (curConvertView == null) {
            curConvertView = ItemCardSetBinding.inflate(LayoutInflater.from(parent?.context), parent, false).root
        }

        curConvertView.findViewById<TextView>(R.id.card_set_title_textview).text = detailText

        return curConvertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listHeaders.size
    }

}