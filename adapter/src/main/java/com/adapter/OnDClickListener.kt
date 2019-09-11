package com.adapter

import android.view.View

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/9/12 17:06
 * 功能描述：点击、长按事件
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2018/9/12 17:06
 * 修改内容：
 *
 * @param <Any>
 */
interface OnDClickListener<T> {
    fun onClick(view: View, item: T, position: Int)
}