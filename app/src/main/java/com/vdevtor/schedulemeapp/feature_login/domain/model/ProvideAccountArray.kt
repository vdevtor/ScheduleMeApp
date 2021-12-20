package com.vdevtor.schedulemeapp.feature_login.domain.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.vdevtor.schedulemeapp.R

class ProvideAccountArray(private val context: Context) {
    operator fun invoke(): MutableList<AccountTypeModel>{
        return mutableListOf(
            AccountTypeModel(context.getString(R.string.choose_your_account_type)),
            AccountTypeModel(context.getString(R.string.personal),ContextCompat.getDrawable(context,R.drawable.ic_person)),
            AccountTypeModel(context.getString(R.string.business),ContextCompat.getDrawable(context,R.drawable.ic_business))
        )
    }
}