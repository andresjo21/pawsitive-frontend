package cr.una.pawsitive.model

import java.util.Date

data class User(
    var userId: Long? = null,
    var createDate: Date? = null,
    var email: String? = null,
    var enabled: Boolean? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var password: String? = null
)

data class UserProfile(
    var userId: Long? = null,
    var email: String? = null,
    var enabled: Boolean? = null,
    var firstName: String? = null,
    var lastName: String? = null,
)

data class UserSignUp(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
)

data class UserSignUpResponse(
    var success: Boolean? = null,
)

data class UserResponse(
    var userId: Long? = null,
    var createDate: Date? = null,
    var email: String? = null,
    var enabled: Boolean? = null,
    var firstName: String? = null,
    var lastName: String? = null
)