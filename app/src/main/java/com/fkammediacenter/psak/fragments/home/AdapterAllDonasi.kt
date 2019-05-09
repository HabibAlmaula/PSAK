package com.fkammediacenter.psak.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.daoresponse.Alldonasi
import com.fkammediacenter.psak.utils.State
import kotlinx.android.synthetic.main.footer_loader.view.*
import kotlinx.android.synthetic.main.item_donasi.view.*

class AdapterAllDonasi(private val retry: () -> Unit)
    : PagedListAdapter<Alldonasi, RecyclerView.ViewHolder>(DonasiDiffCallback){

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) BerandaViewHolder.create(
            parent
        ) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as BerandaViewHolder).bind(getItem(position))
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
        val DonasiDiffCallback = object : DiffUtil.ItemCallback<Alldonasi>() {
            override fun areItemsTheSame(oldItem: Alldonasi, newItem: Alldonasi): Boolean {
                return oldItem.idDonasi == newItem.idDonasi
            }

            override fun areContentsTheSame(oldItem: Alldonasi, newItem: Alldonasi): Boolean {
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

class BerandaViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
    fun bind (donasi: Alldonasi?){
        if (donasi != null){
            itemView.tv_id_donasi.text = donasi.idDonasi
            itemView.tv_id_donatur.text = donasi.idDonatur
            itemView.tv_tgl_donasi.text= donasi.waktuInput
            itemView.tv_jumlah_donasi.text = donasi.nominalDonasi


        }

    }

    companion object {
        fun create(parent: ViewGroup): BerandaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_donasi, parent, false)
            return BerandaViewHolder(view)
        }
    }

}
