package com.bd.favorite;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import java.io.File;
import java.nio.BufferOverflowException;
import java.util.LinkedList;
import java.util.List;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/4 10:19
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/4 10:19
 * @ModifyDescription : <Content>
 */
public class FavoritePicker extends LinearLayout {

    private int MODE;
    private OperatorListener mOperatorListener;


    private static final int MODE_EMPTY = 0x01;
    private static final int MODE_FILL = 0x02;
    private static final int MODE_DONE = 0x03;
    private int DEFAULT_MODE = MODE_EMPTY;
    private LinearLayout mPanel;
    private FlexboxLayout mPickerContainer;
    private ViewGroup mParent;
    private FrameLayout mMakerContainer;
    private EditText mDescriptionEt;
    private FavoMaker mMaker;

    public FavoritePicker(Context context) {
        this(context, null);
    }

    public FavoritePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.favoritePickerStyle);
    }

    public FavoritePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FavoritePicker,
                defStyleAttr, 0);
        MODE = attributes.getInt(R.styleable.FavoritePicker_stateMode, DEFAULT_MODE);
        mMeasureScreenwidth = Utils.screenWidth(getContext());
        mSpace = Utils.dp2px(getContext(), 5);
        attributes.recycle();
        prepareData();
        updateByMode();
    }

    private List<Favo> mData;

    /**
     * 操作监听
     *
     * @param listener
     */
    public void setOperatorListener(OperatorListener listener) {
        this.mOperatorListener = listener;
    }

    /**
     * 外部绑定数据
     *
     * @param data
     */
    public void bind(List<Favo> data) {
        if (null == data || data.size() == 0) {
            MODE = MODE_EMPTY;
        } else if (data.size() > LIMIT_FAVO) {
            throw new BufferOverflowException();
        } else if (data.size() > 0) {
            mData.addAll(data);
            MODE = MODE_FILL;
        }
        updateByMode();
    }

    private void prepareData() {
        mData = new LinkedList<>();
    }

    private int mSpace;

    private int COLUMN_NUM = 3;

    private int LIMIT_FAVO = 6;

    private ImageView mDeleteImageView;

    private TextView mDoneTextView;

    /*设置栅格总数*/
    public void setLimitFavo(int limit) {
        LIMIT_FAVO = limit;
    }

    /*设置栅格间距*/
    public void setSpace(int space) {
        mSpace = space;
    }

    /*设置栅格列数*/
    public void setColumnNumber(int columnNumber) {
        COLUMN_NUM = columnNumber;
    }

    /**
     * 模式更新
     */
    private void updateByMode() {
        removeAllViews();/*can be better*/
        mPanel = (LinearLayout) inflate(getContext(), R.layout.picker_panel, null);
        mPickerContainer = (FlexboxLayout) mPanel.findViewById(R.id.picker);
        callOnCancel(mPanel.findViewById(R.id.cancel));

        if (MODE == MODE_EMPTY) {
            generateGrid(1, COLUMN_NUM);
        } else if (MODE == MODE_FILL) {
            generateGrid(LIMIT_FAVO, COLUMN_NUM);
            mDeleteImageView = (ImageView) ((ViewStub) mPanel.findViewById(R.id.delete)).inflate();
            intoDelete(mDeleteImageView);
        } else if (MODE == MODE_DONE) {
            generateGrid(LIMIT_FAVO, COLUMN_NUM);
            mDoneTextView = (TextView) ((ViewStub) mPanel.findViewById(R.id.done)).inflate();
            callOnDone(mDoneTextView);
        }
        addView(mPanel);
        onConfigurationChangedPanel();
    }

    public void setPanelBackground(int resid) {
        mPanel.setBackgroundResource(resid);
    }

    /**
     * 进入完成模式
     *
     * @param view
     */
    private void callOnDone(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = MODE_FILL;
                updateByMode();
                if (null != mOperatorListener)
                    mOperatorListener.onDone();
            }
        });
    }

    /**
     * 退出
     *
     * @param cancelView
     */
    private void callOnCancel(View cancelView) {
        cancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*add animation translation out*/
                FavoritePicker.this.setVisibility(View.GONE);
                if (null != mOperatorListener)
                    mOperatorListener.onCancel();
            }
        });
    }

    /**
     * 进入删除模式
     *
     * @param deleteView
     */
    private void intoDelete(View deleteView) {
        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = MODE_DONE;
                updateByMode();
                if (null != mOperatorListener)
                    mOperatorListener.onIntoDelete();
            }
        });
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mMeasureScreenwidth = Utils.screenWidth(getContext());
        updateByMode();
    }

    private void onConfigurationChangedPanel() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setPanelBackground(R.drawable.corner_card_overlay);
            ((TextView) mPanel.findViewById(R.id.panel_title)).setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            if (MODE == MODE_DONE)
                mDoneTextView.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            setPanelBackground(R.drawable.corner_card_white);
            ((TextView) mPanel.findViewById(R.id.panel_title)).setTextColor(getResources().getColor(android.R.color.primary_text_light));
            if (MODE == MODE_DONE)
                mDoneTextView.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }
    }

    private int mMeasureScreenwidth;

    /**
     * 动态生成栅格
     *
     * @param totalNum
     * @param columnNum
     */
    private void generateGrid(int totalNum, int columnNum) {
        int sideLength = (mMeasureScreenwidth - Utils.dp2px(getContext(), mSpace) * (columnNum + 1)) / columnNum;
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(sideLength, (int) (sideLength * 2.0 / 3.0));
        lp.leftMargin = Utils.dp2px(getContext(), mSpace);
        lp.topMargin = Utils.dp2px(getContext(), mSpace);

        RelativeLayout item;
        int size = mData.size();
        Favo favo;
        for (int index = 0; index < totalNum; index++) {
            item = (RelativeLayout) inflate(getContext(), R.layout.image_panel, null);
            callOnPick(item, size, index);
            load(item, size, index);
            item.setLayoutParams(lp);
            mPickerContainer.addView(item);
            /*MODE_DONE show delete badge*/
            badgeView(item, size, index);
        }
    }

    /**
     * 加删除角标
     *
     * @param item
     * @param size
     * @param index
     */
    private void badgeView(View item, int size, int index) {
        if (MODE == MODE_DONE && size > index) {
            BadgeView bv = new BadgeView(getContext(), item);
            delete(bv, index);
            bv.setText("—");
            bv.show();
        }
    }

    /**
     * 绑定数据
     *
     * @param item
     * @param size
     * @param index
     */
    private void load(View item, int size, int index) {
        if (size > index) {
            Favo favo = mData.get(index);
            loadText(item, favo);
            loadImage(item, favo);
        }
    }

    /**
     * 绑定文字
     *
     * @param item
     * @param favo
     */
    private void loadText(View item, Favo favo) {
        TextView desc = (TextView) item.findViewById(R.id.description);
        desc.setText(TextUtils.isEmpty(favo.getName()) ? "" : favo.getName());
        desc.setVisibility(View.VISIBLE);
    }

    /**
     * 绑定图片
     *
     * @param item
     * @param favo
     */
    private void loadImage(View item, Favo favo) {
        ImageButton imageBtn = (ImageButton) item.findViewById(R.id.image);
        if (favo.isSelected)
            setSelected(imageBtn);
        if (URLUtil.isNetworkUrl(favo.getImageUrl()))
            Glide.with(getContext()).load(favo.getImageUrl()).into(imageBtn);
        else {  //TODO can be better!
            if (!TextUtils.isEmpty(favo.getImageUrl())) {
                File image = new File(favo.getImageUrl());
                if (image.exists())
                    Glide.with(getContext()).load(image).into(imageBtn);
            }

        }
    }

    /**
     * 新建收藏
     * 默认无描述
     *
     * @param uri
     * @return
     */
    public Favo generateFavo(String uri) {
        int id = (int) (System.currentTimeMillis() / 1000);
        return new Favo(id, "", uri);
    }

    /**
     * 点击栅格
     *
     * @param view
     * @param size
     * @param index
     */
    private void callOnPick(final View view, final int size, final int index) {
        if (size <= index) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = null;
                    if (null != mOperatorListener)
                        uri = mOperatorListener.onPick();

                    Favo favo = generateFavo(uri);
                    if (null == mMakerContainer) {
                        setupMaker(favo);
                    } else {
                       /* 避免多次加入 */
                        mParent.removeView(mMakerContainer);
                        updateMaker(favo, mMakerContainer);
                        mParent.addView(mMakerContainer);
                    }


                }
            });
        } else {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(index);
                    updateByMode();
                    if (null != mOperatorListener)
                        mOperatorListener.onSelect(mData.get(index));
                }
            });
        }
    }

    /**
     * 图片选中效果
     *
     * @param v
     */
    private void setSelected(ImageButton v) {
        v.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * 设置选中位置
     *
     * @param position
     */
    private void setSelected(int position) {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setSelected(i == position);
        }
    }

    /**
     * 生成收藏
     *
     * @param favo
     */
    private void setupMaker(Favo favo) {
        mParent = (ViewGroup) FavoritePicker.this.getParent();
        mMakerContainer = new FrameLayout(getContext());
        mMakerContainer.setBackgroundResource(R.color.light_black_overlay);
        mMakerContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMaker = new FavoMaker(getContext());
        FrameLayout.LayoutParams makerParams = new FrameLayout.LayoutParams(Utils.dp2px(getContext(), 240f), Utils.dp2px(getContext(), 360f));
        makerParams.gravity = Gravity.CENTER;
        mMaker.setLayoutParams(makerParams);
        mMakerContainer.addView(mMaker);
        mParent.addView(mMakerContainer);
        mDescriptionEt = (EditText) mMaker.findViewById(R.id.et_desc);

        updateMaker(favo, mMakerContainer);

        BadgeView badgeView = new BadgeView(getContext(), mMaker);
        badgeView.setText("╳");
        badgeView.show();
        callOnCancelMaker(badgeView);
    }

    /**
     * 输入法完成监听
     *
     * @param favo
     */
    private void setActionListener(final Favo favo) {
        mDescriptionEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveFavo(favo);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 更新新建的收藏
     *
     * @param favo
     * @param container
     */
    private void updateMaker(final Favo favo, View container) {
        setActionListener(favo);
        mMaker.onConfigurationChangedLayout();
        mDescriptionEt.setText(TextUtils.isEmpty(favo.getName()) ? "" : favo.getName());
        loadImage(mMaker, favo);
        callOnSaveFavo(favo, container);
    }

    /**
     * 保存收藏
     *
     * @param favo
     * @param container
     */
    private void callOnSaveFavo(final Favo favo, View container) {
        container.findViewById(R.id.save_favo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFavo(favo);
            }
        });
    }

    private void saveFavo(Favo favo) {
        favo.setName(mDescriptionEt.getText().toString().trim());
        hideSoftKeyboard();
        mParent.removeView(mMakerContainer);
        mData.add(favo);
        MODE = MODE_FILL;
        updateByMode();
        if (null != mOperatorListener)
            mOperatorListener.onFavoMakerSave(mData);
    }

    InputMethodManager inputMethodManager;

    /**
     * 隐藏输入法
     */
    public void hideSoftKeyboard() {

        if (null == inputMethodManager) {
            inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inputMethodManager.hideSoftInputFromWindow(mDescriptionEt.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * 取消新建的收藏
     *
     * @param badgeView
     */
    private void callOnCancelMaker(BadgeView badgeView) {
        badgeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.removeView(mMakerContainer);
                if (null != mOperatorListener)
                    mOperatorListener.onFavoMakerCancel();
            }
        });
    }

    /**
     * 删除收藏
     *
     * @param bv
     * @param index
     */
    private void delete(BadgeView bv, final int index) {
        bv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(index);
                updateByMode();
                if (null != mOperatorListener)
                    mOperatorListener.onFavoDelete();
            }
        });
    }
}
