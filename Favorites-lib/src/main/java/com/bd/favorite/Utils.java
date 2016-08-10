package com.bd.favorite;

import android.content.Context;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/1 17:41
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/1 17:41
 * @ModifyDescription : <Content>
 */
public class Utils {
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale);
    }


    public static int screenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}
