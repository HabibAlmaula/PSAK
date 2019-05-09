package com.fkammediacenter.psak.fragments.home


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.activity.LoginActivity
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.fkammediacenter.psak.utils.State
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var donasiViewModel: DonasiViewModel
    lateinit var adapterAllDonasi: AdapterAllDonasi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPrefManager = SharedPrefManager(requireContext())

        donasiViewModel = activity?.let {
            ViewModelProviders.of(it)
                .get(DonasiViewModel::class.java)
        }!!


        initAdapter()
        initState()

        tv_login_frag.text = "Anda login sebagai :" + sharedPrefManager.getSPNama()
        tv_nama.text = sharedPrefManager.getSPNama()
        tv_id.text = "#"+sharedPrefManager.getSPIdFr()

        btn_logout_frag.setOnClickListener {
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false)
            startActivity(
                Intent(activity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )

            activity?.finish()

        }

    }

    @SuppressLint("WrongConstant")
    private fun initAdapter() {
        adapterAllDonasi = AdapterAllDonasi { donasiViewModel.retry() }
        rv_alldonasi.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        rv_alldonasi.adapter = adapterAllDonasi
        donasiViewModel.donasiList.observe(this, Observer { adapterAllDonasi.submitList(it) })

    }

    private fun initState(){
        tv_errorloaddonasi.setOnClickListener { donasiViewModel.retry() }
        donasiViewModel.getState().observe(this, Observer { state ->
            progress_listDonasi.visibility = if (donasiViewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            tv_errorloaddonasi.visibility = if (donasiViewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!donasiViewModel.listIsEmpty()) {
                adapterAllDonasi.setState(state ?: State.DONE)
            }
        })
    }


}
