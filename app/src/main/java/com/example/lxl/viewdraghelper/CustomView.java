package com.example.lxl.viewdraghelper;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lxl on 2015/10/31.
 */
public class CustomView  extends LinearLayout{
    private ViewDragHelper viewDragHelper;
    private View normalView;
    private View comeBackView;
    private View edgeView;
    private Point mAutoBackOriginPos = new Point();
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper=ViewDragHelper.create(this, 1.0f,
                new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View view, int i) {
                        return view==normalView||view==comeBackView;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {
                        return left;
                    }

                    @Override
                    public int clampViewPositionVertical(View child, int top, int dy) {
                        return top;
                    }

                    @Override
                    public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                        viewDragHelper.captureChildView(edgeView,pointerId);
                    }

                    @Override
                    public void onViewReleased(View releasedChild, float xvel, float yvel) {
                        if (releasedChild==comeBackView){
                            viewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                            invalidate();
                        }
                        super.onViewReleased(releasedChild, xvel, yvel);
                    }
                });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAutoBackOriginPos.set(comeBackView.getLeft(),comeBackView.getTop());
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        normalView=getChildAt(0);
        comeBackView=getChildAt(1);
        edgeView=getChildAt(2);
    }
}
