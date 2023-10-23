package com.blue.corelib.base

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blue.corelib.extensions.getGenericClass
import com.blue.corelib.utils.Logger
import java.lang.reflect.ParameterizedType


/**
 * activity
 */
abstract class BaseDataBindingActivity<Binding : ViewDataBinding> : BaseActivity() {
    companion object {
        const val FORGET_TOKEN = "forget_token"
        const val SMS_VERIFY_BODY = "SMS_VERIFY_BODY"
    }

    protected lateinit var binding: Binding

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.e("onCreate", javaClass.name)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        offsetView()
        onCreateCalled(savedInstanceState)
    }

    //超大号字体处理
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun beforeSetContentView() {
    }

    abstract fun onCreateCalled(savedInstanceState: Bundle?)

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    open fun offsetView() {
        if (isImmerse() && autoOffsetView()) {
            offsetStatusBar(binding.root)
        }
    }

}

abstract class BaseMvvmActivity<Binding : ViewDataBinding, VM : BaseViewModel> :
    BaseDataBindingActivity<Binding>() {

    protected lateinit var viewModel: VM

    override fun onCreateCalled(savedInstanceState: Bundle?) {

        //获得泛型参数的实际类型
        val vmClass =
            (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        //viewModel = ViewModelProvider(this).get(vmClass)
        //viewModel =  ViewModelProviders.of(this).get(vmClass)
        viewModel = ViewModelProvider(this).get(getGenericClass(1))
        viewModel.lifecycleOwner = this
    }

}

/**
 * fragment
 */
abstract class BaseDataBindingFragment<Binding : ViewDataBinding> : BaseFragment() {
    protected lateinit var binding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedCalled(view, savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun onViewCreatedCalled(view: View, savedInstanceState: Bundle?)

}

abstract class BaseMvvmFragment<Binding : ViewDataBinding, VM : BaseViewModel> :
    BaseDataBindingFragment<Binding>() {

    protected lateinit var viewModel: VM

    override fun onViewCreatedCalled(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(getGenericClass(1))
        viewModel.lifecycleOwner = this
    }

}

/**
 * dialog fragment
 */
abstract class BaseDataBindingDialogFragment<Binding : ViewDataBinding> : BaseDialogFragment() {

    protected lateinit var binding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        offsetView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedCalled(view, savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun onViewCreatedCalled(view: View, savedInstanceState: Bundle?)

    open fun offsetView() {
        if (isImmerse() && autoOffsetView()) {
            offsetStatusBar(binding.root)
        }
    }

}

abstract class BaseMvvmDialogFragment<Binding : ViewDataBinding, VM : BaseViewModel> :
    BaseDataBindingDialogFragment<Binding>() {

    protected lateinit var viewModel: VM

    override fun onViewCreatedCalled(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(getGenericClass(1))
        viewModel.lifecycleOwner = this
    }

}