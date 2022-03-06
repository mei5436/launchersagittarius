package com.toptech.launcher.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.toptech.launcher.adapter.BaseAnimAdapter;
import com.toptech.launcher.ui.MainUpView;

public class OpenBaseAnimAdapter extends BaseAnimAdapter {
    private static final int DEFUALT_TRAN_DUR_ANIM = 300;
    private boolean isDrawUpRect = true;
    private boolean isInDraw = false;
    private boolean mAnimEnabled = true;
    private AnimatorSet mCurrentAnimatorSet;
    private View mFocusView;
    private boolean mIsHide = false;
    private NewAnimatorListener mNewAnimatorListener;
    private int mTranDurAnimTime = DEFUALT_TRAN_DUR_ANIM;

    public interface NewAnimatorListener {
        void onAnimationEnd(View view, Animator animator);

        void onAnimationStart(View view, Animator animator);
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public void onInitAdapter(MainUpView view) {
        view.setVisibility(View.INVISIBLE);
    }

    public void setDrawUpRectEnabled(boolean isDrawUpRect2) {
        this.isDrawUpRect = isDrawUpRect2;
        getMainUpView().invalidate();
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
        getMainUpView().setVisibility(this.mIsHide ? 8 : 0);
    }

    public void setOnAnimatorListener(NewAnimatorListener newAnimatorListener) {
        this.mNewAnimatorListener = newAnimatorListener;
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (this.mAnimEnabled && oldFocusView != null) {
            oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration((long) this.mTranDurAnimTime).start();
        }
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        this.mFocusView = focusView;
        if (this.mAnimEnabled && focusView != null) {
            if (!this.mIsHide) {
                focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration((long) this.mTranDurAnimTime).start();
            }
            runTranslateAnimation(focusView, scaleX, scaleY);
        }
    }

    @Override // com.toptech.launcher.adapter.BaseAnimAdapter
    public void flyWhiteBorder(final View focusView, float x, float y, float scaleX, float scaleY) {
        int newWidth = 0;
        int newHeight = 0;
        if (focusView != null) {
            if (focusView.getLayoutParams().width <= 0 || focusView.getLayoutParams().height <= 0) {
                newWidth = (int) (((float) focusView.getWidth()) * scaleX);
                newHeight = (int) (((float) focusView.getHeight()) * scaleY);
            } else {
                newWidth = (int) (((float) focusView.getLayoutParams().width) * scaleX);
                newHeight = (int) (((float) focusView.getLayoutParams().height) * scaleY);
            }
            x += (float) ((focusView.getWidth() - newWidth) / 2);
            y += (float) ((focusView.getHeight() - newHeight) / 2);
        }
        Log.d("sch", "x/y  " + x + "/" + y + " w/h " + newWidth + "/" + newHeight);
        if (this.mCurrentAnimatorSet != null) {
            this.mCurrentAnimatorSet.cancel();
        }
        int oldWidth = getMainUpView().getLayoutParams().width;
        int oldHeight = getMainUpView().getLayoutParams().height;
        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMainUpView(), "translationX", getMainUpView().getX(), x);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMainUpView(), "translationY", getMainUpView().getY(), y);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new BaseAnimAdapter.ScaleView(getMainUpView()), "width", oldWidth, newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new BaseAnimAdapter.ScaleView(getMainUpView()), "height", oldHeight, newHeight);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(2.0f));
        mAnimatorSet.setDuration((long) this.mTranDurAnimTime);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            /* class com.toptech.launcher.adapter.OpenBaseAnimAdapter.AnonymousClass1 */

            public void onAnimationStart(Animator animation) {
                if (!OpenBaseAnimAdapter.this.isDrawUpRect) {
                    OpenBaseAnimAdapter.this.isInDraw = false;
                }
                if (OpenBaseAnimAdapter.this.mIsHide) {
                    OpenBaseAnimAdapter.this.getMainUpView().setVisibility(View.GONE);
                }
                if (OpenBaseAnimAdapter.this.mNewAnimatorListener != null) {
                    OpenBaseAnimAdapter.this.mNewAnimatorListener.onAnimationStart(focusView, animation);
                }
            }

            public void onAnimationRepeat(Animator animation) {
                if (!OpenBaseAnimAdapter.this.isDrawUpRect) {
                    OpenBaseAnimAdapter.this.isInDraw = false;
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (!OpenBaseAnimAdapter.this.isDrawUpRect) {
                    OpenBaseAnimAdapter.this.isInDraw = true;
                }
                OpenBaseAnimAdapter.this.getMainUpView().setVisibility(OpenBaseAnimAdapter.this.mIsHide ? 8 : 0);
                if (OpenBaseAnimAdapter.this.mNewAnimatorListener != null) {
                    OpenBaseAnimAdapter.this.mNewAnimatorListener.onAnimationEnd(focusView, animation);
                }
            }

            public void onAnimationCancel(Animator animation) {
                if (!OpenBaseAnimAdapter.this.isDrawUpRect) {
                    OpenBaseAnimAdapter.this.isInDraw = false;
                }
            }
        });
        mAnimatorSet.start();
        this.mCurrentAnimatorSet = mAnimatorSet;
    }

    @Override // com.toptech.launcher.adapter.IAnimAdapter, com.toptech.launcher.adapter.BaseAnimAdapter
    public boolean onDrawMainUpView(Canvas canvas) {
        canvas.save();
        if (!this.isDrawUpRect) {
            onDrawShadow(canvas);
            onDrawUpRect(canvas);
        }
        if (this.mFocusView != null && !this.isDrawUpRect && this.isInDraw) {
            onDrawFocusView(canvas);
        }
        if (this.isDrawUpRect) {
            onDrawShadow(canvas);
            onDrawUpRect(canvas);
        }
        canvas.restore();
        return true;
    }

    public void onDrawFocusView(Canvas canvas) {
        View view = this.mFocusView;
        canvas.save();
        canvas.scale(((float) getMainUpView().getWidth()) / ((float) view.getWidth()), ((float) getMainUpView().getHeight()) / ((float) view.getHeight()));
        view.draw(canvas);
        canvas.restore();
    }
}
