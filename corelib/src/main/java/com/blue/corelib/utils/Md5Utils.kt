package com.blue.corelib.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.DriverManager.println
import java.util.*

object Md5Utils {
    /**
     * @Comment SHA1实现
     * @Author Ron
     * @Date 2017年9月13日 下午3:30:36
     * @return
     */
   /* @Throws(Exception::class)
    fun shaEncode(inStr: String): String {
        var sha: MessageDigest? = null
        sha = try {
            MessageDigest.getInstance("SHA")
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
            return ""
        }
        val byteArray = inStr.toByteArray(charset("UTF-8"))
        val md5Bytes = sha.digest(byteArray)
        val hexValue = StringBuffer()
        for (i in md5Bytes.indices) {
            val `val` = md5Bytes[i].toInt() and 0xff
            if (`val` < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(`val`))
        }
        return hexValue.toString()
    }*/
/*
    fun generateToken(): String {
        val s = (System.currentTimeMillis() + Random().nextInt()).toString()
        return shaEncode(s)
    }*/
}