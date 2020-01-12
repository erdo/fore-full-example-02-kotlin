package foo.bar.example.fore.fullapp02.api.fruits

import retrofit2.Response
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
    suspend fun getFruits(@Query("mocky-delay") delayScalaDurationFormat: String): Response<List<FruitPojo>>
}
