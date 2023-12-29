package co.id.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.id.storyapp.data.UserRepository
import co.id.storyapp.data.pref.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository): ViewModel() {

    fun postRegister(username: String, email: String, password: String) = userRepository.postRegister(username,email, password)

}