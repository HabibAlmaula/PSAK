package com.fkammediacenter.psak.activity.donaturkotak

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.daoresponse.DonaturKotakList
import com.fkammediacenter.psak.utils.api.BaseApiService
import com.fkammediacenter.psak.utils.api.UtilsApi
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_update_donatur_kotak.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class UpdateDonaturKotakActivity : AppCompatActivity() {
    private lateinit var donaturList : DonaturKotakList
    private lateinit var baseApiService: BaseApiService
    var latitude : Double = 0.0
    var longitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_donatur_kotak)

        baseApiService = UtilsApi.getAPIService()

        donaturList = intent.getParcelableExtra("DONATUR")
        latitude = donaturList.latitude.toDouble()
        longitude = donaturList.longitude.toDouble()

        populateEdittext(donaturList)

        btn_updateDonatur.setOnClickListener {
            Update(donaturList)
        }
        btn_addlokasi.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            try {
                val intent = builder.build(this)
                startActivityForResult(intent,
                    PLACE_PICKER_REQUEST
                )
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val place = PlacePicker.getPlace(this, data)
                 latitude = place.latLng.latitude
                 longitude = place.latLng.longitude

                tv_alertLokasi.visibility = View.GONE

            }
        }
    }

    private fun Update(donatur : DonaturKotakList) {
        val id_donatur = donatur.idDonatur
        val nama_pemilik = et_Namapemilik.text.toString()
        val nama_outlet = et_NamaOultet.text.toString()
        val id_fr = donatur.idFr
        val kode_akun = donatur.kodeAkun
        val type_kotak = donatur.typeKotak
        val kode_wilayah = donatur.kodeWilayah
        val jenis_usaha = et_Jenisusaha.text.toString()
        val email = donatur.email
        val jadwal_kunjugan = donatur.jadwalKunjungan
        val alamat_pemilik = et_alamatPemilik.text.toString()
        val alamat_outlet = et_alamatOutlet.text.toString()
        val no_hp = et_noHp.text.toString()
        val tgl_pasang = et_tglPasang.text.toString()
        val status = donatur.status
        val latitude = latitude
        val longitude = longitude

        val call = baseApiService.updateDonaturKotak(id_donatur,nama_pemilik,nama_outlet,id_fr,kode_akun,type_kotak,kode_wilayah,jenis_usaha,email,jadwal_kunjugan,alamat_pemilik,alamat_outlet,no_hp,tgl_pasang,status,latitude.toString(),longitude.toString())
        call.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ONFAILURE", t.toString())
                Toast.makeText(baseContext,t.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(baseContext,"Berhasil mengupdate data", Toast.LENGTH_SHORT).show()
                Log.d("ONSUCCES",response.toString() )
                val i = Intent(this@UpdateDonaturKotakActivity, DetailDonaturKotak::class.java)
                startActivity(i)
                finish()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, DetailDonaturKotak::class.java)
        startActivity(intent)
    }

    private fun populateEdittext(donatur : DonaturKotakList) {
        et_NamaOultet.setText(donatur.namaOutlet)
        et_Namapemilik.setText(donatur.namaPemilik)
        et_Jenisusaha.setText(donatur.jenisUsaha)
        et_alamatOutlet.setText(donatur.alamatOutlet)
        et_alamatPemilik.setText(donatur.alamatPemilik)
        et_noHp.setText(donatur.noHp)
        et_tglPasang.setText(donatur.tglPasangKotak)

        if (latitude != 0.0){
            tv_alertLokasi.visibility = View.GONE
            btn_addlokasi.text = "Update Lokasi"
        }

    }

    companion object {

        //    val semuamatkulItems = response.body()!!.getSemuamatkul()


        private val PLACE_PICKER_REQUEST = 1
        private val TAG = "Donatur"
    }

}
