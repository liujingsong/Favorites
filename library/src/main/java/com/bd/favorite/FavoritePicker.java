package com.bd.favorite;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

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


    private static final int MODE_EMPTY = 0x01;
    private static final int MODE_FILL = 0x02;
    private static final int MODE_DONE = 0x03;
    private int DEFAULT_MODE = MODE_EMPTY;
    private LinearLayout mPanel;
    private FlexboxLayout mPickerContainer;
    private ViewGroup mParent;
    private FrameLayout mMakerContainer;

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
        MODE = attributes.getInt(R.styleable.FavoritePicker_mode, DEFAULT_MODE);
        attributes.recycle();
        prepareData();
        updateByMode();
    }

    private List<Favo> mData;

    /**
     * the wrapper to bind data to grid layout
     *
     * @param data
     */
    public void bind(List<Favo> data) {
        if (data.size() > LIMIT_FAVO)
            throw new BufferOverflowException();
        else if (data.size() > 0) {
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

    private void updateByMode() {
        removeAllViews();/*can be better*/
        mPanel = (LinearLayout) inflate(getContext(), R.layout.picker_panel, null);
        mPickerContainer = (FlexboxLayout) mPanel.findViewById(R.id.picker);
        callOnCancel(mPanel.findViewById(R.id.cancel));
        mSpace = Utils.dp2px(getContext(), 5);
        if (MODE == MODE_EMPTY) {
            generateGrid(1, COLUMN_NUM);
        } else if (MODE == MODE_FILL) {
            generateGrid(LIMIT_FAVO, COLUMN_NUM);
            mDeleteImageView = (ImageView) ((ViewStub) mPanel.findViewById(R.id.delete)).inflate();
            callOnDelete(mDeleteImageView);
        } else if (MODE == MODE_DONE) {
            generateGrid(LIMIT_FAVO, COLUMN_NUM);
            mDoneTextView = (TextView) ((ViewStub) mPanel.findViewById(R.id.done)).inflate();
            callOnDone(mDoneTextView);
        }
        addView(mPanel);
    }

    private void callOnDone(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = MODE_FILL;
                updateByMode();
            }
        });
    }


    private void callOnCancel(View cancelView) {
        cancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*add animation translation out*/
                FavoritePicker.this.setVisibility(View.GONE);
            }
        });
    }

    private void callOnDelete(View deleteView) {
        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = MODE_DONE;
                updateByMode();
            }
        });
    }

    private void generateGrid(int totalNum, int columnNum) {
        int sideLength = (Utils.screenWidth(getContext()) - Utils.dp2px(getContext(), mSpace) * (columnNum + 1)) / columnNum;
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(sideLength, sideLength);
        lp.leftMargin = Utils.dp2px(getContext(), mSpace);
        lp.topMargin = Utils.dp2px(getContext(), mSpace);

        ImageButton item;
        int size = mData.size();
        Favo favo;
        for (int index = 0; index < totalNum; index++) {
            item = (ImageButton) inflate(getContext(), R.layout.image_panel, null);
            callOnPick(item, size, index);
            load(item, size, index);
            item.setLayoutParams(lp);
            mPickerContainer.addView(item);
            /*MODE_DONE show delete badge*/
            badgeView(item, size, index);
        }
    }

    private void badgeView(ImageButton item, int size, int index) {
        if (MODE == MODE_DONE && size > index) {
            BadgeView bv = new BadgeView(getContext(), item);
            delete(bv, index);
            bv.setText("—");
            bv.show();
        }
    }

    private void load(ImageButton item, int size, int index) {
        Favo favo;
        if (size > index) {
            favo = mData.get(index);
            Glide.with(getContext()).load(favo.getImageUrl()).into(item);
        }
    }

    /*for test*/
    public Favo generateFavo() {
        return new Favo(4, "第四张", "http://download.easyicon.net/png/1082117/128/");
    }




    private void callOnPick(View view, int size, int index) {
        if (size <= index) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Favo favo = generateFavo();
                    if (null == mMakerContainer) {
                        setupMaker(favo);
                    } else {
                        updateMaker(favo, mMakerContainer);
                        mParent.addView(mMakerContainer);
                    }

                }
            });
        }
    }

    private void setupMaker(Favo favo) {
        mParent = (ViewGroup) FavoritePicker.this.getParent();
        mMakerContainer = new FrameLayout(getContext());
        mMakerContainer.setBackgroundResource(R.color.light_black_overlay);
        mMakerContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        FavoMaker maker = new FavoMaker(getContext());
        FrameLayout.LayoutParams makerParams = new FrameLayout.LayoutParams(Utils.dp2px(getContext(), 240f), Utils.dp2px(getContext(), 360f));
        makerParams.gravity = Gravity.CENTER;
        maker.setLayoutParams(makerParams);
        mMakerContainer.addView(maker);
        mParent.addView(mMakerContainer);

        updateMaker(favo, mMakerContainer);

        BadgeView badgeView = new BadgeView(getContext(), maker);
        badgeView.setText("X");
        badgeView.show();
        callOnCancelMaker(badgeView);
    }

    private void updateMaker(final Favo favo, View container) {
        ImageButton mImagePanel = (ImageButton) container.findViewById(R.id.image_panel);
        Glide.with(getContext()).load(favo.getImageUrl()).into(mImagePanel);
        callOnSaveFavo(favo, container);
    }

    private void callOnSaveFavo(final Favo favo, View container) {
        container.findViewById(R.id.save_favo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.removeView(mMakerContainer);
                mData.add(favo);
                MODE = MODE_FILL;
                updateByMode();
            }
        });
    }


    private void callOnCancelMaker(BadgeView badgeView) {
        badgeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mParent.removeView(mMakerContainer);
            }
        });
    }

    private void delete(BadgeView bv, final int index) {
        bv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(index);
                updateByMode();
            }
        });
    }


}
