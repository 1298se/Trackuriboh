package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sam.g.trackuriboh.databinding.BottomSheetFragmentCardPricesBinding
import sam.g.trackuriboh.ui_price.CardDetailPricesFragment
import sam.g.trackuriboh.viewBinding

/**
 * This contains the [CardDetailPricesFragment] in a BottomSheetDialog
 */
class CardPricesBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetFragmentCardPricesBinding::inflate)

    private val args: CardPricesBottomSheetDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardDetailPricesFragment = CardDetailPricesFragment.newInstance(args.skuIds.toList())
        childFragmentManager
            .beginTransaction()
            .replace(binding.cardPricesFragmentContainer.id, cardDetailPricesFragment)
            .commit()
    }
}
