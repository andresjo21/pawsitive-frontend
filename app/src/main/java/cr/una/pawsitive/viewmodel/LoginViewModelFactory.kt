package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.LoginRepository
import cr.una.pawsitive.service.LoginService

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(
                loginRepository = LoginRepository(
                    loginService = LoginService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}