package sam.g.trackuriboh.ui.inventory_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.MainGraphDirections
import sam.g.trackuriboh.databinding.FragmentInventoryDetailBinding
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class InventoryDetailFragment : Fragment() {
    private val binding: FragmentInventoryDetailBinding by viewBinding(
        FragmentInventoryDetailBinding::inflate
    )

    private val viewModel: InventoryDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.inventoryDetailComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    val inventoryWithSkuMetadataAndTransactions by viewModel.inventoryWithSkuMetadataAndTransactions.observeAsState()

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = inventoryWithSkuMetadataAndTransactions?.inventoryWithSkuMetadata?.skuWithMetadata?.productWithCardSet?.product?.name
                                            ?: ""
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = findNavController()::navigateUp) {
                                        Icon(Icons.Filled.ArrowBack, "Navigate up")
                                    }
                                },
                                backgroundColor = MaterialTheme.colors.surface
                            )
                        },
                        content = { padding ->
                            inventoryWithSkuMetadataAndTransactions?.let {
                                InventoryDetailScreen(
                                    it,
                                    Modifier.padding(padding),
                                    viewModel::deleteTransaction,
                                    ::navigateToAddTransactionFragment,
                                    ::navigateToProductDetailsFragment,
                                )
                            }
                        })
                }
            }
        }

        return binding.root
    }

    private fun navigateToAddTransactionFragment() {
        findNavController().navigate(
            InventoryDetailFragmentDirections.actionInventoryDetailFragmentToAddTransactionFragment(
                viewModel.inventoryId
            )
        )
    }

    private fun navigateToProductDetailsFragment(productId: Long) {
        findNavController().navigate(
            MainGraphDirections.actionGlobalCardDetailFragment(productId)
        )
    }
}
