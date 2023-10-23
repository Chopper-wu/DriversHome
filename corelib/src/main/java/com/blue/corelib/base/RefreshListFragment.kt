package com.blue.corelib.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.blue.corelib.R
import com.blue.corelib.base.config.RegexConfig.SPACING_TIME_S
import com.blue.corelib.base.view.list.AndroidLifecycleUtils
import com.blue.corelib.base.view.list.EmptyView
import com.blue.corelib.base.view.list.LinearDecoration
import com.blue.corelib.base.view.list.ListContaniner
import com.blue.corelib.utils.Logger
import com.blue.corelib.utils.toPx
import com.blue.corelib.utils.toast
import com.blue.corelib.view.ClearEditText
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding.view.RxView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import java.util.*
import java.util.concurrent.TimeUnit


abstract class RefreshListFragment<T> : BaseFragment(), OnRefreshListener, OnLoadMoreListener,
    OnMultiPurposeListener {

    protected var mListDatas: MutableList<T> = ArrayList()

    protected lateinit var mAdapter: RecyclerView.Adapter<*>

    protected var mHFContaniner: ListContaniner<T>? = null

    private var mFooterView: View? = null

    protected var mRefreshlayout: SmartRefreshLayout? = null

    protected var mRvList: RecyclerView? = null

    protected var mRlListContainer: View? = null

    protected lateinit var mLayoutManager: RecyclerView.LayoutManager

    protected var mEmptyView: EmptyView? = null

    protected var topExtend: RelativeLayout? = null

    //搜索框
    protected var etSearch: ClearEditText? = null
    protected var fuzzySearch: String = ""

    //是否上拉加载更多
    var isLoadingMore: Boolean = true

    //限制是否显示运单为空的界面
    var isWaybillEmpty: Boolean = false

    var page = DEFAULT_PAGE
    var pages = DEFAULT_PAGES //总页数

    private var mTvNoMoredataText: TextView? = null
    protected lateinit var mRootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(getLayoutId(), null)
        return mRootView
    }

    open fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    open fun isNeedRefreshAnimation(): Boolean = true


    protected// 添加加载更多没有了的提示
    open fun footerView(): View? {
        mFooterView = LayoutInflater.from(context).inflate(R.layout.view_refresh_footer, null)
        mTvNoMoredataText = mFooterView!!.findViewById(R.id.tv_no_moredata_text)
        mFooterView!!.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return mFooterView as View
    }

    protected val isLayzLoad: Boolean
        get() = true


    open fun isNeedRefreshDataWhenComeIn(): Boolean = false


    open fun isNeedRequestNetDataWhenCacheDataNull(): Boolean = true


    open fun itemDecorationSpacing(): Float = DEFAULT_LIST_ITEM_SPACING


    open fun getItemDecoration(): RecyclerView.ItemDecoration {
        return LinearDecoration(0, itemDecorationSpacing().toPx(), 0, 0)
    }

    open fun isLoadingMoreEnable(): Boolean {
        return isLoadingMore
    }


    open fun isRefreshEnable(): Boolean = true


    abstract fun getAdapter(): RecyclerView.Adapter<*>

    val listDatas: List<T>
        get() = mListDatas

    protected val supportsChangeAnimations: Boolean
        get() = false

    protected val pagesize: Int?
        get() = DEFAULT_PAGE_SIZE

    protected val offset: Int
        get() = page * if (DEFAULT_PAGE_SIZE == null) DEFAULT_PAGE_DB_SIZE else DEFAULT_PAGE_SIZE

    open fun isLoadCache(): Boolean = false

    fun hideLoading() {
        mRefreshlayout!!.finishLoadMore(0)
        mRefreshlayout!!.finishRefresh(0)
    }

    open fun setItemCacheSize(): Int {
        return 3 * DEFAULT_PAGE_DB_SIZE
    }

    open fun getNewDataFromNet() {
        mRvList?.visibility = View.VISIBLE
        if (isRefreshEnable() && isNeedRefreshAnimation() && userVisibleHint) {
            autoRefresh(DEFAULT_REFRESH_DELAY)
        } else {
            page = DEFAULT_PAGE
            requestNetData(page, false)
        }
    }

    protected fun autoRefresh(delayed: Int): Boolean {
        return if (mRefreshlayout == null) {
            false
        } else {
            if (!mRefreshlayout!!.autoRefresh(
                    delayed,
                    DEFAULT_REFRESH_DURATION,
                    DEFAULT_REFRESH_DRAGRATE
                )
            ) {
                page = DEFAULT_PAGE
                requestNetData(page, false)
            }
            return true
        }
    }

    protected fun onEmptyViewClick() {
        getNewDataFromNet()
    }

    protected fun setFooterViewBackgroundColorRes(@DrawableRes resId: Int) {
        if (mFooterView != null) {
            mFooterView!!.findViewById<View>(R.id.fl_footer_container).setBackgroundResource(resId)
        }

    }

    open fun sethasFixedSize(): Boolean {
        return false
    }

    open fun showNoMoreData(): Boolean {
        return mListDatas.size >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE
    }

    /**
     * 当列表数据少于一页时，是否禁用上拉加载
     *
     * @return
     */
    open fun noMoreDataCanLoadMore(): Boolean {
        return false
    }

    /**
     * @return 空数据占位图
     */
    open fun setEmptView(): Int {
        return R.mipmap.no_wifi_img
    }

    /**
     * 页面 View 初始化
     *
     * @param rootView
     */
    open fun initView(rootView: View) {
        mRefreshlayout = rootView.findViewById(R.id.refreshlayout)
        mRlListContainer = rootView.findViewById(R.id.rl_list_container)
        mRvList = rootView.findViewById(R.id.swipe_target)
        topExtend = rootView.findViewById(R.id.topExtend)
        etSearch = rootView.findViewById(R.id.search)
        //        try {
        //            ((TSRefreshHeader) mRefreshlayout.getRefreshHeader()).setBackgroundColor(getRefreashHeaderColor());
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        (mRvList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            supportsChangeAnimations
        mRefreshlayout!!.setEnableFooterFollowWhenLoadFinished(setEnableFooterFollowWhenLoadFinished())
        mRefreshlayout!!.setEnableScrollContentWhenLoaded(setEnableScrollContentWhenLoaded())
        mRefreshlayout!!.setOnMultiPurposeListener(this)
        if (setListBackColor() != -1) {
            mRvList?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    setListBackColor()
                )
            )
        }
        mLayoutManager = getLayoutManager()
        mRvList?.layoutManager = mLayoutManager
        //设置Item的间隔
        mRvList?.addItemDecoration(getItemDecoration())
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvList?.setHasFixedSize(sethasFixedSize())
        mRvList?.setItemViewCacheSize(setItemCacheSize())
        mRvList?.isDrawingCacheEnabled = true
        mRvList?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        //设置动画
        mRvList?.itemAnimator = if (supportsChangeAnimations) DefaultItemAnimator() else null
        mAdapter = getAdapter()
        mHFContaniner = ListContaniner(mAdapter)
        mHFContaniner!!.addFootView(footerView())
        mRvList?.adapter = mHFContaniner
        mRefreshlayout!!.setEnableAutoLoadMore(true)
        mRefreshlayout!!.setEnableRefresh(isRefreshEnable())
        mRefreshlayout!!.setEnableLoadMore(isLoadingMoreEnable())
        mRvList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // SCROLL_STATE_FLING; //屏幕处于甩动状态
                // SCROLL_STATE_IDLE; //停止滑动状态
                // SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
                if (activity != null && !activity!!.isDestroyed) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        sIsScrolling = true
                        Glide.with(activity!!).pauseRequests()
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (sIsScrolling) {
                            if (AndroidLifecycleUtils.canLoadImage(activity)) {
                                Glide.with(activity!!).resumeRequests()
                            }
                        }
                        sIsScrolling = false
                    }
                }
            }
        })
        //        ((TSRefreshFooter) mRefreshlayout.getChildAt(2)).updteFooterHeight(setLoadMoreViewHeight(), setMarginBottom());
    }

    protected fun setEnableFooterFollowWhenLoadFinished(): Boolean {
        return true
    }

    open fun setEnableScrollContentWhenLoaded(): Boolean {
        return true
    }

    /**
     * 数据初始化
     */
    open fun initData() {
        if (!isLayzLoad) {
            if (isLoadCache()) {
                // 获取缓存数据
                requestCacheData(page, false)
            } else {
                getNewDataFromNet()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView(mRootView)
        initData()
    }

    /**
     * Fragment 生命周期
     */
    override fun onResume() {
        super.onResume()
        layzLoad()
    }

    /**
     * @param isVisibleToUser 当前页面是否展示到用户
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        layzLoad()
    }

    /**
     * 数据懒加载
     */
    private fun layzLoad() {
        if (userVisibleHint && isLayzLoad && mListDatas.isEmpty()) {
            getNewDataFromNet()
        }
    }

    /**
     * 设置 LayoutManager 区分列表样式
     *
     * @return
     */
    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(activity) {

            override fun onLayoutChildren(
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State
            ) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    Logger.e("TAG", e.toString())
                }

            }
        }
    }

    /**
     * 设置 list 背景色
     *
     * @return
     */
    protected fun setListBackColor(): Int {
        return -1
    }


    /**
     * 显示提示信息，并消息
     *
     * @param text
     */
    protected fun showMessageNotSticky(text: String) {
        text.toast()
    }

    /**
     * 请求本地数据
     *
     * @param maxId
     * @param isLoadMore
     */
    protected fun requestCacheData(page: Int?, isLoadMore: Boolean) {

    }

    /**
     * 获取网络数据
     *
     * @param 分页数
     * @param isLoadMore
     */
    open fun requestNetData(page: Int?, isLoadMore: Boolean) {

    }

    /**
     * 设置 emptyview 可见性
     *
     * @param visiable true 可见
     */
    fun setEmptyViewVisiable(visiable: Boolean) {
        if (isShowEmptyView()) {
            layzLoadEmptyView()
            if (mEmptyView != null) {
                mEmptyView!!.visibility = if (visiable) View.VISIBLE else View.GONE
            }
            if (emptyViewEnableClick()) {
                mRvList!!.visibility = if (visiable) View.GONE else View.VISIBLE
            }
        }
    }

    fun setWaybillEmpty() {
        isWaybillEmpty = true
    }

    fun setWaybillEmptyTxt(msg: String) {
        mEmptyView?.setWaybillEmptyTxt(msg)
    }

    /**
     * 懒加载 emptyView
     */
    protected fun layzLoadEmptyView() {
        if (mEmptyView == null && isShowEmptyView()) {
            try {
                mEmptyView = mRootView.findViewById(R.id.empty_view)
                mEmptyView!!.setErrorImag(setEmptView())
                mEmptyView!!.setErrorMessage(setEmptViewErrorMsg())
                mEmptyView!!.isEnabled = emptyViewEnableClick()
                if (emptyViewEnableClick()) {
                    RxView.clicks(mEmptyView!!)
                        .throttleFirst(SPACING_TIME_S, TimeUnit.SECONDS)
                        .subscribe { onEmptyViewClick() }
                }
                if (isWaybillEmpty) {
                    mEmptyView?.setWaybillEmpty()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * @return true 占位控件可点击
     */
    protected fun emptyViewEnableClick(): Boolean {
        return true
    }

    /**
     * @return 没有内容的默认文字
     */
    open fun setEmptViewErrorMsg(): String {
        return "暂没有内容哦"
    }

    /**
     * 刷新数据
     */

    open fun refreshData() {
        if (mHFContaniner != null) {
            setEmptyViewVisiable(mListDatas.isEmpty() && mHFContaniner!!.headersCount <= 0)
            try {
                mHFContaniner!!.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 刷新数据
     */

    fun refreshData(datas: List<T>) {
        setEmptyViewVisiable(mListDatas.isEmpty() && mHFContaniner!!.headersCount <= 0)
        mHFContaniner!!.notifyDataSetChanged()
    }

    /**
     * 刷新单条数据
     */

    open fun refreshData(index: Int) {
        setEmptyViewVisiable(mListDatas.isEmpty() && mHFContaniner!!.headersCount <= 0)
        val position = index + mHFContaniner!!.headersCount
        mHFContaniner!!.notifyItemChanged(position)
    }

    /**
     * 刷新数据
     */

    fun refreshRangeData(start: Int, count: Int) {
        if (mHFContaniner != null) {
            setEmptyViewVisiable(mListDatas.isEmpty() && mHFContaniner!!.headersCount <= 0)
            val position = start + mHFContaniner!!.headersCount
            mHFContaniner!!.notifyItemRangeChanged(position, count)
        }
    }

    fun notifyItemRangeInserted(poistionStart: Int, itemCount: Int) {
        if (mHFContaniner != null) {
            setEmptyViewVisiable(mListDatas.isEmpty() && mHFContaniner!!.headersCount <= 0)
            try {
                mHFContaniner!!.notifyItemRangeInserted(
                    poistionStart + mHFContaniner!!.headersCount,
                    itemCount
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 手动刷新
     */

    fun startRefrsh() {
        if (mRefreshlayout != null) {
            mRvList?.scrollToPosition(0)
            autoRefresh(0)
        }
    }

    /**
     * 设置列表背景
     */
    fun setBackgroundColor(color: Int) {
        mRvList?.setBackgroundColor(color)
    }

    /**
     * 下拉刷新
     */
    override fun onRefresh(refreshlayout: RefreshLayout) {
        page = DEFAULT_PAGE
        requestNetData(page, false)
    }

    /**
     * 上拉加载
     */
    override fun onLoadMore(refreshlayout: RefreshLayout) {
        /*if (page>pages){
            "已到最后一页".toast()
            return
        }*/
        page++
        requestNetData(page, true)
    }

    /**
     * 刷新动画完成时
     *
     * @param header
     * @param success
     */
    override fun onHeaderFinish(header: RefreshHeader, success: Boolean) {}

    /**
     * 加载动画完成时
     *
     * @param footer
     * @param success
     */
    override fun onFooterFinish(footer: RefreshFooter, success: Boolean) {

    }

    override fun onHeaderMoving(
        header: RefreshHeader,
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        headerHeight: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onHeaderReleased(header: RefreshHeader, headerHeight: Int, maxDragHeight: Int) {

    }

    override fun onHeaderStartAnimator(
        header: RefreshHeader,
        headerHeight: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onFooterMoving(
        footer: RefreshFooter,
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        footerHeight: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onFooterReleased(footer: RefreshFooter, footerHeight: Int, maxDragHeight: Int) {

    }

    override fun onFooterStartAnimator(
        footer: RefreshFooter,
        footerHeight: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {

    }

    /**
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */

    open fun onNetResponseSuccess(data: MutableList<T>?, isLoadMore: Boolean) {
        hideRefreshState(isLoadMore, true)
        handleReceiveData(data, isLoadMore, false)
    }

    /**
     * 处理获取到的缓存数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */

    open fun onCacheResponseSuccess(data: MutableList<T>?, isLoadMore: Boolean) {
        //        hideRefreshState(isLoadMore,true);
        // 如果没有缓存，直接拉取服务器数据
        if (!isLoadMore && (data == null || data.size == 0) && isNeedRequestNetDataWhenCacheDataNull()) {
            getNewDataFromNet()
        } else {
            // 如果数据库有数据就先显示
            handleReceiveData(data, isLoadMore, true)
            val isCanRequestNetData = mListDatas.isEmpty() && isNeedRefreshDataWhenComeIn()
            if (isCanRequestNetData) {
                // 如果不是游客 >> 进入界面刷新， 如果是游客 >> 数据为空刷新
                getNewDataFromNet()
            }
        }
    }

    /**
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */

    fun onResponseError(throwable: Throwable, isLoadMore: Boolean) {
        hideRefreshState(isLoadMore, false)
        // 刷新
        if (!isLoadMore && mListDatas.size == 0) {
            layzLoadEmptyView()
            if (mHFContaniner!!.headersCount <= 0) {
                setEmptyViewVisiable(true)
            } else {
                setEmptyViewVisiable(true)
                showMessageNotSticky("当前网络不佳")
            }
            mEmptyView?.setErrorType(EmptyView.STATE_NETWORK_ERROR)
            mAdapter.notifyDataSetChanged()
        } else { // 加载更多
            page--
            showMessageNotSticky("当前网络不佳")
        }
    }

    /**
     * 处理服务器或者缓存中拿到的数据
     *
     * @param data       返回的数据
     * @param isLoadMore 是否是加载更多
     */
    protected fun handleReceiveData(data: List<T>?, isLoadMore: Boolean, isFromCache: Boolean) {
        // 刷新
        if (!isLoadMore) {
            if (mFooterView != null) {
                mFooterView!!.visibility = View.GONE
                mTvNoMoredataText!!.visibility = View.GONE
            }
            if (isLoadingMoreEnable()) {
                mRefreshlayout!!.setEnableLoadMore(true)
            }
            if (data != null && data.size != 0) {
                if (!isFromCache) {
                    // 更新缓存

                }
                // 内存处理数据
                if (!isFromCache && refreshDataAllToFirst(data, false)) {
                    mListDatas.addAll(0, data)
                } else {
                    mListDatas.clear()
                    mListDatas.addAll(data)
                }
            } else {
                layzLoadEmptyView()
                if (mEmptyView != null) {
                    mEmptyView!!.visibility = View.VISIBLE
                    mEmptyView!!.setErrorType(EmptyView.STATE_NODATA)
                }
//                if (!mListDatas.isEmpty()) {
//                    refreshNoDataShowTip()
//                }
                //下拉情况下，如果没有数据，则清空列表
                mListDatas.clear()
            }
            refreshData()
        } else { // 加载更多
            if (data != null && data.size != 0) {
                if (!isFromCache) {
                    // 更新缓存
                }
                // 内存处理数据
                refreshDataAllToFirst(data, true)
                mListDatas.addAll(data)
                notifyItemRangeInserted(mListDatas.size - data.size, data.size)
            }
        }
        // 数据加载后，数据为空，说明没有更多数据了，不判断数量是否小于请求数量
        val isNetNoData =
            data == null || data.isEmpty() && pagesize != null && data.size < pagesize!!
        if (!isFromCache && isNetNoData && !noMoreDataCanLoadMore()) {
            mRefreshlayout!!.setEnableLoadMore(false)
            // mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE 当前数量大于一页显示数量时，显示加载更多
            if (showNoMoreData() && mFooterView != null) {
                mFooterView!!.visibility = View.VISIBLE
                mTvNoMoredataText!!.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 当页面有数据，刷新时又没有获取到数据，显示顶部提示信息
     */
    open fun refreshNoDataShowTip() {

    }


    /**
     * 处理关闭加载、刷新状态
     *
     * @param isLoadMore
     * @param success    是否是成功
     */

    open fun hideRefreshState(isLoadMore: Boolean, success: Boolean) {
        if (isLoadMore) {
            mRefreshlayout!!.finishLoadMore(success)
        } else {
            mRefreshlayout!!.finishRefresh(success)
        }
    }


    /**
     * 没有更多数据了的提示
     */
    protected fun setLoadMorNodataTipText(text: String) {
        if (mTvNoMoredataText == null) {
            throw NullPointerException("loadmore text not init!!!")
        }
        mTvNoMoredataText!!.text = text
    }

    /**
     * 没有更多数据了的提示
     */
    protected fun setLoadMorNodataTipBackground(@DrawableRes resid: Int) {
        if (mTvNoMoredataText == null) {
            throw NullPointerException("loadmore text not init!!!")
        }
        mTvNoMoredataText!!.setBackgroundResource(resid)
    }

    /**
     * 将刷新的数据安排到列表顶部
     *
     * @return
     */
    protected fun refreshDataAllToFirst(data: List<T>, isLoadMore: Boolean): Boolean {
        return false
    }

    /**
     * 加载更多的高度
     *
     * @return
     */
    protected fun setLoadMoreViewHeight(): Int {
        return resources.getDimensionPixelOffset(R.dimen.dp_45)
    }


    protected fun setMarginBottom(): Int {

        return 0
    }

    override fun onDestroyView() {
        mEmptyView = null
        super.onDestroyView()
    }

    companion object {

        /**
         * 默认每页的数量
         */
        var DEFAULT_PAGE_SIZE: Int = 10

        /**
         * 数据库默认每页数量
         */
        val DEFAULT_PAGE_DB_SIZE = 10

        /**
         * 一个页面显示的最大条数，用来判断是否显示加载更多
         */
        val DEFAULT_ONE_PAGE_SHOW_MAX_SIZE = 3

        /**
         * 默认初始化列表分页，只对当 max_id 无法使用时有效，如热门动态
         */
        val DEFAULT_PAGE = 1

        /**
         * 默认初始化列表分页，默认总页数
         */
        val DEFAULT_PAGES = 1

        /**
         * 拖拽动画持续时间
         */
        val DEFAULT_REFRESH_DURATION = 250

        /**
         * 刷新默认延迟时间
         */
        val DEFAULT_REFRESH_DELAY = 1

        /**
         * 拉拽的高度比率（要求 ≥ 1 ）
         */
        val DEFAULT_REFRESH_DRAGRATE = 1.2f

        /**
         * 列表顶部提示信息停留时间
         */
        private val DEFAULT_TIP_STICKY_TIME = 3000

        /**
         * 列表分割线高度
         */
        val DEFAULT_LIST_ITEM_SPACING = 0.5f

        /**
         * 避免 Glide.resume.重复设置增加开销
         */
        private var sIsScrolling: Boolean = false
    }

    open fun isShowEmptyView(): Boolean {
        return true
    }

    //设置背景为透明
    open fun showRvBg() {
        mRefreshlayout?.setBackgroundResource(R.color.transparent)
        mRlListContainer?.setBackgroundResource(R.color.transparent)
    }
    //显示搜索信息
    open fun showTopExtendSearch() {
        topExtend?.visibility = View.VISIBLE
        // 键盘点击搜索
        etSearch?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                /*fuzzySearch = etSearch?.text.toString().trim()
                startRefrsh()*/
                HideSoftInput(etSearch?.windowToken)
                return@OnEditorActionListener true
            }
            false
        })
        etSearch?.setAfterTextChangedListener {
            fuzzySearch = etSearch?.text.toString().trim()
            startRefrsh()
        /*if (it.isNullOrBlank()) {
                fuzzySearch = ""
                startRefrsh()
                HideSoftInput(etSearch?.windowToken)
            }*/
        }

    }
}
