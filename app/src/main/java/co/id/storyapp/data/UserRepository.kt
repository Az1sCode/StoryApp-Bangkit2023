package co.id.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import co.id.storyapp.data.api.ApiService
import co.id.storyapp.data.paging.StoriesPagingSource
import co.id.storyapp.data.pref.DetailResponse
import co.id.storyapp.data.pref.ListStoryItem
import co.id.storyapp.data.pref.RegisterResponse
import co.id.storyapp.data.pref.StoryResponse
import co.id.storyapp.data.pref.UploadResponse
import co.id.storyapp.data.pref.UserModel
import co.id.storyapp.data.pref.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService){

    fun postRegister(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.register(name, email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }

    }

    fun postLogin(email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.login(email, password)
            val name = successResponse.loginResult.name
            val token = successResponse.loginResult.token
            saveSession(UserModel(name,token,true))
            emit(Result.Success(successResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }

    }

    fun stories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiService)
            }
        ).liveData
    }

    fun getStories() = liveData {
        emit(Result.Loading)

        try {
            val responseSuccess = apiService.getStories(1,20,1)
            emit(Result.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getDetail(id: String) = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.getDetail(id)
            emit(Result.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun uploadStory(image: File, description: String) = liveData {
        emit(Result.Loading)

        val imageRequest = image.asRequestBody("text/plain".toMediaType())
        val descriptionRequest = description.toRequestBody("text/plain".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            imageRequest
        )

        try {
            val responseSuccess = apiService.uploadImage(multipartBody, descriptionRequest)
            emit(Result.Success(responseSuccess))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }

    }

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    companion object {

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreferences,
            apiService: ApiService,
            need: Boolean
        ): UserRepository? {
            if (userPreference == null || need) {
                synchronized(this) {
                    instance = UserRepository(userPreference, apiService)
                }
            }
            return instance
        }

    }

}