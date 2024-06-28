package cr.una.pawsitive.repository

import cr.una.pawsitive.model.UserLoginResponse
import cr.una.pawsitive.model.UserSignUp
import cr.una.pawsitive.model.UserSignUpResponse
import cr.una.pawsitive.service.LoginService
import cr.una.pawsitive.service.SignUpService
import retrofit2.Response

class SignUpRepository constructor(
    private val signUpService: SignUpService
) {
    suspend fun signUp(userSignUp: UserSignUp): Response<UserSignUpResponse> {
        val response = signUpService.signUp(userSignUp)
        return response
    }
}