package com.mrtrying.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.mrtrying.bindexviewdemo.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

/**
 * Description :
 * PackageName : com.mrtrying.bindexviewdemo
 * Created by mrtrying on 2018/7/27 15:10.
 * e_mail : ztanzeyu@gmail.com
 */
public class BindexNavigationView extends View {
    public static final String TAG = BindexNavigationView.class.getSimpleName();
    /*绘制的列表导航集合*/
    private final ArrayList<IndexBean> indexBeanList = new ArrayList<>();
    private static final int[] ANDROID_ATTRS = new int[]{android.R.attr.textSize, android.R.attr.gravity};
    /*字母画笔*/
    private Paint wordsPaint;
    /*字母背景画笔*/
    private Paint bgPaint;
    /** 字幕宽度 */
    private int itemWidth;
    /** 字幕高度 */
    private int itemHeight;
    /** 当前选中index */
    private int currentSelectIndex = 0;
    /** 暂时未实现 */
    private int gravity = Gravity.CENTER;
    /** 选中文字颜色 */
    private int selectedTextColor = Color.WHITE;
    /** 未选中文字颜色 */
    private int unselectedTextColor = Color.BLACK;
    /** 选中背景色 */
    private int selectedBackgroundColor = 0;
    /** 选中背景 */
    private Drawable selectedBackgroundDrawable;
    /** 背景色 */
    private int backgroundColor = android.R.color.transparent;
    /** 背景 */
    private GradientDrawable backgroundDrawable;
    private boolean backgroundIsRound;
    private boolean backgroundIsFill;
    private RectF bgRectF;
    /** 字体大小 */
    private int textSize = 28;
    private int offsetTop;
    private int realHeight;
    private int height;

    private List<OnItemSelectedListener> onItemSelectedListeners;

    public BindexNavigationView(Context context) {
        this(context, null);
    }

