package com.sjzj.drivershome.event


//发布运单事件
class CustomEvent {
    companion object {
        const val EVENT_WECHAT_LOGIN = 1 //微信登录
        const val EVENT_HOME_REFRESH = 2 //首页订单刷新
        const val EVENT_WAYBILL_RECEIVED_REFRESH = 3 //运单待接单列表刷新
        const val EVENT_CASHOUT_NO_DATA_REFRESH = 4 //未到期列表刷新
        const val EVENT_CASHOUT_NO_REFRESH = 5 //待入账列表刷新
        const val EVENT_WAYBILLSTEP_REFRESH = 6 //运单打卡步骤刷新
        const val EVENT_WAYBILLSTEP_LOAD_NEXT_REFRESH = 7 //装货运单打卡下一步刷新
        const val EVENT_WAYBILLSTEP_UNLOAD_NEXT_REFRESH = 8 //卸货运单打卡下一步刷新
        const val EVENT_WAYBILLSTEP_EVALUA = 9 //卸货运单打卡下一步刷新
        const val EVENT_HOME_REPORT_VISIBLE = 10 //首页订单刷新
        const val EVENT_HOME_IDCARD_VISIBLE = 11 //首页身份验证弹窗控制
        const val EVENT_UNBINDING_VEHICLE = 12 //解绑车队长
        const val EVENT_COMPLAINT = 13 //投诉
        const val EVENT_SERVICE_STATION_CONSUMPTION_DETAILS = 14 //油站消费明细
        const val EVENT_ABNORMAL_LIST = 15 //异常刷新
        const val EVENT_ABNORMAL_SHUNDOW_WAYBILL = 16 //异常关闭运单
        const val EVENT_LOAD_DIALOG_TOAST = "EVENT_LOAD_DIALOG_TOAST" //接单装货提示弹窗
    }

    constructor(eventType: Int?, content: String?) {
        this.eventType = eventType
        this.content = content
    }

    public var eventType: Int? = -1
    public var content: String? = ""

}