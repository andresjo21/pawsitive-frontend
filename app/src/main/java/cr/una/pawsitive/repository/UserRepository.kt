package cr.una.pawsitive.repository

import cr.una.pawsitive.service.UserService

class UserRepository constructor(
    private val userService: UserService
){
    suspend fun getUserById(id: Long) = userService.getUserById(id)

    suspend fun getLoggedUser() = userService.getLoggedUser()
}