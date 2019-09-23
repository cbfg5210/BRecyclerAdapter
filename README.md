# BRecyclerAdapter

最近抽空重新封装了一下 RecyclerView.Adapter，封装过程中学到了很多之前没有用到的知识。下面和大家分享一下封装后的使用方法，还望各位看官多多指点!

#### 实现 BViewHolderFactory 接口方法 createViewHolder : SimpleVHFactory.kt

```java
class SimpleVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): BViewHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
        BViewHolder<RankItem>(inflater, parent, R.layout.item_simple) {

        override fun setContents(item: RankItem, isSelected: Boolean, payload: Any?) {
            itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
            itemView.tvRank.text = item.rank.toString()
        }

        /*
         * 默认会给 itemView 设置点击和长按事件,
         * 如果需要给除 itemView 外的 view 设置点击/长按事件则需要重写以下方法,
         * 否则不用重写以下方法
         */
        override fun setListeners(
            clickListener: View.OnClickListener,
            longClickListener: View.OnLongClickListener
        ) {
            //默认给 itemView 设置点击和长按事件,如果不需要可以去掉以下一句
            super.setListeners(clickListener, longClickListener)

            //给 icon 设置点击事件
            itemView.ivIcon.setOnClickListener(clickListener)
            ////给 icon 设置长按事件
            //itemView.ivIcon.setOnLongClickListener(longClickListener)

            //给 rank 设置点击事件
            itemView.tvRank.setOnClickListener(clickListener)
            ////给 rank 设置长按事件
            //itemView.tvRank.setOnLongClickListener(longClickListener)
        }
    }
}
```

#### 页面使用

```java
 var adapter = BRecyclerAdapter<RankItem>(context, SimpleVHFactory())
            .bindRecyclerView(recyclerView) //为 RecyclerView 设置 adapter
            .setItems(getItems())   //设置数据
            .setItemInfo(RankItem::class.java, selectable = true, multiSelectable = true) //设置 是否可选/单选/多选
            .enableDrag(RankItem::class.java, null) //启用拖拽排序,可传入自定义的 ItemTouchHelper.Callback,如果为空则使用默认的
            .setDragBgColor(RankItem::class.java, 0, Color.LTGRAY) //设置拖拽时和完成后的背景色
            //.setDragBgRes(RankItem::class.java,0,R.drawable.bg_drag_bg) //设置拖拽时和完成后的背景图
            .setItemClickListener { view, item: RankItem, position -> } //设置点击事件
            .setItemLongClickListener { view, item: RankItem, position -> } //设置长按事件
            .setBDiffCallback(object : BDiffCallback<RankItem> {  //如果想要使用 DiffUtil 优化数据更新效果,需要传入这个回调,同时数据类要重写 equals 方法
                override fun areContentsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
                    return true
                }

                override fun getChangePayload(oldItem: RankItem, newItem: RankItem): Any? {
                    return 1
                }
            })

        //设置数据并通知刷新
        adapter.setItems(getItems())
            .notifyDataSetChanged()

        //DiffUtil 更新数据
        adapter.refreshItems(getItems())

        //获取数据
        adapter.items

        //获取选中项
        adapter.selections  
```

#### 最后附上几个 Demo 截图：

![capture_simple](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_simple.png?raw=true)

![capture_payload](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_payload.png?raw=true)

![capture_select_single](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_select_single.png?raw=true)

![capture_select_multi](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_select_multi.png?raw=true)

![capture_select_mix](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_select_mix.png?raw=true)

![capture_multi_type_single_bean](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_multi_type_single_bean.png?raw=true)

![capture_multi_type_multi_bean](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_multi_type_multi_bean.png?raw=true)

![capture_complex](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_complex.png?raw=true)

![capture_diff](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_diff.png?raw=true)

![capture_drag](https://github.com/cbfg5210/BRecyclerAdapter/blob/master/captures/capture_drag.png?raw=true)
