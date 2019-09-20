package com.adapter.demo.multi_type_multi_bean

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.model.ImageItem
import com.adapter.model.TextItem
import com.adapter.model.TitleItem
import kotlinx.android.synthetic.main.item_multi_type_mb_image.view.*
import kotlinx.android.synthetic.main.item_multi_type_mb_text.view.*
import kotlinx.android.synthetic.main.item_multi_type_mb_title.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class MultiTypeMBVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return when (item) {
            is TextItem -> TextItemVH(inflater, parent)
            is ImageItem -> ImageItemVH(inflater, parent)
            else -> TitleItemVH(inflater, parent)
        }
    }

    private inner class TitleItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<TitleItem>(inflater, parent, R.layout.item_multi_type_mb_title) {

        override fun setContents(item: TitleItem, isSelected: Boolean, payload: Any?) {
            itemView.tvTitle.text = item.title
        }
    }

    private inner class TextItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<TextItem>(inflater, parent, R.layout.item_multi_type_mb_text) {

        override fun setContents(item: TextItem, isSelected: Boolean, payload: Any?) {
            itemView.tvText.text = item.text
        }
    }

    private inner class ImageItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<ImageItem>(inflater, parent, R.layout.item_multi_type_mb_image) {

        override fun setContents(item: ImageItem, isSelected: Boolean, payload: Any?) {
            itemView.ivImage.setImageResource(item.imageRes)
        }
    }
}