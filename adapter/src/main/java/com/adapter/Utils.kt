package com.adapter

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/9/23 11:34
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/9/23 11:34
 * 修改内容：
 */
internal object Utils {
    /**
     * context 是否有效
     */
    fun isContextValid(context: Context): Boolean {
        var tempContext = context

        while (tempContext is ContextWrapper) {
            if (tempContext is Activity) {
                return !tempContext.isFinishing
            }
            tempContext = tempContext.baseContext
        }
        return false
    }
}