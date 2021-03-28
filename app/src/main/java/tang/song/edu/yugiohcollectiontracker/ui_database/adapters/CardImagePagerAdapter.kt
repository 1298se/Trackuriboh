package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.databinding.CardImageContainerBinding
import javax.inject.Inject

class CardImagePagerAdapter @Inject constructor(
    private val requestManager: RequestManager
) : RecyclerView.Adapter<CardImagePagerAdapter.CardImageViewHolder>() {
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
            requestManager.load(imageUrl).into(binding.cardImageContainer)
        }
    }
}
