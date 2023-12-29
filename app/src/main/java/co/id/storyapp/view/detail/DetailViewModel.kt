package co.id.storyapp.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import co.id.storyapp.data.UserRepository

class DetailViewModel (private val userRepository: UserRepository): ViewModel() {

    fun getDetail(id: String) = userRepository.getDetail(id)

    fun getSession() = userRepository.getSession().asLiveData()
}