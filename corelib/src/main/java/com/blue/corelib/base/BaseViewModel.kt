package com.blue.corelib.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner
}