package com.toptech.launcher.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.toptech.launchersagittarius.R;

public class ReflectItemView extends FrameLayout {
    private static final int REFHEIGHT = 80;
    private static final String TAG = "ReflectItemView";
    private View mContentView;
    private Context mContext;
    private int mHeight;
    private String mInfo;
    private boolean mIsReflection = true;
    private Paint mRefPaint = null;
    private Bitmap mReflectBitmap;
    private Canvas mReflectCanvas;
    private TextView mTextView;
    private int mWidth;

    public ReflectItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init(context, attrs);
    }

    public ReflectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
    }

    public ReflectItemView(Context context) {
        super(context);
        this.mContext = context;
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (this.mInfo != null) {
            this.mTextView.setText(this.mInfo);
        }
        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.reflectItemView);
            setReflection(tArray.getBoolean(0, false));
            this.mInfo = tArray.getString(1);
        }
        if (this.mRefPaint == null) {
            this.mRefPaint = new Paint(1);
            this.mRefPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, 80.0f, new int[]{1996488704, 1722460842, 5242880, 0}, new float[]{0.0f, 0.1f, 0.9f, 1.0f}, Shader.TileMode.CLAMP));
            this.mRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        }
        setClipChildren(false);
        setClipToPadding(false);
        setWillNotDraw(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            this.mContentView = getChildAt(0);
        }
    }

    public void addView(View child) {
        this.mContentView = child;
        super.addView(child);
    }

    public View getContentView() {
        return this.mContentView;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
    }

    public void setReflection(boolean ref) {
        this.mIsReflection = ref;
        invalidate();
    }

    public boolean isReflection() {
        return this.mIsReflection;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mIsReflection && this.mContentView != null && this.mContentView.getWidth() > 0) {
            if (this.mReflectBitmap == null) {
                this.mReflectBitmap = Bitmap.createBitmap(this.mContentView.getWidth(), REFHEIGHT, Bitmap.Config.ARGB_8888);
                this.mReflectCanvas = new Canvas(this.mReflectBitmap);
            }
            drawReflection(this.mReflectCanvas);
            canvas.save();
            canvas.translate((float) this.mContentView.getLeft(), (float) this.mContentView.getBottom());
            canvas.drawBitmap(this.mReflectBitmap, 0.0f, 0.0f, (Paint) null);
            canvas.restore();
        }
    }

    public Bitmap getReflectBitmap() {
        return this.mReflectBitmap;
    }

    public void drawReflection(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, 0, this.mContentView.getWidth(), REFHEIGHT);
        canvas.save();
        canvas.scale(1.0f, -1.0f);
        canvas.translate(0.0f, (float) (-this.mContentView.getHeight()));
        this.mContentView.draw(canvas);
        canvas.restore();
        canvas.drawRect(0.0f, 0.0f, (float) this.mContentView.getWidth(), 80.0f, this.mRefPaint);
        canvas.restore();
    }
}
