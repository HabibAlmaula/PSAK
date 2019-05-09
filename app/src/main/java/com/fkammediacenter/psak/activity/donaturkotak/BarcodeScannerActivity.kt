package com.fkammediacenter.psak.activity.donaturkotak

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_barcode_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


class BarcodeScannerActivity :  AppCompatActivity(), ZXingScannerView.ResultHandler  {

    private val FLASH_STATE = "FLASH_STATE"
    lateinit var sharedPrefManager: SharedPrefManager


    private var mScannerView: ZXingScannerView? = null
    private var mFlash: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)
        sharedPrefManager = SharedPrefManager(this)

        mScannerView = ZXingScannerView(this)
        content_frame.addView(mScannerView)
    }


    override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        // You can optionally set aspect ratio tolerance level
        // that is used in calculating the optimal Camera preview size
        mScannerView?.setAspectTolerance(0.2f)
        mScannerView?.startCamera()
        mScannerView?.setFlash(mFlash)
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FLASH_STATE, mFlash)
    }

    override fun handleResult(rawResult: Result) {
        sharedPrefManager.saveSPString(SharedPrefManager.SP_IDDONATURKotak,rawResult.text)
        val intent = Intent(this, DetailDonaturKotak::class.java)
        startActivity(intent)
        finish()


      /*  Toast.makeText(
            this, "Contents = " + rawResult.text +
                    ", Format = " + rawResult.barcodeFormat.toString(), Toast.LENGTH_SHORT
        ).show()

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        val handler = Handler()
        handler.postDelayed({ mScannerView?.resumeCameraPreview(this@BarcodeScannerActivity) }, 2000)*/
    }

    fun toggleFlash(v: View) {
        mFlash = !mFlash
        mScannerView?.setFlash(mFlash)
    }

}

