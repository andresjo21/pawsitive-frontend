package cr.una.pawsitive.service

import cr.una.pawsitive.model.UserProfile
import cr.una.pawsitive.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("v1/users/{id}")
    suspend fun getUserById(@Path("id") id: Long) : Response<UserResponse>

    @GET("v1/users/me")
    suspend fun getLoggedUser() : Response<UserProfile>

    companion object{
        fun getInstance() : UserService {
            return ServiceManager.getService(UserService::class.java)
        }
    }
}