package com.vdevtor.common.core

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<out T>(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list : MutableList<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolderBase(parent,viewType)
    }

    abstract fun onCreateViewHolderBase(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolderBase(holder, position)
    }
    abstract fun onBindViewHolderBase(holder: RecyclerView.ViewHolder?, position: Int)

    override fun getItemCount() = list.size

    fun getDataList(): MutableList<Any> {
        return list
    }

}