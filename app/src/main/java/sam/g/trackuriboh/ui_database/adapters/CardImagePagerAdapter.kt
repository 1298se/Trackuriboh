package sam.g.trackuriboh.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sam.g.trackuriboh.databinding.CardImageContainerBinding
import javax.inject.Inject

class CardImagePagerAdapter @Inject constructor() : RecyclerView.Adapter<CardImagePagerAdapter.CardImageViewHolder>() {
    private var cardImageList: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardImageViewHolder {
        return CardImageViewHolder(CardImageContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardImageViewHolder, position: Int) {
        holder.bind(cardImageList?.get(position))
    }

    override fun getItemCount(): Int {
        return cardImageList?.size ?: 0
    }

    fun setImageList(imageList: List<String>?) {
        cardImageList = imageList
        notifyDataSetChanged()
    }

    inner class CardImageViewHolder(private val binding: CardImageContainerBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun bind(imageUrl: String?) {
            Glide.with(itemView).load(imageUrl).into(binding.cardImageContainer)
        }
    }
}
