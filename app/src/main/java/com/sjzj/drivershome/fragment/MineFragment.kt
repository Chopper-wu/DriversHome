package com.sjzj.drivershome.fragment

import android.os.Bundle
import android.view.View
import com.blue.corelib.base.BaseDataBindingFragment
import com.sjzj.drivershome.R
import com.sjzj.drivershome.databinding.FragmentMineBinding

class MineFragment: BaseDataBindingFragment<FragmentMineBinding>() {
    override fun getLayoutId(): Int {
         return R.layout.fragment_mine
    }

    override fun onViewCreatedCalled(view: View, savedInstanceState: Bundle?) {
    }
}