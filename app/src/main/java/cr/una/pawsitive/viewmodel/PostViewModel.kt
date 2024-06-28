package cr.una.pawsitive.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.AiResponse
import cr.una.pawsitive.model.NewPost
import cr.una.pawsitive.model.PetAiRequest
import cr.una.pawsitive.model.Post
import cr.una.pawsitive.model.PostModel
import cr.una.pawsitive.repository.PostRepository
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

sealed class StatePost {
    object Loading : StatePost()
    data class Success(val post: PostModel?) : StatePost()
    data class SuccessList(val postList: List<PostModel>?) : StatePost()
    data class Error(val message: String) : StatePost()
}

class PostViewModel constructor(
    val postRepository: PostRepository
) : ViewModel() {
    private val _state = MutableLiveData<StatePost>()
    val state: LiveData<StatePost> get() = _state

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun findAllPosts() {
        _state.value = StatePost.Loading
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            try {
                val response = postRepository.getAllPosts()
                withContext(Dispatchers.Main) {
                    _state.postValue(
                        if (response.isSuccessful) {
                            val postList = response.body()
                            //For each age in postList with the format of 2024-06-01T18:37:24.854+00:00 calculate the age
                            StatePost.SuccessList(response.body())
                        }
                        else StatePost.Error("Error : ${response.message()} ")
                    )
                }
            } catch (e: Exception) {
                onError("Exception: ${e.message}")
            }
        }
    }

    //Ai request
//    // LiveData to hold the AiResponse
//    private val _aiResponse = MutableLiveData<AiResponse>()
//    val aiResponse: LiveData<AiResponse> get() = _aiResponse
//    fun getAiInformation(petAiRequest: PetAiRequest, postModel: PostModel) {
//        _state.value = StatePost.Loading
//        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            try {
//                val response = postRepository.getAiInformation(petAiRequest)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        postModel.aiResponse.value = response.body()
//                        _state.value = StatePost.Success(null)
//                    } else {
//                        _state.value = StatePost.Error("Error : ${response.message()}")
//                    }
//                }
//            } catch (e: Exception) {
//                onError("Exception: ${e.message}")
//            }
//        }
//    }

    /*fun getPostById(id: Long) {
        _state.value = StatePost.Loading
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val post = postRepository.getPostById(id)
                withContext(Dispatchers.Main) {
                    _post.value = post
                    _state.value = StatePost.Success(null)
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }*/

    fun createPost(newPost: NewPost){
        _state.value = StatePost.Loading
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = postRepository.createPost(newPost)
            withContext(Dispatchers.Main) {
                // if you're using postValue I don't think you need to switch to Dispatchers.Main?
                _state.postValue(
                    // when you get a response, the state is now either Success or Error
                    (if (response.isSuccessful) {
                        StatePost.Success(response.body() as PostModel)
                    } else {
                        StatePost.Error("Error : ${response.message()} ")
                        onError("Error : ${response.message()}")
                    }) as StatePost?
                )
            }
        }
    }

    private fun onError(message: String) {
        //_state.value = StatePost.Error(message)
        _state.postValue(StatePost.Error(message))
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}