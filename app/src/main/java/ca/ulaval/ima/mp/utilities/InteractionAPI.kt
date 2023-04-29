package ca.ulaval.ima.mp.utilities


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Exception


private val api = InteractionAPI( "https://kungry.infomobile.app/api/v1/account")
fun getInstanceAPI() : InteractionAPI = api

@Parcelize
data class InteractionAPI(val host : String) : Parcelable {

    // initialisation d'attributs

    //private val scheme = "https"

    private val client = OkHttpClient()

    var token: TokenOutput? = null
    var prevToken: TokenOutput? = null

    var connected = false

    var error = false

    var accountAlreadyExists = false
    var accountDoesntExist = false
    var notFound = false

    var account: Account? = null

    var requestActive = false



    // fonctions requêtes API account
    /**
     * Fonction qui exécute POST/account/
     *
     * @param[data] information de création de compte
     */
    fun POSTaccount(data: CreateAccountCreate)
    {
        val data_json = createJSONFromCreateAccountCreate(data)
        val formBody: RequestBody =
            data_json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://kungry.infomobile.app/api/v1/account/")
            .post(formBody)
            .build()
        println("Connecting to Url")

        val response = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requestActive = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Not connected")
                        if (response.code == 409) {
                            accountAlreadyExists = true
                            //prevToken
                        }
                        //error = true
                        requestActive = false
                        throw IOException("Unexpected code $response")
                    }
                    else {
                        println("Connected")

                        val str_json = response.body?.string()

                        val obj = JSONObject(str_json)

                        token = createTokenOutputFromJSON(obj.getJSONObject("content"))

                        connected = true
                        requestActive = false
                    }
                }
            }
        })
    }

    /**
     * Fonction qui exécute POST/account/login/
     *
     * @param[data] information de connexion
     */
    fun POSTaccountLogin(data: AccountLogin) {
        requestActive = true

        token = null

        val data_json = createJSONFromAccountLogin(data)

        val formBody: RequestBody =
            data_json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://kungry.infomobile.app/api/v1/account/login")
            .post(formBody)
            .build()
        println("Connecting to Url")

        val response = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requestActive = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) {
                        if (response.code == 404) accountDoesntExist = true
                        //error = true
                        requestActive = false
                        throw IOException("Unexpected code $response")
                    }

                    val str_json = response.body?.string()

                    val obj = JSONObject(str_json)

                    token = createTokenOutputFromJSON(obj.getJSONObject("content"))

                    connected = true
                    requestActive = false
                }
            }
        })
    }

    /**
     * Fonction qui exécute GET/account/me/
     */
    fun GETaccountMe() {
        requestActive = true

        account = null

        val request = Request.Builder()
            .url("https://kungry.infomobile.app/api/v1/account/me")
            .header("Authorization", "${token?.token_type} ${token?.access_token}")
            .build()
        println("Connecting to Url")

        val response = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requestActive = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) {
                        if (response.code == 404) accountDoesntExist = true
                        //error = true
                        requestActive = false
                        throw IOException("Unexpected code $response")
                    }
                    else {

                        val str_json = response.body?.string()

                        val obj = JSONObject(str_json)

                        account = createAccountFromJSON(obj.getJSONObject("content"))


                        requestActive = false
                    }
                }
            }
        })
    }

    /**
     * Fonction qui exécute POST/account/refresh_token/
     *
     * @param[data] information de rafraichissement de token
     */
    fun POSTaccountRefreshToken() {
        requestActive = true

        prevToken = token
        token = null

        val data = prevToken?.let { RefreshTokenInput(it.refresh_token) }

        val data_json = data?.let { createJSONFromRefreshTokenInput(it) }

        val formBody: RequestBody =
            data_json.toString().toRequestBody("application/json".toMediaTypeOrNull())


        val request = Request.Builder()
            .url("https://kungry.infomobile.app/api/v1/account/refresh_token")
            .post(formBody)
            .build()
        println("Connecting to Url")

        val response = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requestActive = false
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) {
                        error = true
                        requestActive = false
                        token = prevToken
                        throw IOException("Unexpected code $response")
                    }

                    val str_json = response.body?.string()

                    val obj = JSONObject(str_json)

                    println("TOKEN REFRESHED")
                    token = createTokenOutputFromJSON(obj.getJSONObject("content"))

                    requestActive = false
                }
            }
        })
    }

    // fonctions qui retournent le résultat d'une requête
    fun createAccount(data : CreateAccountCreate) : TokenOutput? {
        POSTaccount(data)
        while (requestActive) {println("waiting /account")}
        return token
    }

    fun loginAccount(data: AccountLogin) : TokenOutput? {
        POSTaccountLogin(data)
        while (requestActive) {println("waiting /account/login")}
        return token
    }

    fun getMe() : Account? {
        GETaccountMe()
        while (requestActive) {println("waiting /account/me")}
        return account
    }

    fun refreshToken() : TokenOutput? {
        POSTaccountRefreshToken()
        while (requestActive) {println("waiting /account/refresh_token")}
        if (error) {
            println("REFRESH_TOKEN FAILED")
            error = false
        }
        return token
    }

    fun disconnect() {
        token = null
        prevToken = null
        connected = false
    }


    // fonctions de création d'objets à partir de JSON
    private fun createTokenOutputFromJSON(obj : JSONObject) : TokenOutput {
        val access_token = obj.getString("access_token")
        val token_type = obj.getString("token_type")
        val refresh_token = obj.getString("refresh_token")
        val scope = obj.getString("scope")
        val expires_in = obj.getInt("expires_in")

        return TokenOutput(access_token,token_type,refresh_token,scope,expires_in)
    }

    private fun createAccountFromJSON(obj : JSONObject) : Account {
        val total_review_count = obj.getInt("total_review_count")
        val last_name = obj.getString("last_name")
        val first_name = obj.getString("first_name")
        val email = obj.getString("email")
        val created = obj.getString("email")
        val updated = obj.getString("updated")
        val user = obj.getInt("user")

        return Account(total_review_count, last_name, first_name, email, created, updated, user)
    }


    private fun createCreatorFromJSON(obj : JSONObject) : Creator {
        val first_name = obj.getString("first_name")
        val last_name = obj.getString("last_name")

        return Creator(first_name, last_name)
    }

    //    // création de JSON à partir des valeurs des data
    private fun createJSONFromCreateAccountCreate(data: CreateAccountCreate) : JSONObject {
        return JSONObject()
            .put("client_id", data.client_id)
            .put("client_secret", data.client_secret)
            .put("first_name", data.first_name)
            .put("last_name", data.last_name)
            .put("email", data.email)
            .put("password", data.password)
    }

    private fun createJSONFromAccountLogin(data : AccountLogin) : JSONObject {
        return JSONObject()
            .put("client_id", data.client_id)
            .put("client_secret", data.client_secret)
            .put("email", data.email)
            .put("password", data.password)
    }

    private fun createJSONFromRefreshTokenInput(data : RefreshTokenInput) : JSONObject {
        return JSONObject()
            .put("refresh_token", data.refresh_token)
            .put("client_id", data.client_id)
            .put("client_secret", data.client_secret)
    }



