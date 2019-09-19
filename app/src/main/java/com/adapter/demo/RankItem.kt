package com.adapter.demo

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/12 9:52
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/12 9:52
 * 修改内容：
 */
data class RankItem(var icon: String, var rank: Int) {
    /**
     * 重写以下方法,避免 List.contains 判断数据不准确
     */
    override fun equals(other: Any?): Boolean {
        if (other is RankItem) {
            return this.icon == other.icon && this.rank == other.rank
        }
        return super.equals(other)
    }

    /**
     * 重写以下方法,避免 Set 存放相同值的对象
     */
    override fun hashCode(): Int {
        return this.rank
    }
}
