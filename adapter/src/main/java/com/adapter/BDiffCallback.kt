package com.adapter

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/9/23 8:48
 * 功能描述：参考 DiffUtil.Callback
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/9/23 8:48
 * 修改内容：
 */
interface BDiffCallback<T> {
    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * <p>
     * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * For example, if you are using DiffUtil with a
     * {@link RecyclerView.Adapter RecyclerView.Adapter}, you should
     * return whether the items' visual representations are the same.
     * <p>
     * This method is called only if {@link #areItemsTheSame(int, int)} returns
     * {@code true} for these items.
     *
     * @param oldItem The item in the old list
     * @param newItem The item in the new list which replaces the
     *                        oldItem
     * @return True if the contents of the items are the same or false if they are different.
     */
    fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * When {@link #areItemsTheSame(int, int)} returns {@code true} for two items and
     * {@link #areContentsTheSame(int, int)} returns false for them, DiffUtil
     * calls this method to get a payload about the change.
     * <p>
     * For example, if you are using DiffUtil with {@link RecyclerView}, you can return the
     * particular field that changed in the item and your
     * {@link RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * <p>
     * Default implementation returns {@code null}.
     *
     * @param oldItem The item in the old list
     * @param newItem The item in the new list
     *
     * @return A payload object that represents the change between the two items.
     */
    fun getChangePayload(oldItem: T, newItem: T): Any?
}