//
}

//   fun POSTaccount(data: CreateAccountCreate) {
//        requestActive = true
//
//        //prevToken = token
//        token = null
//
//        val data_json = createJSONFromCreateAccountCreate(data)
//
//        val formBody: RequestBody =
//            data_json.toString().toRequestBody("application/json".toMediaTypeOrNull())
//
//        val url = HttpUrl.Builder()
////            .scheme(scheme)
//            .host(host)
////            .addPathSegment("api")
////            .addPathSegment("v1")
////            .addPathSegment("account")
//            .build()
//
//        println(url)
//
//        val request = Request.Builder()
//            .url("https://kungry.infomobile.app/api/v1/account/")
//            .post(formBody)
//            .build()
//
//        val response = client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                requestActive = false
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
//
//                    if (!response.isSuccessful) {
//                        println("Not connected")
//                        if (response.code == 409) {
//                            accountAlreadyExists = true
//                            //prevToken
//                        }
//                        //error = true
//                        requestActive = false
//                        throw IOException("Unexpected code $response")
//                    }
//                    println("Connected")
//                    val str_json = response.body?.string()
//
//                    val obj = JSONObject(str_json)
//
//                    token = createTokenOutputFromJSON(obj.getJSONObject("content"))
//
//                    connected = true
//                    requestActive = false
//                }
//            }
//        })
//    }
//




