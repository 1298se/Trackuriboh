package sam.g.trackuriboh.ui.collection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.types.CollectionType
import sam.g.trackuriboh.databinding.BottomSheetCreateCollectionBinding
import sam.g.trackuriboh.databinding.ItemCollectionTypeBinding
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding
import java.util.*


class CreateCollectionBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding: BottomSheetCreateCollectionBinding by viewBinding(BottomSheetCreateCollectionBinding::inflate)

    private var selectedType: CollectionType? = null

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CreateCollectionBottomSheetDialogFragment_fragmentResultRequestKey"
        const val COLLECTION_DATA_KEY = "CreateCollectionBottomSheetDialogFragment_collection"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOptions()
        initFragmentObservers()
    }

    private fun initFragmentObservers() {
        parentFragmentManager.setFragmentResultListener(
            AddEditCollectionDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val type = selectedType
            val name = bundle.getString(AddEditCollectionDialogFragment.COLLECTION_NAME_DATA_KEY)

            if (type != null && name != null) {
                setFragmentResult(
                    FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(COLLECTION_DATA_KEY to UserList(name = name, creationDate = Date(), type = type))
                )

                findNavController().popBackStack()
            }
        }
    }

    private fun initOptions() {
        with(binding.createCollectionList) {
            adapter = CollectionTypeAdapter(requireContext())
        }
    }

    inner class CollectionTypeAdapter(
        context: Context
    ) : ArrayAdapter<CollectionType>(context, 0, CollectionType.values()) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var curView = convertView

            if (curView == null) {
                val binding = ItemCollectionTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                with(binding) {
                    val type = getItem(position)!!.also { selectedType = it }

                    val icon = when (selectedType) {
                        CollectionType.COLLECTION -> R.drawable.ic_outline_add_photo_alternate_24
                        // CollectionType.CHECKLIST -> R.drawable.ic_baseline_playlist_add_24
                        else -> null
                    }

                    icon?.let { itemCollectionTypeImage.setImageDrawable(AppCompatResources.getDrawable(context, it)) }

                    val text = when (selectedType) {
                        CollectionType.COLLECTION -> R.string.create_collection_option
                        // CollectionType.CHECKLIST -> R.string.create_checklist_option
                        else -> null
                    }

                    itemCollectionTypeTextview.text = text?.let { getString(it) }

                    root.setOnClickListener { _ ->
                        findNavController().safeNavigate(
                            CreateCollectionBottomSheetDialogFragmentDirections
                                .actionCreateCollectionBottomSheetDialogFragmentToAddEditCollectionDialogFragment(type)
                        )
                    }
                }

                curView = binding.root
            }

            return curView
        }
    }
}