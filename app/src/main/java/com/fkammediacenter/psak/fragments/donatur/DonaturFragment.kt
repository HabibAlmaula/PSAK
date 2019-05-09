package com.fkammediacenter.psak.fragments.donatur


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.activity.donaturkotak.BarcodeScannerActivity
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak.AdapterDonaturKotak
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak.DKVMFactory
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak.DonaturKotakViewModel
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap.AdapterDonaturTetap
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap.DTVMFactory
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap.DonaturTetapViewModel
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.fkammediacenter.psak.utils.State
import com.github.clans.fab.FloatingActionMenu
import kotlinx.android.synthetic.main.fragment_donatur.*


class DonaturFragment : Fragment() {
    private val menus = ArrayList<FloatingActionMenu>()
    private val mUiHandler = Handler()
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var donaturKotakViewModel: DonaturKotakViewModel
    lateinit var donaturTetapViewModel: DonaturTetapViewModel
    lateinit var adapterDonaturKotak: AdapterDonaturKotak
    lateinit var adapterDonaturTetap: AdapterDonaturTetap
    lateinit var searchView: SearchView
    var cari = ""

    val items = arrayOf("Donatur Kotak", "Donatur Tetap")
    var person = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donatur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.title = "Panel Donatur"

        setHasOptionsMenu(true)

        sharedPrefManager = SharedPrefManager(requireContext())
        sharedPrefManager.deleteSpString(SharedPrefManager.SP_IDDONATURKotak)

        fab_add_donatur.setClosedOnTouchOutside(true)
        fab_add_donatur.hideMenuButton(false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        menus.add(fab_add_donatur)
        spinnerdonatur.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
        spinnerdonatur.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = spinnerdonatur.selectedItem.toString()
                initialViewModel(selected,cari)
                initAdapter(selected,cari)
                initState(selected)
            }

        }

        searchView = sv_donatur.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                donaturKotakViewModel.clearDataSource()
                if (query== ""){
                    initialViewModel(person,cari)
                    initAdapter(person,cari)
                    initState(person)
                }else
                initialViewModel(person,query.toString())
                initAdapter(person,query.toString())
                initState(person)


                return false
            }
                override fun onQueryTextChange(newText: String?): Boolean {
                    val yu = newText


                    return true
                }
        })
        var delay = 400
        for (menu in menus) {
            mUiHandler.postDelayed({ menu.showMenuButton(true) }, delay.toLong())
            delay += 150
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_searchv_view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            sv_donatur.show()

            return true
        }
        if (item.itemId == R.id.action_scan_barcode){
            val intent = Intent(activity, BarcodeScannerActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onResume() {
        super.onResume()
        Log.d("ONRESUME","resuming fragment")
        Toast.makeText(requireContext(),"ONRESUME",Toast.LENGTH_SHORT).show()

        if (person == "Donatur Kotak"){
            donaturKotakViewModel = ViewModelProviders.of(requireActivity(),
                DKVMFactory(person, sharedPrefManager.getSPIdFr()!!, cari))
                .get(DonaturKotakViewModel::class.java)

            donaturKotakViewModel.reload()
        }
        if (person == "Donatur Tetap") {
            donaturTetapViewModel = ViewModelProviders.of(requireActivity(),
                DTVMFactory(person, sharedPrefManager.getSPIdFr()!!))
                .get(DonaturTetapViewModel::class.java)
            donaturTetapViewModel.reload()
        }



    }

    fun initialViewModel(string: String, cari : String){
        when(string){
            "Donatur Kotak"->{
                person = "Donatur Kotak"
                donaturKotakViewModel = ViewModelProviders.of(requireActivity(),
                    DKVMFactory(string, sharedPrefManager.getSPIdFr()!!, cari)
                )
                    .get(DonaturKotakViewModel::class.java)

            }
            "Donatur Tetap"->{
                person = "Donatur Tetap"
                donaturTetapViewModel = ViewModelProviders.of(requireActivity(),
                    sharedPrefManager.getSPIdFr()?.let { DTVMFactory(string, it) })
                    .get(DonaturTetapViewModel::class.java)

            }
        }

    }


    @SuppressLint("WrongConstant")
    private fun initAdapter(donatur : String, cari: String){
        when(donatur){
            "Donatur Kotak" -> {
                if (cari.isEmpty()||cari == ""){
                    adapterDonaturKotak = AdapterDonaturKotak(requireContext()) { donaturKotakViewModel.retry() }
                    Log.d("ADAPTER","WITHOUT CARI")
                }
                if (cari.isNotEmpty()|| cari !=""){
                    adapterDonaturKotak = AdapterDonaturKotak(requireContext()) { donaturKotakViewModel.reload() }
                    Log.d("ADAPTER","WITH CARI")
                }
               // adapterDonaturKotak = AdapterDonaturKotak(requireContext()) { donaturKotakViewModel.retry() }
                adapterDonaturKotak.notifyDataSetChanged()
                rv_alldonatur.layoutManager= LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rv_alldonatur.adapter = adapterDonaturKotak
                donaturKotakViewModel.donaturList.observe(this, Observer {donaturKotakViewModel.clearDataSource(); adapterDonaturKotak.submitList(it) })
            }

            "Donatur Tetap" ->{
                adapterDonaturTetap = AdapterDonaturTetap { donaturTetapViewModel.retry() }
                adapterDonaturTetap.notifyDataSetChanged()
                rv_alldonatur.layoutManager= LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rv_alldonatur.adapter = adapterDonaturTetap
                donaturTetapViewModel.donaturList.observe(this, Observer { adapterDonaturTetap.submitList(it) })
            }

            else ->{ Toast.makeText(requireContext(),"Not Found", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun initState(donatur: String){
        tv_errorloaddonatur.setOnClickListener {
            if (donatur=="Donatur Kotak"){
                donaturKotakViewModel.retry()
            }
            if (donatur=="Donatur Tetap"){
                donaturTetapViewModel.retry()
            }
        }
        if (donatur=="Donatur Kotak"){
            donaturKotakViewModel.getState().observe(this, Observer { state ->
                progress_listDonatur.visibility = if (donaturKotakViewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
                tv_errorloaddonatur.visibility = if (donaturKotakViewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
                if (!donaturKotakViewModel.listIsEmpty()) {
                    adapterDonaturKotak.setState(state ?: State.DONE)
                } })
        }

        if (donatur=="Donatur Tetap"){
            donaturTetapViewModel.getState().observe(this, Observer { state ->
                progress_listDonatur.visibility = if (donaturTetapViewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
                tv_errorloaddonatur .visibility = if (donaturTetapViewModel.listIsEmpty() && state == State.ERROR ) View.VISIBLE else View.GONE
                if (!donaturTetapViewModel.listIsEmpty()) {
                    adapterDonaturTetap.setState(state ?: State.DONE)
                } })
        }

    }


}
