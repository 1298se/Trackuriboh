package sam.g.trackuriboh.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import sam.g.trackuriboh.databinding.FragmentListPickerBinding
import sam.g.trackuriboh.databinding.ItemCheckedBinding
import sam.g.trackuriboh.utils.viewBinding


class ListPickerFragment : Fragment() {
    private val args by navArgs<ListPickerFragmentArgs>()
    private val binding by viewBinding(FragmentListPickerBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.listPickerList.adapter = CheckableItemAdapter(requireContext(), args.items)
    }

    inner class CheckableItemAdapter(
        context: Context,
        items: Array<String>
    ) : ArrayAdapter<String>(context, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var curView = convertView

            val binding = if (curView == null) {
                ItemCheckedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            } else {
                ItemCheckedBinding.bind(curView)
            }

            binding.textView.text = getItem(position)

            return binding.root
        }
    }
}