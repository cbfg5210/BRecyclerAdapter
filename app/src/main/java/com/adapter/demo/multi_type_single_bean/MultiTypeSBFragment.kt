package com.adapter.demo.multi_type_single_bean

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.ChatMsgItem
import kotlinx.android.synthetic.main.fragment_list.view.*

class MultiTypeSBFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_multi_type_sb, container, false)

        layout.tvDesc.text = "同种数据类型多 ViewType demo。"

        BRecyclerAdapter<ChatMsgItem>(context!!, MultiTypeSBVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItems(getItems())
                .setItemClickListener { _, item, position ->
                    if (item.isFromMe) {
                        Toast.makeText(activity, "点击了的是自己的消息，position=$position", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "点击了的是别人的消息，position=$position", Toast.LENGTH_SHORT).show()
                    }
                }

        return layout
    }

    private fun getItems(): List<ChatMsgItem> {
        return arrayListOf(
                ChatMsgItem(true, "以下内容来源于: https://www.zhihu.com/question/63165436/answer/688123061"),
                ChatMsgItem(true, "人生的一切收获都压在这沉甸甸的苦字下边。—— 冯骥才《苦夏》"),
                ChatMsgItem(false, "人性的确如此，既轻信又爱怀疑，说它软弱它又很顽固，自己打不定主意，为别人做事却又很有决断。——萨克雷《名利场》"),
                ChatMsgItem(true, "我渴望能见你一面，但请你记得，我不会开口要求要见你。这不是因为骄傲，你知道我在你面前毫无骄傲可言，而是因为，唯有你也想见我的时候，我们见面才有意义。—— 西蒙那•德•波伏娃《越洋情书》"),
                ChatMsgItem(true, "年轻的时候以为不读书不足以了解人生，直到后来才发现如果不了解人生，是读不懂书的。——杨绛"),
                ChatMsgItem(false, "在隆冬，我终于知道，我身上有一个不可战胜的夏天。——阿尔贝•加缪《夏天集》"),
                ChatMsgItem(false, "被别人揭下面具是一种失败，自己揭下面具却是一种胜利。—— 雨果《海上劳工》"),
                ChatMsgItem(true, "不要为那些不愿在你身上花费时间的人而浪费你的时间——玛格丽特·米切尔《飘》"),
                ChatMsgItem(false, "凡事都有偶然的凑巧，结果却又如宿命的必然。——沈从文《边城》"),
                ChatMsgItem(true, "人生的每一条路都是临时走出来的，一个人在醒着的时候，他在生命的大部分时间里都注视着自己的脚下。—— 威廉•戈尔丁《蝇王》"),
                ChatMsgItem(false, "猛兽总是独行，牛羊才成群结队。——鲁迅《鲁迅杂文精选》"),
                ChatMsgItem(true, "和你一同笑过的人，你可能把他忘掉，但是和你一同哭过的人，你却永远不忘。—— 纪伯伦《沙与沫》"),
                ChatMsgItem(false, "我年纪还轻，阅历不深的时候，我父亲教导过我一句话，我至今还念念不忘。 “每逢你想要批评任何人的时候。”他对我说，“你就记住，这个世界上所有的人，并不是个个都有过你拥有的那些优越条件。”——菲茨杰拉德 《了不起的盖茨比》"),
                ChatMsgItem(true, "闲时与你立黄昏，灶前笑问粥可温。——沈复《浮生六记》"),
                ChatMsgItem(false, "好书像真爱，可能一见钟情，但死生契阔，与子成说，执子之手，与子偕老的杳远理解和同情却总需要悠悠岁月。——海莲·汉芙 《查令十字街84号》"),
                ChatMsgItem(true, "在所谓“人世间”摸爬滚打，我唯一愿意视为真理的就只有这一句话：一切都会过去的。——太宰治《人间失格》"),
                ChatMsgItem(false, "一个我爱过的人给了我一盒子黑暗。很多年后我才明白，这也是一份礼物。——玛丽•奥利弗《悲伤之用》"),
                ChatMsgItem(true, "在人的一生，有些细微之事，本身毫无意义可言，却具有极大的重要性。事过境迁之后，回顾其因果关系，却发现其影响之大，殊可惊人。——林语堂 《京华烟云》"),
                ChatMsgItem(true, "也许你我终将行踪不明，但是你该知道我曾为你动情。——波德莱尔《恶之花》"),
                ChatMsgItem(false, "爱情应该给人一种自由感，而不是囚禁感。——劳伦斯《儿子与情人》"),
                ChatMsgItem(false, "有时候远方唤起的渴望并非是引向陌生之地，而是一种回家的召唤。——瓦尔特•本雅明《驼背小人》"),
                ChatMsgItem(true, "我只要看见你这面镜子没有尘埃，整个世界对于我便晴空万里。——席勒"),
                ChatMsgItem(false, "生命是在低谷里孕育出来的。它随着古来的恐惧、古老的欲念、古老的绝望一直吹到山顶。我们之所以必须一步步上山，就是为了可以坐车下山。——威廉•福克纳《我弥留之际》"),
                ChatMsgItem(true, "好了，装逼结束，谢谢大家！")
        )
    }
}
