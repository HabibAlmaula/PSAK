package com.fkammediacenter.psak.fragments.donasi


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fkammediacenter.psak.R
import com.github.clans.fab.FloatingActionMenu
import kotlinx.android.synthetic.main.fragment_donasi.*
import java.util.ArrayList

class DonasiFragment : Fragment() {

    private val menus = ArrayList<FloatingActionMenu>()
    private val mUiHandler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_add_donasi.setClosedOnTouchOutside(true)
        fab_add_donasi.hideMenuButton(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        menus.add(fab_add_donasi)

        var delay = 400
        for (menu in menus) {
            mUiHandler.postDelayed({ menu.showMenuButton(true) }, delay.toLong())
            delay += 150
        }

    }


}
