package sam.g.trackuriboh.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.databinding.DialogQuantitySelectorBinding
import sam.g.trackuriboh.ui.user_list.UserListDetailFragment

class QuantitySelectorDialogFragment : DialogFragment() {

    private lateinit var binding: DialogQuantitySelectorBinding

    private var initialQuantity = 0

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "QuantitySelectorDialogFragment_fragmentResultRequestKey"
        const val QUANTITY_DATA_KEY = "QuantitySelectorDialogFragment_userList"

        private const val ARG_INITIAL_QUANTITY = "QuantitySelectorDialogFragment_argInitialQuantity"

        /**
         * !IMPORTANT
         * Instead of using Dialog destinations with Jetpack navigation, we use the fragmentManagers.
         * This is because Jetpack Navigation uses the NavHost's fragmentManager and
         * only one listener can be set per key per fragmentManager, it's
         * not possible for multiple fragments to listen to the same key because adding a new listener will
         * remove the former one. So for one shot operations like Dialogs, we should just call [show] on them
         * with the caller's childFragmentManager instead of using navigation. Using SavedStateHandle
         * is also not a convenient options in the case of embedded fragments in a ViewPager, like
         * [UserListDetailFragment] because then they would all be observing the live data result.
         * Unless multiple observers need to be set for a result, I think this is the cleanest solution.
         *
         * "If you have multiple consumers and you're worried about the event being consumed multiple times,
         * you might need to reconsider your app architecture."
         *
         * https://developer.android.com/jetpack/guide/ui-layer/events
         */
        fun newInstance(quantity: Int = 0) =
            QuantitySelectorDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INITIAL_QUANTITY, quantity)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            initialQuantity = it.getInt(ARG_INITIAL_QUANTITY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogQuantitySelectorBinding.inflate(layoutInflater, null, false)

        binding.quantitySelectorComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {

                MdcTheme {
                    var quantity by rememberSaveable { mutableStateOf(initialQuantity) }

                    Content(
                        quantity = quantity,
                        onQuantityChanged = { quantity = it.coerceIn(0, 99) }
                    )
                }
            }
        }

        return binding.root
    }

    /**
     * Can't use viewbinding delegate here because [onCreateDialog] is called before [onCreateView]
     */

    @Composable
    private fun Content(
        quantity: Int,
        onQuantityChanged: (Int) -> Unit,
    ) {
        SimpleDialogForm(
            title = "Update Quantity",
            onPositiveButtonClick = { setResult(quantity) },
            onNegativeButtonClick = ::dismiss,
        ) {

            QuantitySelector(
                onQuantityChanged = onQuantityChanged,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                quantity = quantity,
                dense = true
            )
        }
    }

    private fun setResult(quantity: Int) {
        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(QUANTITY_DATA_KEY to quantity))
        dismiss()
    }


    @Preview
    @Composable
    private fun Preview() {
        MdcTheme {
            Content(
                quantity = 0,
                onQuantityChanged = { }
            )
        }
    }
}
