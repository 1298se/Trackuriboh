package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tang.song.edu.yugiohcollectiontracker.data.repository.CardSetRepository
import javax.inject.Inject

class SetViewModelFactory @Inject constructor(private val cardSetRepository: CardSetRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetViewModel::class.java)) {
            return SetViewModel(
                cardSetRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}