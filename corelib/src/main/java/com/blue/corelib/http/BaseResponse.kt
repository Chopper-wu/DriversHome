package com.blue.corelib.http

import java.io.Serializable

/**
 * 请求响应
 */
class BaseResponse<T> : Serializable {
    // 数据实体
    var data: T? = null

    // 请求响应描述
    var msg: String? = null

    // 请求响应状态码
    var code: Int = 0
}
