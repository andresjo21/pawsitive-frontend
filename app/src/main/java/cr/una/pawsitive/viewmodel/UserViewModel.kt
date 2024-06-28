package cr.una.pawsitive.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.UserProfile
import cr.una.pawsitive.model.UserResponse
import cr.una.pawsitive.repository.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class StateUser {
    object Loading : StateUser()
    data class Success(val user: UserProfile?) : StateUser()
    data class SuccessList(val userList: List<UserResponse>?) : StateUser()
    data class Error(val message: String) : StateUser()
}

class UserViewModel constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableLiveData<StateUser>()
    val state: LiveData<StateUser> get() = _state

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getUser() {
        _state.value = StateUser.Loading
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = userRepository.getLoggedUser()
            withContext(Dispatchers.Main) {
                _state.postValue(
                    if (response.isSuccessful) StateUser.Success(response.body())
                    else StateUser.Error("Error : ${response.message()} ")
                )
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}