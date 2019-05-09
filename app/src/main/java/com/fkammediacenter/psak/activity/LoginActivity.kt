package com.fkammediacenter.psak.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.fkammediacenter.psak.utils.api.BaseApiService
import com.fkammediacenter.psak.utils.api.UtilsApi
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var mApiService: BaseApiService
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var loading: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mApiService = UtilsApi.getAPIService() // meng-init yang ada di package apihelper

        sharedPrefManager = SharedPrefManager(this)
        loading = ProgressDialog(this)

        // Code berikut berfungsi untuk mengecek session, Jika session true ( sudah login )
        // maka langsung memulai MainActivity.

        if (sharedPrefManager.getSPSudahLogin()!!){
            startActivity(
                Intent(this@LoginActivity, SplashWelcome::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()

        }

        btn_login.setOnClickListener {
            requestLogin()
        }


    }


    private fun requestLogin() {
        loading.setMessage("tunggu sebentar...")
        loading.show()

        val call = mApiService.loginRequest(et_idfr.text.toString(), et_password.text.toString())
        call.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("debug", "onFailure: ERROR > $t")
                loading.dismiss()

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    loading.dismiss()

                    try {
                        val jsonRESULTS = JSONObject(response.body()!!.string())
                        if (jsonRESULTS.getString("error").equals("false")){
                            val nama = jsonRESULTS.getJSONObject("user").getString("nama_lengkap_fr")
                            val idfr = jsonRESULTS.getJSONObject("user").getString("id_fr")

                            // Jika login berhasil maka data nama yang ada di response API
                            // akan diparsing ke activity selanjutnya.

                            Toast.makeText(this@LoginActivity,"Selamat datang " + nama, Toast.LENGTH_SHORT).show()

                            sharedPrefManager.saveSPString(SharedPrefManager.SP_IDFR, idfr)
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama)
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true)

                            startActivity(
                                Intent(this@LoginActivity, SplashWelcome::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        }else{
                            val error_message = jsonRESULTS.getString("error_msg")
                            Toast.makeText(this@LoginActivity, error_message, Toast.LENGTH_SHORT).show()
                        }

                    }catch (e : JSONException){
                        e.printStackTrace()
                    }catch (e :IOException){
                        e.printStackTrace()
                    }

                }else{
                    loading.dismiss()
                }

            }


        })

    }

}
