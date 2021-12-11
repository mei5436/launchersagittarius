package com.toptech.launcher.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.widget.HorizontalScrollView;
import com.toptech.launchersagittarius.R;

public class SmoothHorizontalScrollView extends HorizontalScrollView {
    final String TAG;

    public SmoothHorizontalScrollView(Context context) {
        this(context, null, 0);
    }

    public SmoothHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = "SmoothHorizontalScrollView";
    }

    /* access modifiers changed from: protected */
    public int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        int scrollXDelta;
        int scrollXDelta2;
        if (getChildCount() == 0) {
            return 0;
        }
        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
        int fadingEdge = getResources().getDimensionPixelSize(R.dimen.fading_edge);
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }
        if (rect.right > screenRight && rect.left > screenLeft) {
            if (rect.width() > width) {
                scrollXDelta2 = 0 + (rect.left - screenLeft);
            } else {
                scrollXDelta2 = 0 + (rect.right - screenRight);
            }
            return Math.min(scrollXDelta2, getChildAt(0).getRight() - screenRight);
        } else if (rect.left >= screenLeft || rect.right >= screenRight) {
            return 0;
        } else {
            if (rect.width() > width) {
                scrollXDelta = 0 - (screenRight - rect.right);
            } else {
                scrollXDelta = 0 - (screenLeft - rect.left);
            }
            return Math.max(scrollXDelta, -getScrollX());
        }
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        if (previouslyFocusedRect != null) {
            if (direction == 2) {
                direction = 66;
            } else if (direction == 1) {
                direction = 17;
            }
            View nextFocus = FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
            if (nextFocus == null) {
                return false;
            }
            return nextFocus.requestFocus(direction, previouslyFocusedRect);
        }
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0 && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }
}
