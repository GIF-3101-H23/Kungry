package ca.ulaval.ima.mp.utilities


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val CLIENT_ID = "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0"
const val CLIENT_SECRET = "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLprUmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWleQGYyApC2a469l36TdlXFqAG1tpK"
const val INDENT = "   "

@Parcelize
data class CreateAccountCreate(
    val first_name : String,
    val last_name : String,
    val email : String,
    val password : String,
    val client_id : String = CLIENT_ID,
    val client_secret : String = CLIENT_SECRET,
) : Parcelable {
    fun getString() : String = "CreateAccountCreate :\n"+"prenom: $first_name, nom: $last_name; \nemail: $email;\npassword: $password;".prependIndent(INDENT)
}

@Parcelize
data class TokenOutput(
    val access_token : String,
    val token_type : String,
    val refresh_token : String,
    val scope : String,
    val expires_in : Int
) : Parcelable {
    fun getString() : String = "TokenOutput :\n" + ("access_token: $access_token, token_type: $token_type;\n" +
            "refresh_token: $refresh_token;\nscope: $scope, expire: $expires_in ").prependIndent(INDENT)
    fun print() = println(getString())
}

@Parcelize
data class AccountLogin(
    val email: String,
    val password: String,
    val client_id: String = CLIENT_ID,
    val client_secret: String = CLIENT_SECRET
) : Parcelable {
    fun getString() : String = "AccountLogin : \n" + "email: $email, password: $password".prependIndent(INDENT)
    fun print() = println(getString())
}

@Parcelize
data class Account(
    val total_review_count : Int,
    val last_name: String,
    val first_name: String,
    val email: String,
    val created : String,
    val updated : String,
    val user : Int
) : Parcelable {
    fun getString() : String = "Account : \n" + ("total_review_count: $total_review_count\nnom: $last_name, prenom: $first_name;\n" +
            "email: $email;\ncreated: $created, updated: $updated, user: $user").prependIndent(INDENT)
    fun print() = println(getString())
}

@Parcelize
data class RefreshTokenInput(
    val refresh_token: String,
    val client_id: String = CLIENT_ID,
    val client_secret: String = CLIENT_SECRET
) : Parcelable {
    fun getString() : String = "RefreshTokenInput : \n" + "refresh_token: $refresh_token".prependIndent(INDENT)
    fun print() = println(getString())
}


@Parcelize
data class Creatore(val first_name: String, val last_name: String) : Parcelable {
    fun getString() : String = "Creator : \n" +
            "prenom: $first_name, nom: $last_name".prependIndent(INDENT)
    fun print() = println(getString())
}
