package com.fkammediacenter.psak.activity.donaturkotak

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.activity.MapsActivity
import com.fkammediacenter.psak.daoresponse.DonaturKotakList
import com.fkammediacenter.psak.daoresponse.DonaturKotakResponse
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.fkammediacenter.psak.utils.api.BaseApiService
import com.fkammediacenter.psak.utils.api.UtilsApi
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_detail_donatur_kotak.*
import retrofit2.Call
import retrofit2.Response


class DetailDonaturKotak : AppCompatActivity() {

    private lateinit var donaturList : DonaturKotakList
    private lateinit var baseApiService: BaseApiService
    lateinit var sharedPrefManager: SharedPrefManager


    internal var dataDonaturKotak: List<DonaturKotakList>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_donatur_kotak)
//        donaturList = intent.getParcelableExtra("DONATUR")
        baseApiService = UtilsApi.getAPIService()

        sharedPrefManager = SharedPrefManager(this)


        loadItem(sharedPrefManager.getSPIdDonaturKotak()!!)
        //populateItem(donaturList)

        btn_updateDonatur.setOnClickListener {
            val intent = Intent(this, UpdateDonaturKotakActivity::class.java)
            intent.putExtra("DONATUR", dataDonaturKotak?.get(0))
            startActivity(intent)
            finish()
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

        btn_lokasi.setOnClickListener {
            if (dataDonaturKotak!![0].latitude=="0.000000"){
                Toast.makeText(this,"Lokasi Belum Ditambahkan",Toast.LENGTH_SHORT).show()

            }else{
                val intent = Intent(this, MapsActivity::class.java)
                val extras = Bundle()
                extras.putString("Latitude", dataDonaturKotak!![0].latitude)
                extras.putString("Longitude", dataDonaturKotak!![0].longitude)
                extras.putString("Outlet", dataDonaturKotak!![0].namaOutlet)
                intent.putExtras(extras)
                startActivity(intent)
            }
        }

    }

    private fun loadItem(idDonatur : String) {
        baseApiService.getDataDonaturKotak(idDonatur).enqueue(object : retrofit2.Callback<DonaturKotakResponse>{
            override fun onFailure(call: Call<DonaturKotakResponse>, t: Throwable) {
                Toast.makeText(baseContext, t.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DonaturKotakResponse>, response: Response<DonaturKotakResponse>) {
                val donatur = response.body()?.donaturlist
                donatur?.let { populateItem(it) }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val place = PlacePicker.getPlace(this, data)
                val latitude = place.latLng.latitude
                val longitude = place.latLng.longitude

            }
        }
    }

    private fun populateItem(donaturKotakList: List<DonaturKotakList>) {
        dataDonaturKotak = donaturKotakList

        tv_Iddonatur.text = dataDonaturKotak!![0].idDonatur
        tv_NamaOultet.text = dataDonaturKotak!![0].namaOutlet
        tv_Namapemilik.text = dataDonaturKotak!![0].namaPemilik
        tv_Jenisusaha.text = dataDonaturKotak!![0].jenisUsaha
        tv_alamatPemilik.text = dataDonaturKotak!![0].alamatPemilik
        tv_alamatOutlet.text = dataDonaturKotak!![0].alamatOutlet
        tv_noHp.text = dataDonaturKotak!![0].noHp
        tv_tglPasang.text = dataDonaturKotak!![0].tglPasangKotak

        if (dataDonaturKotak!![0].latitude=="0.000000"){
            btn_addlokasi.visibility = View.GONE
            btn_lokasi.visibility = View.VISIBLE
        }else{
            tv_alertLokasi.visibility = View.GONE
            btn_addlokasi.visibility = View.GONE
            btn_lokasi.visibility = View.VISIBLE
        }

    }

    companion object {

    //    val semuamatkulItems = response.body()!!.getSemuamatkul()


        private val PLACE_PICKER_REQUEST = 1
        private val TAG = "Donatur"
    }
}
