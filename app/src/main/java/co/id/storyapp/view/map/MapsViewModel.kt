package co.id.storyapp.view.map

import androidx.lifecycle.ViewModel
import co.id.storyapp.data.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getStories() = repository.getStories()

}