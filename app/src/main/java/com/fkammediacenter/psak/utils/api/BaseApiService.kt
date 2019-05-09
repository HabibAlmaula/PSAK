package com.fkammediacenter.psak.utils.api

import com.fkammediacenter.psak.daoresponse.DonasiResponse
import com.fkammediacenter.psak.daoresponse.DonaturKotakResponse
import com.fkammediacenter.psak.daoresponse.DonaturTetapResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BaseApiService {
    // Fungsi ini untuk memanggil API http://192.168.43.100/psak/login.php
    @FormUrlEncoded
    @POST("login.php")
    fun loginRequest(
        @Field("idfr") idfr: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @GET("alldonasi")
    fun getAllDonasi(@Query("page") page : Int, @Query("pagesize") pagesize : Int) : Single<DonasiResponse>

    @GET("listdonatur/{donatur}/{id_fr}")
    fun getAllDonaturKotak(@Path("donatur") donatur : String,@Path("id_fr") id_fr : String,@Query("page") page : Int, @Query("pagesize") pagesize : Int) : Single<DonaturKotakResponse>

    @GET("listdonatur/{donatur}/{id_fr}")
    fun getAllDonaturTetap(@Path("donatur") donatur : String,@Path("id_fr") id_fr : String,@Query("page") page : Int, @Query("pagesize") pagesize : Int) : Single<DonaturTetapResponse>

    @GET("donaturkotak/{id_donatur}")
    fun getDataDonaturKotak(@Path("id_donatur") id_donatur: String): Call<DonaturKotakResponse>

    @FormUrlEncoded
    @PUT("updatedonaturrkotak")
    fun updateDonaturKotak(
        @Field("id_donatur")id_donatur : String,
        @Field("nama_pemilik")nama_pemilik : String,
        @Field("nama_outlet")nama_outlet:String,
        @Field("id_fr")id_fr:String,
        @Field("kode_akun")kode_akun:String,
        @Field("type_kotak")type_kotak:String,
        @Field("kode_wilayah")kode_wilayah:String,
        @Field("jenis_usaha")jenis_usaha:String,
        @Field("email")email:String,
        @Field("jadwal_kunjungan")jadwal_kunjungan:String,
        @Field("alamat_pemilik")alamat_pemilik:String,
        @Field("alamat_outlet")alamat_outlet:String,
        @Field("no_hp")no_hp:String,
        @Field("tgl_pasang_kotak")tgl_pasang_kotak:String,
        @Field("status")status:String,
        @Field("latitude")latitude:String,
        @Field("longitude")longitude:String
    ):Call<ResponseBody>

    @GET("caridonatur/{donatur}/{id_fr}/{nama}")
    fun searchDonaturKotak(@Path("donatur")donatur: String,@Path("id_fr") id_fr: String, @Path("nama") nama : String,@Query("page") page : Int, @Query("pagesize") pagesize : Int) : Single<DonaturKotakResponse>

    @GET("caridonatur/{donatur}/{id_fr}/{nama}")
    fun searchDonaturTetap(@Path("donatur")donatur: String,@Path("id_fr") id_fr: String, @Path("nama") nama : String,@Query("page") page : Int, @Query("pagesize") pagesize : Int) : Single<DonaturTetapResponse>

}
