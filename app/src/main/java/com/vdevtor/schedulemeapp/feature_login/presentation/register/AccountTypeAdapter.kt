package com.vdevtor.schedulemeapp.feature_login.presentation.register

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.vdevtor.schedulemeapp.databinding.AccountTypeItemBinding
import com.vdevtor.schedulemeapp.feature_login.domain.model.AccountTypeModel

class AccountTypeAdapter(context: Context, resource: Int, objects: MutableList<AccountTypeModel>) :
    ArrayAdapter<AccountTypeModel>(context, resource, objects) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = AccountTypeItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        val type : AccountTypeModel? = getItem(position)
        binding.tvAccountType.text = type?.type
        binding.image.background = type?.drawable

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = AccountTypeItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        val type : AccountTypeModel? = getItem(position)
        binding.tvAccountType.text = type?.type
        binding.image.background = type?.drawable

        return binding.root
    }
}