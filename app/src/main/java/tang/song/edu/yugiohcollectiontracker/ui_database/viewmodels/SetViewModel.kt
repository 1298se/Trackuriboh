package tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import tang.song.edu.yugiohcollectiontracker.data.repository.SetRepository

class SetViewModel(setRepository: SetRepository) : ViewModel() {
    val setListResult = liveData {
        emit(setRepository.getAllSets())
    }
}
