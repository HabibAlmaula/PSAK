package com.fkammediacenter.psak.fragments.kinerja


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.activity.LoginActivity
import com.fkammediacenter.psak.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_kinerja.*


class KinerjaFragment : Fragment() {

    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kinerja, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPrefManager = SharedPrefManager(requireContext())
        tv_user_fr.text = sharedPrefManager.getSPNama()

        btn_logout_kinerja.setOnClickListener {

            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false)
            startActivity(
                Intent(activity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )

            activity?.finish()

        }
    }

}
