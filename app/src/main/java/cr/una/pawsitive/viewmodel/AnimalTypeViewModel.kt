package cr.una.pawsitive.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.AnimalType
import cr.una.pawsitive.model.AnimalTypeNewPost
import cr.una.pawsitive.repository.AnimalTypeRepository
import kotlinx.coroutines.*

class AnimalTypeViewModel constructor(
    private val animalTypeRepository: AnimalTypeRepository
) : ViewModel() {
    val animalTypeList = MutableLiveData<List<AnimalTypeNewPost>>()

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    fun findAllAnimalTypes() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = animalTypeRepository.getAllAnimalTypes()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    animalTypeList.postValue(response.body())
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