    public BindexNavigationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BindexNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    @SuppressLint("ResourceAsColor")
    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray originalAttrs = context.obtainStyledAttributes(attrs, ANDROID_ATTRS);
            textSize = originalAttrs.getDimensionPixelSize(0, textSize);
            gravity = originalAttrs.getInt(1, gravity);
            originalAttrs.recycle();

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindexNavigationView);
            backgroundColor = a.getColor(R.styleable.BindexNavigationView_backgroundColor, backgroundColor);
            selectedTextColor = a.getColor(R.styleable.BindexNavigationView_selectedTextColor, selectedTextColor);
            unselectedTextColor = a.getColor(R.styleable.BindexNavigationView_unselectedTextColor, unselectedTextColor);
            selectedBackgroundColor = a.getColor(R.styleable.BindexNavigationView_selectedBackgroundColor, selectedBackgroundColor);
            selectedBackgroundDrawable = a.getDrawable(R.styleable.BindexNavigationView_selectedBackgroundDrawable);
            backgroundIsRound = a.getBoolean(R.styleable.BindexNavigationView_backgroundIsRound, backgroundIsRound);
            backgroundIsFill = a.getBoolean(R.styleable.BindexNavigationView_backgroundIsRound, backgroundIsFill);
            a.recycle();
        }

        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(backgroundColor);

        wordsPaint = new Paint();
        wordsPaint.setColor(unselectedTextColor);
        wordsPaint.setTextSize(textSize);
        wordsPaint.setAntiAlias(true);
        wordsPaint.setTextAlign(Paint.Align.CENTER);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(selectedBackgroundColor);

        onItemSelectedListeners = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        height = getMeasuredHeight();
        setRealHeight();
        setBackgroundRectF();
    }

    private void setBackgroundRectF() {
        //是否有圆角
        if (backgroundIsRound) {
            backgroundDrawable.setCornerRadius(itemWidth / 2);
        }
        bgRectF = new RectF();
        bgRectF.left = getPaddingLeft();
        bgRectF.right = bgRectF.left + itemWidth;
        //高度是否撑满
        if (backgroundIsFill) {
            bgRectF.top = getTop();
            bgRectF.bottom = getBottom();
        } else {
            bgRectF.top = offsetTop;
            bgRectF.bottom = realHeight + offsetTop;
        }
        backgroundDrawable.setBounds((int) bgRectF.left, (int) bgRectF.top, (int) bgRectF.right, (int) bgRectF.bottom);
    }

    private void setRealHeight() {
        if (!indexBeanList.isEmpty()) {
            realHeight = itemWidth * indexBeanList.size();
            if (height > realHeight) {
                offsetTop = (height - realHeight) / 2 + getPaddingTop();
            }
            itemHeight = realHeight / indexBeanList.size();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.saveLayer(bgRectF, null, Canvas.ALL_SAVE_FLAG);
        backgroundDrawable.draw(canvas);
        //绘制索引
        for (int i = 0; i < indexBeanList.size(); i++) {
            boolean isSelected = i == currentSelectIndex;
            if (isSelected) {
                //绘制选中背景
                if (selectedBackgroundDrawable != null) {
                    RectF rectF = new RectF();
                    rectF.left = getPaddingLeft();
                    rectF.top = itemHeight * i + offsetTop;
                    rectF.right = rectF.left + itemWidth;
                    rectF.bottom = rectF.top + itemHeight;
                    canvas.saveLayer(rectF, null, ALL_SAVE_FLAG);
                    selectedBackgroundDrawable.setBounds((int) (rectF.left), (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                    selectedBackgroundDrawable.draw(canvas);
                    canvas.restore();
                } else {
                    canvas.drawCircle(itemWidth / 2, itemHeight * i + itemHeight / 2 + offsetTop, itemWidth / 2, bgPaint);
                }
            }
            wordsPaint.setColor(isSelected ? selectedTextColor : unselectedTextColor);
            //获取文字高度
            Rect rect = new Rect();
            String word = indexBeanList.get(i).getIndexValue();
            wordsPaint.getTextBounds(word, 0, 1, rect);
            int wordHeight = rect.height();
            //绘制字母
            float wordX = itemWidth / 2;
            float wordY = wordHeight / 2 + itemHeight * i + itemHeight / 2 + offsetTop;
            canvas.drawText(word, wordX, wordY, wordsPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                final int index = (int) ((y - offsetTop) / itemHeight);
                if (index >= 0 && index < indexBeanList.size()) {
                    if (index != currentSelectIndex) {
                        currentSelectIndex = index;
                        invalidate();
                    }
                    notifyOnItemSelected(currentSelectIndex, indexBeanList.get(currentSelectIndex));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //do nothing
                break;
        }
        return true;
    }

    public void setData(ArrayList<? extends IndexBean> data) {
        indexBeanList.clear();
        indexBeanList.addAll(data);
        setRealHeight();
        invalidate();
        //默认选中
        selectFirstIndex();
    }

    public void addAll(ArrayList<IndexBean> data) {
        indexBeanList.addAll(data);
        setRealHeight();
        invalidate();
        //默认选中
        selectFirstIndex();
    }

    /** 默认选中 */
    private void selectFirstIndex() {
        if (currentSelectIndex == 0 && !indexBeanList.isEmpty()) {
            notifyOnItemSelected(currentSelectIndex, indexBeanList.get(currentSelectIndex));
        }
    }

    public void setSelectPosition(int position) {
        if (position >= 0 && position < indexBeanList.size()
                && !indexBeanList.isEmpty()
                && currentSelectIndex != position) {
            this.currentSelectIndex = position;
            invalidate();
            //双向联动有问题
//            notifyOnItemSelected(currentSelectIndex,indexBeanList.get(currentSelectIndex));
        }
    }

    public boolean addOnItemSelectedListener(OnItemSelectedListener listener) {
        return listener != null && !onItemSelectedListeners.contains(listener) &&
                onItemSelectedListeners.add(listener);
    }

    public boolean removeOnItemSelectedListener(OnItemSelectedListener listener) {
        return listener != null && onItemSelectedListeners.remove(listener);
    }

    public void removeAllOnItemSelectedListener() {
        onItemSelectedListeners.clear();
    }

    private void notifyOnItemSelected(int position, IndexBean bean) {
        for (OnItemSelectedListener listener : onItemSelectedListeners) {
            if (listener != null) {
                listener.onItemSelected(position, bean);
            }
        }
    }

    /** 选中监听 */
    public interface OnItemSelectedListener {
        public void onItemSelected(int position, IndexBean bean);
    }

    /** 索引Bean */
    public static class IndexBean {
        String type;
        final String indexValue;

        public IndexBean(@NonNull String indexValue) {
            this.indexValue = indexValue;
        }

        public String getIndexValue() {
            return TextUtils.isEmpty(indexValue) ? "" : indexValue;
        }
    }
}
