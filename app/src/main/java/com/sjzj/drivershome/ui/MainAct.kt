package com.sjzj.drivershome.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blue.corelib.base.BaseDataBindingActivity
import com.blue.corelib.base.BaseMvvmActivity
import com.blue.corelib.http.SchedulersAndBodyTransformer
import com.blue.corelib.http.SchedulersAndBodyTransformerIncludeNull
import com.blue.corelib.utils.*
import com.blue.corelib.view.DownloadCircleDialog
import com.blue.xrouter.annotation.Router
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.newcw.component.manager.SystemManager
import com.newcw.component.utils.*
import com.orhanobut.dialogplus.DialogPlus
import com.sjzj.drivershome.R
import com.sjzj.drivershome.databinding.ActMainBinding
import com.sjzj.drivershome.event.CustomEvent
import com.sjzj.drivershome.fragment.MineFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@Router(DRIVER_HOME)
class MainAct : BaseDataBindingActivity<ActMainBinding>() {
    var captainInviteFlag: Boolean = true
    private var currentBackPressedTime: Long = 0
    private val BACK_PRESSED_INTERVAL = 5000
    var downLoadding = false  //是否需要下载
    var mainDriverHomeFragment = MineFragment()
    var mainDriverWayBillFragment = MineFragment()
    var goodsSourceFragment = MineFragment()
    var mineFragment = MineFragment()
    private val fragments: ArrayList<Fragment> by lazy {
        arrayListOf<Fragment>(
            mainDriverHomeFragment,
            goodsSourceFragment,
            mainDriverWayBillFragment,
            mineFragment
        )
    }
    private val dialogProgress by lazy {
        DownloadCircleDialog(
            this
        )
    }

