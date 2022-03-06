package com.toptech.launcher.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.toptech.launcher.adapter.BaseAnimAdapter;
import com.toptech.launcher.ui.MainUpView;

public class AnimNoDrawAdapter extends BaseAnimAdapter {
    private static final int DEFUALT_TRAN_DUR_ANIM = 300;
    private boolean mAnimEnabled = true;
    private AnimatorSet mCurrentAnimatorSet;
    private boolean mIsHide = false;
    private int mTranDurAnimTime = DEFUALT_TRAN_DUR_ANIM;

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public void onInitAdapter(MainUpView view) {
        view.setVisibility(View.INVISIBLE);
        view.setBackgroundDrawable(view.getUpRectDrawable());
    }

    public void setTranDurAnimTime(int time) {
        this.mTranDurAnimTime = time;
        getMainUpView().invalidate();
    }

    public void setAnimEnabled(boolean animEnabled) {
        this.mAnimEnabled = animEnabled;
    }

    public void setVisibleWidget(boolean isHide) {
        this.mIsHide = isHide;
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (this.mAnimEnabled && oldFocusView != null) {
            oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration((long) this.mTranDurAnimTime).start();
        }
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        if (this.mAnimEnabled && focusView != null) {
            if (!this.mIsHide) {
                focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration((long) this.mTranDurAnimTime).start();
            }
            runTranslateAnimation(focusView, scaleX, scaleY);
        }
    }

    @Override // com.toptech.launcher.adapter.BaseAnimAdapter
    public void flyWhiteBorder(View focusView, float x, float y, float scaleX, float scaleY) {
        Rect paddingRect = getMainUpView().getDrawUpRect();
        int newWidth = 0;
        int newHeight = 0;
        if (focusView != null) {
            newWidth = ((int) (((float) focusView.getMeasuredWidth()) * scaleX)) + paddingRect.left + paddingRect.right;
            newHeight = ((int) (((float) focusView.getMeasuredHeight()) * scaleY)) + paddingRect.top + paddingRect.bottom;
            x += (float) ((focusView.getMeasuredWidth() - newWidth) / 2);
            y += (float) ((focusView.getMeasuredHeight() - newHeight) / 2);
        }
        if (this.mCurrentAnimatorSet != null) {
            this.mCurrentAnimatorSet.cancel();
        }
        int oldWidth = getMainUpView().getMeasuredWidth();
        int oldHeight = getMainUpView().getMeasuredHeight();
        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMainUpView(), "translationX", x);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMainUpView(), "translationY", y);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new BaseAnimAdapter.ScaleView(getMainUpView()), "width", oldWidth, newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new BaseAnimAdapter.ScaleView(getMainUpView()), "height", oldHeight, newHeight);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1.0f));
        mAnimatorSet.setDuration((long) this.mTranDurAnimTime);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            /* class com.toptech.launcher.adapter.AnimNoDrawAdapter.AnonymousClass1 */

            public void onAnimationStart(Animator animation) {
                if (AnimNoDrawAdapter.this.mIsHide) {
                    AnimNoDrawAdapter.this.getMainUpView().setVisibility(View.GONE);
                }
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                AnimNoDrawAdapter.this.getMainUpView().setVisibility(AnimNoDrawAdapter.this.mIsHide ? 8 : 0);
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        mAnimatorSet.start();
        this.mCurrentAnimatorSet = mAnimatorSet;
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public boolean onDrawMainUpView(Canvas canvas) {
        return false;
    }
}
