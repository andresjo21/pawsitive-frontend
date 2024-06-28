package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.SignUpRepository
import cr.una.pawsitive.service.SignUpService

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            SignUpViewModel(
                signUpRepository = SignUpRepository(
                    signUpService = SignUpService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}