    override fun statusBarDarkFont(): Boolean {
        return false
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainAct::class.java)
            context.startActivity(intent)
        }
    }

    val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    private var currentContentPanelIndex = -1
    override fun getLayoutId() = R.layout.act_main
    override fun autoOffsetView(): Boolean {
        return false
    }

    override fun onCreateCalled(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        //拉去城市列表
        //CityListUtils.getAllAreaList(this)
        if (savedInstanceState != null) {
            currentContentPanelIndex = savedInstanceState.getInt("mLastIndex", 0)
            if (supportFragmentManager.findFragmentByTag("mainDriverHomeFragment") != null) {
                mainDriverHomeFragment =
                    supportFragmentManager.findFragmentByTag("mainDriverHomeFragment") as MineFragment
            }
            if (supportFragmentManager.findFragmentByTag("goodsSourceFragment") != null) {
                goodsSourceFragment =
                    supportFragmentManager.findFragmentByTag("goodsSourceFragment") as MineFragment
            }
            if (supportFragmentManager.findFragmentByTag("mainDriverWayBillFragment") != null) {
                mainDriverWayBillFragment =
                    supportFragmentManager.findFragmentByTag("mainDriverWayBillFragment") as MineFragment
            }
            if (supportFragmentManager.findFragmentByTag("mineFragment") != null) {
                mineFragment =
                    supportFragmentManager.findFragmentByTag("mineFragment") as MineFragment
            }
        }
        refreshAuthorUi()
        //3天后刷新token
        if (ConfigManager.getLongValue("tokenTime") > 0 && DateUtil.daysOfTwo(
                ConfigManager.getLongValue(
                    "tokenTime"
                ).toString()
            ) <= -3
        ) {
            ConfigManager.setLongValue("tokenTime", System.currentTimeMillis())
            Handler().postDelayed({
                //刷新token
            }, 5000)
        }
        //viewModel.saveProtocolRecord("运费结算协议", "url", "1", null)
        //首次跳过，当天不弹，后面弹出2次，1天1次。
        /*if (ConfigManager.getIntValue("authInfoSum") in 1..2 && DateUtil.daysOfTwo(
                ConfigManager.getLongValue(
                    "authInfoTime"
                ).toString()
            ) != 0
        ) {
            ConfigManager.setIntValue("authInfoSum", ConfigManager.getIntValue("authInfoSum")+1)
            ConfigManager.setLongValue("authInfoTime", System.currentTimeMillis())
            showGotoAuthentication()
        }*/
        //showGotoAuthentication()


        //app权限申请
        //startActivity(Intent(this, TestAct::class.java))
        //requestBackGroundPer()
        // gotoPowerManager()
        //checkPermissionsRead()
    }

    override fun onResume() {
        super.onResume()
        if (!downLoadding) {
            Handler().postDelayed({ }, 1000)
        }
    }

    fun gotoPowerManager() {
        var intent = Intent();
        try {
            intent.setComponent(
                ComponentName(
                    "com.android.permissioncontroller",
                    "com.android.permissioncontroller.permission.ui.ManagePermissionsActivity"
                )
            )
            startActivity(intent);
        } catch (e: Exception) {
        }

    }



    /**
     * 扫码建单成功，跳到待提货
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun bargainSucess(event: CustomEvent?) {
        if (event?.eventType == CustomEvent.EVENT_HOME_REFRESH) {
            //switchFragment(0)
        }
    }

    private fun refreshAuthorUi() {
        switchFragment(0)
        setViewListener()
    }

    @SuppressLint("AutoDispose")
    private fun setViewListener() {
        binding.llTabOne.setOnClickListener {
            val index = binding.llTabOne.tag.toString().toInt()
            switchFragment(index)
        }
        binding.llTabHy.setOnClickListener {
            val index = binding.llTabHy.tag.toString().toInt()
            switchFragment(index)
        }
        binding.llTabTwo.setOnClickListener {
            val index = binding.llTabTwo.tag.toString().toInt()
            switchFragment(index)
        }

        binding.llTabFive.setOnClickListener {
            val index = binding.llTabFive.tag.toString().toInt()
            switchFragment(index)

        }
    }

    /* 检查使用权限 */
    private fun checkPermissionsRead() {
        RxPermissions(this).request(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            ACCESS_BACKGROUND_LOCATION
        ).autoDisposableDefault(this)
            .subscribe({ granted ->
                Logger.d("initLocation", "init:" + granted)
                if (granted) {

                } else {
                }
            }, {
                it.printStackTrace()
            })
    }

    @SuppressLint("AutoDispose")
    fun scanning() {
        /*   val rxPermissons = RxPermissions(this)
           rxPermissons.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
               .subscribe {
                   if (it) {
                       IntentIntegrator(this)
                           .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                           .setPrompt("扫描二维码建单")// 设置提示语
                           .setCameraId(0)// 选择摄像头,可使用前置或者后置
                           .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                           .setBarcodeImageEnabled(false)// 扫完码之后生成二维码的图片
                           .setCaptureActivity(MyCaptureActivity::class.java)
                           .initiateScan()// 初始化扫码
                   } else {
                       "在手机系统【设置】-【应用管理】-网运通中允许开启相机权限，以正常使用拍照、扫一扫等功能".toast()
                   }
               }*/
    }


    private fun switchFragment(index: Int) {
        try {
            Log.e("http", "switchFragment:" + index);
            if (currentContentPanelIndex != index) {
                val trx = supportFragmentManager.beginTransaction()
                if (currentContentPanelIndex != -1) {
                    trx.hide(fragments[currentContentPanelIndex])
                }
                if (!fragments[index].isAdded) {
                    var tag: String = "" + index
                    if (index == 0) {
                        tag = "mainDriverHomeFragment"
                        if (supportFragmentManager.findFragmentByTag(tag) != null) {
                            mainDriverHomeFragment =
                                supportFragmentManager.findFragmentByTag(tag) as MineFragment
                        }
                        if (!mainDriverHomeFragment.isAdded) {
                            trx.add(binding.viewContentPanel.id, mainDriverHomeFragment, tag)
                            Logger.e("fragments", tag + " ++ " + index)
                        }
                        fragments[index] = mainDriverHomeFragment
                        Logger.e("fragments", tag + " -- " + index)
                    } else if (index == 1) {
                        tag = "goodsSourceFragment"
                        if (supportFragmentManager.findFragmentByTag(tag) != null) {
                            goodsSourceFragment =
                                supportFragmentManager.findFragmentByTag(tag) as MineFragment
                        }
                        if (!goodsSourceFragment.isAdded) {
                            trx.add(binding.viewContentPanel.id, goodsSourceFragment, tag)
                            Logger.e("fragments", tag + " ++ " + index)
                        }
                        fragments[index] = goodsSourceFragment
                        Logger.e("fragments", tag + " -- " + index)
                    } else if (index == 2) {
                        tag = "mainDriverWayBillFragment"
                        if (supportFragmentManager.findFragmentByTag(tag) != null) {
                            mainDriverWayBillFragment =
                                supportFragmentManager.findFragmentByTag(tag) as MineFragment
                        }
                        if (!mainDriverWayBillFragment.isAdded) {
                            trx.add(binding.viewContentPanel.id, mainDriverWayBillFragment, tag)
                            Logger.e("fragments", tag + " ++ " + index)
                        }
                        fragments[index] = mainDriverWayBillFragment
                        Logger.e("fragments", tag + " -- " + index)
                    } else if (index == 3) {
                        tag = "mineFragment"
                        if (supportFragmentManager.findFragmentByTag(tag) != null) {
                            mineFragment =
                                supportFragmentManager.findFragmentByTag(tag) as MineFragment
                        }
                        if (!mineFragment.isAdded) {
                            trx.add(binding.viewContentPanel.id, mineFragment, tag)
                            Logger.e("fragments", tag + " ++ " + index)
                        }
                        fragments[index] = mineFragment
                        Logger.e("fragments", tag + " -- " + index)
                    }
                    //  trx.add(viewContentPanel.id, fragments[index], tag)
                }
                trx.show(fragments[index]).commitAllowingStateLoss()
                Logger.e("fragments", "index：" + index)
                if (!captainInviteFlag) {
                    Handler().postDelayed({
                    }, 800)
                }
            }
            currentContentPanelIndex = index
            setImageResuorce(currentContentPanelIndex)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setImageResuorce(index: Int) {
        binding.ivTabOne.setImageResource(R.mipmap.ic_launcher)
        binding.ivTabHy.setImageResource(R.mipmap.ic_launcher)
        binding.ivTabTwo.setImageResource(R.mipmap.ic_launcher)
        binding.ivTabFive.setImageResource(R.mipmap.ic_launcher)
        binding.tvTabOne.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_84849A))
        binding.tvTabHy.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_84849A))
        binding.tvTabTwo.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_84849A))
        binding.tvTabFive.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_84849A))
        when (index) {
            0 -> {
                binding.ivTabOne.setImageResource(R.mipmap.ic_launcher)
                binding.tvTabOne.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_main_driver))
            }

            1 -> {
                binding.ivTabHy.setImageResource(R.mipmap.ic_launcher)
                binding.tvTabHy.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_main_driver))
            }

            2 -> {
                binding.ivTabTwo.setImageResource(R.mipmap.ic_launcher);
                binding.tvTabTwo.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_main_driver))
            }

            3 -> {
                binding.ivTabFive.setImageResource(R.mipmap.ic_launcher)
                binding.tvTabFive.setTextColor(resources!!.getColor(com.blue.corelib.R.color.color_main_driver))
            }
        }
    }


    //监听按键退出
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN
            && event.keyCode == KeyEvent.KEYCODE_BACK
        ) {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis()
                ("再按一次，退出应用！").toast()
                return true
            } else {
                SystemManager.INSTANCES.exitApp(this)
            }
            return false
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /*if (resultCode == MyCaptureActivity.RESULT_CODE_NOT_SCAN) {
            "图片无法识别二维码".toast()
            return
        }
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //"扫描取消".toast()
                Logger.e("a", "扫描取消。。。")
            } else {
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }*/
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mLastIndex", currentContentPanelIndex);
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}