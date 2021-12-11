package com.toptech.launcher.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import com.toptech.launcher.ui.MainUpView;

public abstract class BaseAnimAdapter implements IAnimAdapter {
    private static final int DEFUALT_TRAN_DUR_ANIM = 300;
    private MainUpView mMainUpView;

    @Override // com.toptech.launcher.adapter.IAnimAdapter
    public void onInitAdapter(MainUpView view) {
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter
    public boolean onDrawMainUpView(Canvas canvas) {
        canvas.save();
        onDrawShadow(canvas);
        onDrawUpRect(canvas);
        canvas.restore();
        return true;
    }

    public void onDrawShadow(Canvas canvas) {
        Drawable drawableShadow = getMainUpView().getShadowDrawable();
        if (drawableShadow != null) {
            Rect shadowPaddingRect = getMainUpView().getDrawShadowRect();
            int width = getMainUpView().getWidth();
            int height = getMainUpView().getHeight();
            Rect padding = new Rect();
            drawableShadow.getPadding(padding);
            drawableShadow.setBounds((-padding.left) + shadowPaddingRect.left, (-padding.top) + shadowPaddingRect.top, (padding.right + width) - shadowPaddingRect.right, (padding.bottom + height) - shadowPaddingRect.bottom);
            drawableShadow.draw(canvas);
        }
    }

    public void onDrawUpRect(Canvas canvas) {
        Drawable drawableUp = getMainUpView().getUpRectDrawable();
        if (drawableUp != null) {
            Rect paddingRect = getMainUpView().getDrawUpRect();
            int width = getMainUpView().getWidth();
            int height = getMainUpView().getHeight();
            Rect padding = new Rect();
            drawableUp.getPadding(padding);
            drawableUp.setBounds((-padding.left) + paddingRect.left, (-padding.top) + paddingRect.top, (padding.right + width) - paddingRect.right, (padding.bottom + height) - paddingRect.bottom);
            drawableUp.draw(canvas);
        }
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (oldFocusView != null) {
            oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(300).start();
        }
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        if (focusView != null) {
            focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(300).start();
            runTranslateAnimation(focusView, scaleX, scaleY);
        }
    }

    public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
        Rect fromRect = findLocationWithView(getMainUpView());
        Rect toRect = findLocationWithView(toView);
        flyWhiteBorder(toView, (float) (toRect.left - fromRect.left), (float) (toRect.top - fromRect.top), scaleX, scaleY);
    }

    public Rect findLocationWithView(View view) {
        Rect rect = new Rect();
        ((ViewGroup) getMainUpView().getParent()).offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    public void flyWhiteBorder(View focusView, float x, float y, float scaleX, float scaleY) {
        int newWidth = 0;
        int newHeight = 0;
        if (focusView != null) {
            newWidth = (int) (((float) focusView.getMeasuredWidth()) * scaleX);
            newHeight = (int) (((float) focusView.getMeasuredHeight()) * scaleY);
            x += (float) ((focusView.getMeasuredWidth() - newWidth) / 2);
            y += (float) ((focusView.getMeasuredHeight() - newHeight) / 2);
        }
        int oldWidth = getMainUpView().getMeasuredWidth();
        int oldHeight = getMainUpView().getMeasuredHeight();
        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMainUpView(), "translationX", x);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMainUpView(), "translationY", y);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "width", oldWidth, newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "height", oldHeight, newHeight);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1.0f));
        mAnimatorSet.setDuration(300L);
        mAnimatorSet.start();
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter
    public void setMainUpView(MainUpView view) {
        this.mMainUpView = view;
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter
    public MainUpView getMainUpView() {
        return this.mMainUpView;
    }

    public class ScaleView {
        private int height;
        private View view;
        private int width;

        public ScaleView(View view2) {
            this.view = view2;
        }

        public int getWidth() {
            return this.view.getLayoutParams().width;
        }

        public void setWidth(int width2) {
            this.width = width2;
            this.view.getLayoutParams().width = width2;
            this.view.requestLayout();
        }

        public int getHeight() {
            return this.view.getLayoutParams().height;
        }

        public void setHeight(int height2) {
            this.height = height2;
            this.view.getLayoutParams().height = height2;
            this.view.requestLayout();
        }
    }
}
