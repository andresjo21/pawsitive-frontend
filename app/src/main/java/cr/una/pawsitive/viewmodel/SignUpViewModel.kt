package cr.una.pawsitive.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.UserSignUp
import cr.una.pawsitive.repository.SignUpRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private var job: Job? = null
    private val _signUpResponse = MutableLiveData<Boolean>()
    val signUpResponse: LiveData<Boolean> = _signUpResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun signUp(user: UserSignUp) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val success = signUpRepository.signUp(user)
            _signUpResponse.postValue(success.isSuccessful)
        }
    }

    private fun onError(message: String) {
        _errorMessage.postValue(message)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
