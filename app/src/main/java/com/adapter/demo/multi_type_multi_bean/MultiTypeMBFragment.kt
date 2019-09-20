package com.adapter.demo.multi_type_multi_bean

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.ImageItem
import com.adapter.model.TextItem
import com.adapter.model.TitleItem
import kotlinx.android.synthetic.main.fragment_list.view.*

class MultiTypeMBFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_multi_type_mb, container, false)

        layout.tvDesc.text = "多数据类型多 ViewType demo。"

        BRecyclerAdapter<Any>(context!!, MultiTypeMBVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItems(getItems())
                .setItemClickListener { _, item, position ->
                    when (item) {
                        is TitleItem -> Toast.makeText(activity, "点击了 Title, position=$position", Toast.LENGTH_SHORT).show()
                        is TextItem -> Toast.makeText(activity, "点击了 Text, position=$position", Toast.LENGTH_SHORT).show()
                        is ImageItem -> Toast.makeText(activity, "点击了 Image, position=$position", Toast.LENGTH_SHORT).show()
                    }
                }

        return layout
    }

    private fun getItems(): List<Any> {
        return arrayListOf(
                TitleItem("文字列表："),
                TextItem("以下内容来源于: https://www.zhihu.com/question/63165436/answer/688123061"),
                TextItem("我用尽了全力，过着平凡的一生。——毛姆《月亮与六便士》"),
                TextItem("没有什么比时间更具有说服力了，因为时间无需通知我们就可以改变一切。——《活着》"),
                TextItem("很不幸，我爱上了太阳，光芒万丈。——亦舒《我的前半生》"),
                TextItem("所有的光鲜靓丽都敌不过时间，并且一去不复返。——菲茨杰拉德《了不起的盖茨比》"),
                TextItem("记住该记住的，忘记该忘记的。改变能改变的，接受不能改变的。——杰罗姆·大卫·塞林格《麦田里的守望者》"),
                TitleItem("图片列表："),
                ImageItem(R.mipmap.ic_launcher),
                ImageItem(R.mipmap.ic_launcher_round),
                ImageItem(R.mipmap.ic_launcher),
                ImageItem(R.mipmap.ic_launcher_round),
                ImageItem(R.mipmap.ic_launcher),
                TitleItem("图文混排列表："),
                ImageItem(R.mipmap.ic_launcher_round),
                TextItem("真正不羁的灵魂不会真的去计较什么，因为他们的内心深处有国王般的骄傲。——杰克·凯鲁亚克《在路上》"),
                ImageItem(R.mipmap.ic_launcher),
                TextItem("压倒她的不是重，而是不能承受的生命之轻。——米兰·昆德拉《不可承受的生命之轻》"),
                ImageItem(R.mipmap.ic_launcher_round),
                TextItem("过去都是假的，回忆是一条没有归途的路，以往的一切春天都无法复原，即使最狂热最坚贞的爱情，归根结底也不过是一种瞬息即逝的现实，唯有孤独永恒。——加西亚·马尔克斯《百年孤独》"),
                ImageItem(R.mipmap.ic_launcher),
                TextItem("一个人一生可能会爱上很多人，等你真正获得属于你的幸福之后，你就会明白，以前的伤痛其实是一种财富，它让你更好地把握和珍惜你爱的人。——出处不明"),
                ImageItem(R.mipmap.ic_launcher_round),
                TextItem("有时候世界虽然是假的，但并不缺少真心对待我们的人。——《楚门的世界》")
        )
    }
}
