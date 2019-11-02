package foo.bar.example.fore.fullapp02.api.authentication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * These stubs are hosted at https://www.mocky.io/
 *
 * http://www.mocky.io/v2/5a48e531310000aa06a8211d
 * http://www.mocky.io/v2/5a48e611310000a106a82120
 *
 */
interface AuthenticationService {

    @POST("5a48e531310000aa06a8211d/")
    fun getSessionToken(
        @Body sessionRequestPojo: SessionRequestPojo,
        @Query("mocky-delay") delayScalaDurationFormat: String
    ): Call<SessionResponsePojo>

    @POST("5a48e611310000a106a82120/")
    fun invalidateSessionToken(@Query("mocky-delay") delayScalaDurationFormat: String): Call<Void>

}
