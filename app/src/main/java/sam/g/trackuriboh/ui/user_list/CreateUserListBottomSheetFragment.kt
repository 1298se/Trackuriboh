package sam.g.trackuriboh.ui.user_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.types.UserListType
import sam.g.trackuriboh.databinding.BottomSheetCreateUserListBinding
import sam.g.trackuriboh.databinding.ItemCreateUserListOptionBinding
import sam.g.trackuriboh.ui.common.SimpleTextFieldDialogFragment
import sam.g.trackuriboh.utils.viewBinding
import java.util.*


class CreateUserListBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: BottomSheetCreateUserListBinding by viewBinding(BottomSheetCreateUserListBinding::inflate)

    private var selectedType: UserListType? = null

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CreateUserListBottomSheetDialogFragment_fragmentResultRequestKey"
        const val USER_LIST_DATA_KEY = "CreateUserListBottomSheetDialogFragment_userList"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListView()
        initFragmentObservers()
    }

    private fun initFragmentObservers() {
        childFragmentManager.setFragmentResultListener(
            SimpleTextFieldDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val type = selectedType
            val name = bundle.getString(SimpleTextFieldDialogFragment.TEXT_DATA_KEY)

            if (type != null && name != null) {
                setFragmentResult(
                    FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(USER_LIST_DATA_KEY to UserList(name = name, creationDate = Date(), type = type))
                )

                dismiss()
            }
        }
    }

    private fun initListView() {
        with(binding.createUserListOptionsList) {
            adapter = CreateUserListOptionsAdapter(requireContext())
        }
    }

    inner class CreateUserListOptionsAdapter(
        context: Context
    ) : ArrayAdapter<UserListType>(context, 0, UserListType.values()) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var curView = convertView

            if (curView == null) {
                val binding = ItemCreateUserListOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                with(binding) {
                    val type = getItem(position)

                    val icon = when (type) {
                        UserListType.USER_LIST -> R.drawable.ic_baseline_playlist_add_24
                        // CollectionType.CHECKLIST -> R.drawable.ic_baseline_playlist_add_24
                        else -> null
                    }

                    icon?.let { itemCreateUserListOptionImage.setImageDrawable(AppCompatResources.getDrawable(context, it)) }

                    val text = when (type) {
                        UserListType.USER_LIST -> R.string.create_user_list_option
                        // CollectionType.CHECKLIST -> R.string.create_checklist_option
                        else -> null
                    }

                    itemCreateUserListOptionTextview.text = text?.let { getString(it) }

                    root.setOnClickListener {
                        selectedType = type

                        val title = when (selectedType) {
                            UserListType.USER_LIST -> getString(R.string.create_user_list_option)
                            else -> getString(R.string.create_user_list_option)
                        }

                        SimpleTextFieldDialogFragment.newInstance(title).show(childFragmentManager, null)
                    }
                }

                curView = binding.root
            }

            return curView
        }
    }
}