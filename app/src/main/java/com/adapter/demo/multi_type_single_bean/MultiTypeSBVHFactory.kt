package com.adapter.demo.multi_type_single_bean

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.demo.model.ChatMsgItem
import kotlinx.android.synthetic.main.item_multi_type_sb_me.view.*
import kotlinx.android.synthetic.main.item_simple.view.ivIcon

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class MultiTypeSBVHFactory : BViewHolderFactory() {
    companion object {
        private const val TYPE_ME = 1
        private const val TYPE_OTHERS = 2
    }

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return if (getItemViewType(item) == TYPE_ME) MyMsgVH(inflater, parent) else OthersMsgVH(inflater, parent)
    }

    /**
     * 对于同种数据类型多 ViewType 的情况要重写以下方法，不然会造成复用上的混乱，
     */
    override fun getItemViewType(item: Any): Int {
        return if ((item as ChatMsgItem).isFromMe) TYPE_ME else TYPE_OTHERS
    }

    private inner class MyMsgVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<ChatMsgItem>(inflater, parent, R.layout.item_multi_type_sb_me) {

        override fun setContents(item: ChatMsgItem, isSelected: Boolean, payload: Any?) {
            itemView.ivIcon.setImageResource(R.mipmap.ic_launcher_round)
            itemView.tvContent.text = item.msg
        }
    }

    private inner class OthersMsgVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<ChatMsgItem>(inflater, parent, R.layout.item_multi_type_sb_others) {

        override fun setContents(item: ChatMsgItem, isSelected: Boolean, payload: Any?) {
            itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
            itemView.tvContent.text = item.msg
        }
    }
}