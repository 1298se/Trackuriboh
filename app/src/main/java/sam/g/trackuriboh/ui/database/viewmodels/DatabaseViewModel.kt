package sam.g.trackuriboh.ui.database.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor() : ViewModel() {

    var hasUserInitiatedSearch = false
}