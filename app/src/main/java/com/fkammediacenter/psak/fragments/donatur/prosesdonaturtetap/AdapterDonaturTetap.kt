package com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fkammediacenter.psak.R
import com.fkammediacenter.psak.daoresponse.DonaturTetaplist
import com.fkammediacenter.psak.utils.State
import kotlinx.android.synthetic.main.footer_loader.view.*
import kotlinx.android.synthetic.main.item_donatur_tetap.view.*

class AdapterDonaturTetap(private val retry: () -> Unit)
    : PagedListAdapter<DonaturTetaplist, RecyclerView.ViewHolder>(DonasiDiffCallback){

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) DonaturTetapViewHolder.create(
            parent
        ) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as DonaturTetapViewHolder).bind(getItem(position))
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
        val DonasiDiffCallback = object : DiffUtil.ItemCallback<DonaturTetaplist>() {
            override fun areItemsTheSame(oldItem: DonaturTetaplist, newItem: DonaturTetaplist): Boolean {
                return oldItem.idDonatur == newItem.idDonatur
            }

            override fun areContentsTheSame(oldItem: DonaturTetaplist, newItem: DonaturTetaplist): Boolean {
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

class DonaturTetapViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
    fun bind (donatur: DonaturTetaplist?){
        if (donatur != null){
            itemView.tv_nama_donatur.text = donatur.namaDonatur
            itemView.tv_alamat_donatur.text = donatur.alamatRumah

        }

    }

    companion object {
        fun create(parent: ViewGroup): DonaturTetapViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_donatur_tetap, parent, false)
            return DonaturTetapViewHolder(view)
        }
    }

}
