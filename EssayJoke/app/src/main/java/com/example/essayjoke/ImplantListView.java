package com.example.essayjoke;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @Author Q
 * @Date 2020/7/21 3:55 PM
 */
public class ImplantListView extends ListView {
    public ImplantListView(Context context) {
        super(context);
    }

    public ImplantListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImplantListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        嵌套ListView内容显示不全
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
