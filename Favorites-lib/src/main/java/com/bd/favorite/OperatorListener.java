package com.bd.favorite;

import java.util.List;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/9 15:02
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/9 15:02
 * @ModifyDescription : <Content>
 */

public interface OperatorListener {
    /**
     * FavoritesPicker 取消操作 回调
     */
    public void onCancel();

    /**
     * FavoritesPicker 删除完确定操作 回调
     */
    public void onDone();

    /**
     * 跳转新建收藏 回调
     */
    public String onPick();

    /**
     * 选中 回调
     */
    public void onSelect(Favo favo);


    /**
     * 进入删除模式 回调
     */
    public void onIntoDelete();

    /**
     * 新建收藏 取消操作 回调
     */
    public void onFavoMakerCancel();

    /**
     *  新建收藏 保存操作 回调
     */
    public void onFavoMakerSave(List<Favo> favos);

    /**
     * 删除收藏 回调
     */
    public void onFavoDelete();
}
