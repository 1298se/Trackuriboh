package tang.song.edu.yugiohcollectiontracker.ui_transaction_form.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import tang.song.edu.yugiohcollectiontracker.data.types.ConditionType
import tang.song.edu.yugiohcollectiontracker.data.types.EditionType
import tang.song.edu.yugiohcollectiontracker.data.types.PlatformType
import tang.song.edu.yugiohcollectiontracker.data.types.TransactionType
import tang.song.edu.yugiohcollectiontracker.databinding.BottomSheetTransactionBinding
import tang.song.edu.yugiohcollectiontracker.ui_transaction_form.TransactionFormValidatorImpl
import tang.song.edu.yugiohcollectiontracker.ui_transaction_form.viewmodels.TransactionBottomSheetDialogViewModel

@AndroidEntryPoint
class TransactionBottomSheetDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private val mViewModel: TransactionBottomSheetDialogViewModel by viewModels()

    private val args: TransactionBottomSheetDialogFragmentArgs by navArgs()

    private lateinit var binding: BottomSheetTransactionBinding

    private lateinit var mCard: CardWithSetInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setOnShowListener {
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
            }
        }

        binding = BottomSheetTransactionBinding.inflate(layoutInflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            editionTypes = EditionType.values().toList()
            platformTypes = PlatformType.values().toList()
            transactionTypes = TransactionType.values().toList()
            conditionTypes = ConditionType.values().toList()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newTransactionSaveButton.setOnClickListener(this)

        initToolbar()

        lifecycleScope.launch {
            mCard = mViewModel.getCardDetailsById(args.cardId)
            binding.newTransactionCardNameEdittext.isEnabled = false
            initDropdowns()
        }
    }

    override fun onClick(view: View?) {
        val formValidator = TransactionFormValidatorImpl(binding, mCard)
        setFormIsEnabled(false)

        binding.apply {
            validator = formValidator
            executePendingBindings()
        }

        setFormIsEnabled(true)
        if (formValidator.isValid) {
            mViewModel.insertTransaction()
        }
    }

    private fun initToolbar() {
        binding.newTransactionToolbar.apply {
            setNavigationOnClickListener { dismiss() }
        }
    }

    private fun initDropdowns() {
        initCardNumberDropdown()
    }

    private fun initCardNumberDropdown() {
        val cardNumberList = mCard.sets.map { it.cardNumber }.toSet()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cardNumberList.toList())
        binding.newTransactionCardNumberTextview.apply {
            setAdapter(adapter)

            setOnItemClickListener { _, _, _, _ ->
                initRarityDropdown(this.text.toString())
            }

            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    return
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.newTransactionRarityLayout.isEnabled = false
                    if (cardNumberList.contains(text?.toString())) {
                        initRarityDropdown(text?.toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    return
                }

            })
        }
    }

    private fun initRarityDropdown(cardNumber: String?) {
        binding.newTransactionRarityLayout.isEnabled = true

        val rarityList = mutableListOf<String>()

        // If null is passed in as cardNumber, no cardNumber is selected so show all rarities
        mCard.sets.forEach { if (cardNumber ?: it.cardNumber == it.cardNumber && it.rarity != null) {
            rarityList.add(it.rarity)
        } }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rarityList)
        binding.newTransactionRarityTextview.setAdapter(adapter)
    }

    private fun setFormIsEnabled(enabled: Boolean) {
        binding.apply {
            newTransactionCardNameLayout.isEnabled = enabled
            newTransactionCardNumberLayout.isEnabled = enabled
            newTransactionRarityLayout.isEnabled = enabled
            newTransactionEditionLayout.isEnabled = enabled
            newTransactionPriceLayout.isEnabled = enabled
            newTransactionQuantityLayout.isEnabled = enabled
            newTransactionTypeLayout.isEnabled = enabled
            newTransactionPlatformLayout.isEnabled = enabled
            newTransactionConditionLayout.isEnabled = enabled
            newTransactionPartyNameLayout.isEnabled = enabled
            newTransactionTrackingLayout.isEnabled = enabled
        }
    }
}
