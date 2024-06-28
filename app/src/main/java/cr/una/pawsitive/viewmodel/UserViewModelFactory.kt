package cr.una.pawsitive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.una.pawsitive.repository.UserRepository
import cr.una.pawsitive.service.UserService

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(
                userRepository = UserRepository(
                    userService = UserService.getInstance()
                )
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}