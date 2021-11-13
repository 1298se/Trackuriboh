package sam.g.trackuriboh.ui_database.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import javax.inject.Inject

class ImagePagerAdapter @Inject constructor() : RecyclerView.Adapter<ImagePagerAdapter.CardImageViewHolder>() {
    private var mImageList: List<String?>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardImageViewHolder {
        return CardImageViewHolder(ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun onBindViewHolder(holder: CardImageViewHolder, position: Int) {
        holder.bind(mImageList?.get(position))
    }

    override fun getItemCount(): Int {
        return mImageList?.size ?: 0
    }

    fun setImageList(imageList: List<String?>) {
        mImageList = imageList
        notifyItemRangeInserted(0, imageList.size)
    }

    inner class CardImageViewHolder(private val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {
        internal fun bind(imageUrl: String?) {
            Glide.with(itemView).load(imageUrl).placeholder(R.drawable.img_cardback).fitCenter().into(imageView)
        }
    }
}
