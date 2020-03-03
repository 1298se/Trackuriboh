package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.SetRepository
import javax.inject.Inject

class SetViewModelFactory @Inject constructor(private val setRepository: SetRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetViewModel::class.java)) {
            return SetViewModel(
                setRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}