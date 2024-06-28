package cr.una.pawsitive.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.una.pawsitive.model.BreedNewPost
import cr.una.pawsitive.repository.BreedRepository
import kotlinx.coroutines.*
import retrofit2.Response

class BreedViewModel constructor(
    private val breedRepository: BreedRepository
) : ViewModel() {
    val breedList = MutableLiveData<List<BreedNewPost>>()

    private var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    private val loading = MutableLiveData<Boolean>()

    fun findAllBreeds() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = breedRepository.getAllBreeds()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    breedList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    // using filter to get the breeds by animal type
    fun getBreedsByAnimalType(animalType: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = breedRepository.getAllBreeds()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    breedList.postValue(response.body()?.filter { it.animalType.name == animalType })
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