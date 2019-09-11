package com.adapter.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adapter.BViewHolder;
import com.adapter.BViewHolderFactory;

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
public class TitleItemVHFactory extends BViewHolderFactory {

    @SuppressWarnings("unchecked")
    public BViewHolder createViewHolder(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @NonNull Object item) {
        return new TitleItemVH(inflater, parent);
    }

    final class TitleItemVH extends BViewHolder<TitleItem> {
        private TextView tvText;

        TitleItemVH(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent) {
            super(inflater, parent, false, R.layout.item_text);
            tvText = itemView.findViewById(R.id.tvText);
        }

        @Override
        public void setContents(@NonNull TitleItem item, boolean selectable) {
            tvText.setText(item.getTitle());
        }
    }
}