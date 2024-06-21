import com.example.rightspellv01.data.pref.LoginModel
import com.example.rightspellv01.data.pref.RegisterModel
import com.example.rightspellv01.data.response.AuthResponse
import com.example.rightspellv01.data.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    //User Signup
    @POST("register")
    fun userRegister(
        @Body user: RegisterModel
    ): Call<String>

    //User Login
    @POST("login")
    fun userLogin(
        @Body user: LoginModel
    ): Call<LoginResponse>

}