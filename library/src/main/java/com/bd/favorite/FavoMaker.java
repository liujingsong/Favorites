package com.bd.favorite;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/5 8:39
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/5 8:39
 * @ModifyDescription : <Content>
 */
public class FavoMaker extends RelativeLayout {

    private View mLayout;

    public FavoMaker(Context context) {
        super(context);
        init();
    }

    public FavoMaker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavoMaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mLayout = inflate(getContext(), R.layout.favo_maker, null);
        addView(mLayout);
    }


}
