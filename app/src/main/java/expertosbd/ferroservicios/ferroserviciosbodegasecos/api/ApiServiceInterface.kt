package expertosbd.ferroservicios.ferroserviciosbodegasecos.api

import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecos.utils.BASE_URL
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceInterface {

    @GET("version")
    fun version(): Single<ApexApiResponse<Version>>

    @GET("login/{user}")
    fun login(@Path("user") user: String): Observable<ApexApiResponse<Login>>

    @GET("eventos")
    fun eventos(): Single<ApexApiResponse<Evento>>

    @POST("iniciomaniobra")
    fun inicioManiobra(@Body inicioManiobra: InicioManiobra): Single<PostSuccesfulResponse>

    @POST("finmaniobra")
    fun finManiobra(@Body finManiobra: FinManiobra): Single<PostSuccesfulResponse>

    @POST("eventos/nuevo")
    fun nuevoEvento(@Body nuevoEvento: NuevoEvento): Single<PostSuccesfulResponse>

    companion object Factory {
        fun create(): ApiServiceInterface {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .build()

            return retrofit.create(ApiServiceInterface::class.java)
        }
    }
}