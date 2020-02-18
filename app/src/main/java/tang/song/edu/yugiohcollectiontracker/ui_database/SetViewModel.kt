package tang.song.edu.yugiohcollectiontracker.ui_database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import tang.song.edu.yugiohcollectiontracker.data.repository.SetRepository
import tang.song.edu.yugiohcollectiontracker.network.response.Resource

class SetViewModel(setRepository: SetRepository) : ViewModel() {
    val allSets = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        emit(setRepository.getAllSets())
    }
}
