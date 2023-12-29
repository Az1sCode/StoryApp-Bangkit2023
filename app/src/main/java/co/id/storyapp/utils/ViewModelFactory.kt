package co.id.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.id.storyapp.data.UserRepository
import co.id.storyapp.di.Injection
import co.id.storyapp.view.detail.DetailViewModel
import co.id.storyapp.view.login.LoginViewModel
import co.id.storyapp.view.main.MainViewModel
import co.id.storyapp.view.map.MapsViewModel
import co.id.storyapp.view.signup.SignUpViewModel
import co.id.storyapp.view.upload.AddStoryViewModel

class ViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, need: Boolean): ViewModelFactory {
            synchronized(this) {
                if (instance == null || need) {
                    instance = Injection.provideRepository(context)?.let { ViewModelFactory(it) }
                }
            }
            return instance as ViewModelFactory
        }

    }
}