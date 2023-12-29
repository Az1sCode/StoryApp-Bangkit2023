package co.id.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.id.storyapp.data.UserRepository
import co.id.storyapp.data.pref.ListStoryItem
import co.id.storyapp.data.pref.StoryResponse
import co.id.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

   val story: LiveData<PagingData<ListStoryItem>> = userRepository.stories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
