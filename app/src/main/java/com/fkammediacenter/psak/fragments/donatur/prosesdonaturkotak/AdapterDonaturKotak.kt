package com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.activity.donaturkotak.DetailDonaturKotak
import com.fkammediacenter.psak.daoresponse.DonaturKotakList
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.fkammediacenter.psak.utils.State
import kotlinx.android.synthetic.main.footer_loader.view.*
import kotlinx.android.synthetic.main.item_donatur_kotak.view.*
import org.jetbrains.anko.startActivity

class AdapterDonaturKotak(context: Context, private val retry: () -> Unit)
    : PagedListAdapter<DonaturKotakList, RecyclerView.ViewHolder>(DonasiDiffCallback){
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    private var sharedPrefManager: SharedPrefManager = SharedPrefManager(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) DonaturKotakViewHolder.create(
            parent
        ) else ListFooterViewHolder.create(
            retry,
            parent
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as DonaturKotakViewHolder).bind(getItem(position))
        else(holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val DonasiDiffCallback = object : DiffUtil.ItemCallback<DonaturKotakList>() {
            override fun areItemsTheSame(oldItem: DonaturKotakList, newItem: DonaturKotakList): Boolean {
                return oldItem.idDonatur == newItem.idDonatur
            }

            override fun areContentsTheSame(oldItem: DonaturKotakList, newItem: DonaturKotakList): Boolean {
                return oldItem == newItem
            }
        }
    }




}

class ListFooterViewHolder( view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status : State?){
        itemView.progress_bar.visibility = if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == State.ERROR) View.VISIBLE else View.INVISIBLE

    }

    companion object {
        fun create (retry: () -> Unit, parent: ViewGroup) : ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_loader, parent, false)
            view.txt_error.setOnClickListener { retry () }
            return ListFooterViewHolder(view)
        }
    }

}

class DonaturKotakViewHolder(context: Context,itemview: View) : RecyclerView.ViewHolder(itemview) {
    private var sharedPrefManager: SharedPrefManager = SharedPrefManager(context)
    fun bind (donatur: DonaturKotakList?){

        if (donatur != null){
            itemView.tv_nama_toko.text = donatur.namaOutlet
            itemView.tv_nama_pemilik.text = donatur.namaPemilik
            itemView.tv_alamat.text= donatur.alamatOutlet

            itemView.setOnClickListener {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_IDDONATURKotak,donatur.idDonatur)
                itemView.context.startActivity<DetailDonaturKotak>()

            }
        }

    }

    companion object {
        fun create(parent: ViewGroup): DonaturKotakViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_donatur_kotak, parent, false)
            return DonaturKotakViewHolder(parent.context,view)
        }
    }

}
