package cr.una.pawsitive.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.PostType
import cr.una.pawsitive.repository.PostTypeRepository
import kotlinx.coroutines.*

class PostTypeViewModel constructor(
    private val postTypeRepository: PostTypeRepository
) : ViewModel() {
    val postTypeList = MutableLiveData<List<PostType>>()

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    fun findAllPostTypes() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = postTypeRepository.getAllPostTypes()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    postTypeList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
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