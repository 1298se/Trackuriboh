package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.data.types.EditionType
import tang.song.edu.yugiohcollectiontracker.data.types.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.types.TransactionType
import tang.song.edu.yugiohcollectiontracker.databinding.BottomSheetTransactionBinding
import tang.song.edu.yugiohcollectiontracker.ui_inventory.adapters.EnumArrayAdapter
import tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels.TransactionBottomSheetDialogViewModel
import tang.song.edu.yugiohcollectiontracker.ui_inventory.viewmodels.TransactionData
import tang.song.edu.yugiohcollectiontracker.viewBinding
import java.util.*

@AndroidEntryPoint
class TransactionBottomSheetDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private val mViewModel: TransactionBottomSheetDialogViewModel by viewModels()

    private val args: TransactionBottomSheetDialogFragmentArgs by navArgs()

    private val binding by viewBinding(BottomSheetTransactionBinding::inflate)

    private lateinit var mCard: CardWithSetInfo

    private val KEY_EDITION_TYPE = "Edition_Type"
    private val KEY_TRANSACTION_TYPE = "Transaction_Type"
    private val KEY_PLATFORM_TYPE = "Platform_type"

    private var mEditionType: EditionType? = null
    private var mTransactionType: TransactionType? = null
    private var mPlatformType: PlatformType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditionType = EditionType.fromInt(savedInstanceState?.getInt(KEY_EDITION_TYPE, -1))
        mTransactionType = TransactionType.fromInt(savedInstanceState?.getInt(KEY_TRANSACTION_TYPE, -1))
        mPlatformType = PlatformType.fromInt(savedInstanceState?.getInt(KEY_PLATFORM_TYPE, -1))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.setOnShowListener {
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newTransactionSaveButton.setOnClickListener(this)

        initToolbar()

        lifecycleScope.launch {
            mCard = mViewModel.getCardDetailsById(args.cardId)
            binding.newTransactionNameEdittext.apply {
                setText(mCard.card.name)
                inputType = InputType.TYPE_NULL
            }
                initDropdowns()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_EDITION_TYPE, mEditionType?.value ?: -1)
        outState.putInt(KEY_TRANSACTION_TYPE, mTransactionType?.value ?: -1)
        outState.putInt(KEY_PLATFORM_TYPE, mPlatformType?.value ?: -1)
    }

    override fun onClick(view: View?) {
        val transactionData = TransactionData(
            cardId = mCard.card.cardId,
            cardName = mCard.card.name,
            cardNumber = binding.newTransactionCardNumberTextview.text.toString(),
            cardImageURL = mCard.card.getDefaultImageURL(),
            rarity = binding.newTransactionRarityTextview.text.toString(),
            edition = mEditionType,
            quantity = binding.newTransactionQuantityEdittext.text.toString().toInt(),
            date = Date(),
            buyerSellerName = binding.newTransactionBuyerSellerEdittext.text.toString(),
            transactionType = mTransactionType!!,
            trackingNumber = binding.newTransactionTrackingEdittext.text.toString(),
            salePlatform = mPlatformType!!,
            price = binding.newTransactionPriceEdittext.text.toString().toDouble()
        )

        mViewModel.insertTransaction(transactionData)
    }

    private fun initToolbar() {
        binding.newTransactionToolbar.apply {
            setNavigationOnClickListener { dismiss() }
        }
    }

    private fun initDropdowns() {
        initCardNumberDropdown(null)
        initRarityDropdown(null)
        initEditionDropdown()
        initTransactionTypeDropdown()
        initPlatformDropdown()
    }

    private fun initCardNumberDropdown(rarity: String?) {
        val cardNumberList = mutableSetOf<String>()
        // Add to set because there can be multiple rarities for each card number
        mCard.sets.forEach { if (rarity ?: it.rarity == it.rarity) {
            cardNumberList.add(it.cardNumber)
        } }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cardNumberList.toList())
        binding.newTransactionCardNumberTextview.apply {
            setAdapter(adapter)

            setOnItemClickListener { _, _, _, _ ->
                initRarityDropdown(this.text.toString())
            }
        }
    }

    private fun initRarityDropdown(cardNumber: String?) {
        val rarityList = mutableListOf<String>()

        // If null is passed in as cardNumber, no cardNumber is selected so show all rarities
        mCard.sets.forEach { if (cardNumber ?: it.cardNumber == it.cardNumber && it.rarity != null) {
            rarityList.add(it.rarity)
        } }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rarityList)
        binding.newTransactionRarityTextview.apply {
            setAdapter(adapter)

            setOnItemClickListener { _, _, _, _ ->
                initCardNumberDropdown(this.text.toString())
            }
        }
    }

    private fun initEditionDropdown() {
        val adapter = EnumArrayAdapter(requireContext(), EditionType.values().toList())
        binding.newTransactionEditionTextview.apply {
            setAdapter(adapter)

            setOnItemClickListener { adapterView, view, position, id ->
                mEditionType = adapterView.getItemAtPosition(position) as EditionType
            }
        }
    }

    private fun initTransactionTypeDropdown() {
        val adapter = EnumArrayAdapter(requireContext(), TransactionType.values().toList())
        binding.newTransactionTypeTextview.apply {
            setAdapter(adapter)

            setOnItemClickListener { adapterView, view, position, id ->
                mTransactionType = adapterView.getItemAtPosition(position) as TransactionType
            }
        }

    }

    private fun initPlatformDropdown() {
        val adapter = EnumArrayAdapter(requireContext(), PlatformType.values().toList())
        binding.newTransactionPlatformTextview.apply {
            setAdapter(adapter)

            setOnItemClickListener { adapterView, view, position, id ->
                mPlatformType = adapterView.getItemAtPosition(position) as PlatformType
            }
        }
    }
}
