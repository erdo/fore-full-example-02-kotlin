package foo.bar.example.fore.fullapp02.api.fruits

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * These stubs are hosted at https://www.mocky.io/
 *
 * http://www.mocky.io/v2/5a491b03310000a308a82149
 *
 */
interface FruitService {

    @GET("5a491b03310000a308a82149/")
    fun getFruits(@Query("mocky-delay") delayScalaDurationFormat: String): Call<List<FruitPojo>>
}
