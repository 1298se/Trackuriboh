package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.network.response.SetResponse

class SetListAdapter : RecyclerView.Adapter<SetListAdapter.SetViewHolder>() {
    private var mSetList: List<SetResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_set, parent, false)

        return SetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val item = mSetList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mSetList.size
    }

    fun setItems(setList: List<SetResponse>?) {
        mSetList = setList ?: ArrayList()
        notifyDataSetChanged()
    }

    inner class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(item: SetResponse) {
            itemView.findViewById<TextView>(R.id.item_title_textview).text = (item.setName)
        }
    }
}