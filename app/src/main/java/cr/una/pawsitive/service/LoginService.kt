package cr.una.pawsitive.service

import cr.una.pawsitive.model.LoginRequest
import cr.una.pawsitive.model.UserLoginResponse
import cr.una.pawsitive.model.UserSignUp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("v1/users/login")
    suspend fun login(@Body userLogin: LoginRequest) : Response<UserLoginResponse>

    // logout with no response body
    @POST("v1/users/logout")
    suspend fun logout()

    companion object {
        fun getInstance() : LoginService {
            return ServiceManager.getService(LoginService::class.java)
        }
    }
}