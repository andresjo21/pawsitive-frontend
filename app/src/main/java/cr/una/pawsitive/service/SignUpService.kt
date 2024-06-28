package cr.una.pawsitive.service

import cr.una.pawsitive.model.LoginRequest
import cr.una.pawsitive.model.UserLoginResponse
import cr.una.pawsitive.model.UserSignUp
import cr.una.pawsitive.model.UserSignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("v1/users/signup")
    suspend fun signUp(@Body userSignUp: UserSignUp) : Response<UserSignUpResponse>

    companion object {
        fun getInstance() : SignUpService {
            return ServiceManager.getService(SignUpService::class.java)
        }
    }
}