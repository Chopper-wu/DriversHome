package com.blue.corelib.http

class NoNetWorkException(message: String = "网络异常,请稍后再试") : RuntimeException(message)

class HttpBodyException(message: String = "网络开小差了,请稍后再试") : RuntimeException(message)

// 1001:停用请登录
class StopException(message: String) : RuntimeException(message)

// 1002:单点登录
class SingleLoginException(message: String) : RuntimeException(message)

// 401:请登录
class TokenException(message: String) : RuntimeException(message)

// 500:系统错误
class SystemException(message: String) : RuntimeException(message)

// 1003:请求参数错误
class ParamsException(message: String) : RuntimeException(message)

// 400:业务错误
class BusinessException(message: String) : RuntimeException(message)

// 402:业务逻辑处理
class BusinessControlException(message: String) : RuntimeException(message)