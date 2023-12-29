package co.id.storyapp.view.upload

import androidx.lifecycle.ViewModel
import co.id.storyapp.data.UserRepository
import java.io.File

class AddStoryViewModel(private val userRepository: UserRepository): ViewModel() {
    fun uploadStory(image: File, description: String) = userRepository.uploadStory(image, description)

}