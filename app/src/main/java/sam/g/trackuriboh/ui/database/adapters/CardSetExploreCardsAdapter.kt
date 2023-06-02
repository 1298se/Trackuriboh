package sam.g.trackuriboh.ui.database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.databinding.ItemCardSetExploreCardBinding

class CardSetExploreCardsAdapter(
    private val products: List<Product>,
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<CardSetExploreCardsAdapter.CardSetExploreCardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(cardId: Long)
    }

    inner class CardSetExploreCardViewHolder(private val binding: ItemCardSetExploreCardBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun bind(product: Product) {
            Glide.with(itemView)
                .load(product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.cardImage)

            binding.itemCardTitleTextview.text = product.name
            binding.itemCardMarketPriceTextview.text =
                itemView.context.getString(R.string.market_price_placeholder, product.marketPrice)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardSetExploreCardViewHolder {
        return CardSetExploreCardViewHolder(ItemCardSetExploreCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(products[bindingAdapterPosition].id)
            }
        }
    }

    override fun onBindViewHolder(holder: CardSetExploreCardViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() =
        products.size
}