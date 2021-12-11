package com.toptech.launchersagittarius.tv;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.toptech.AudioManagerEX;
import com.toptech.launcher.ShareData;
import com.toptech.launchersagittarius.SagittariusActivity;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;

public class TVView extends SurfaceView implements SurfaceHolder.Callback {
    private final int CLOSE_TV;
    private final int SCALE_WINDOW;
    private final int SHOW_TV;
    private static String TAG = "TVView";
    private Boolean bSyncC;
    private Boolean bSyncT;
    InputSourceThread inputSourceThread;
    private Boolean isSetStorageInput;
    private Boolean isSetTVInput;
    private AudioManager mAudioManager;
    private boolean mConditionChanged;
    private Context mContext;
    private boolean mCreateFromLayout;
    private boolean mExpectState;
    private ChangeTVStatus mHandler;
    private OnCheckSignalListener mOnCheckSignalListener;
    private boolean mRealState;
    private SurfaceHolder mSurfaceHolder;
    private MuteType mSystemMute;
    private TvCecManager mTvCecManager;
    private WindowManager mWM;
    private int mWindowHeight;
    private int mWindowLeft;
    private int mWindowTop;
    private int mWindowWidth;
    private int preInputSource;
    private int saveInputSource;
    private WindowManager.LayoutParams surfaceParams;

    public enum MuteType {
        MuteType_FRIST,
        MuteType_MUTE,
        MuteType_UNMUTE
    }

    public interface OnCheckSignalListener {
        void postRunnable();

        void removeCallback();
    }

    /* access modifiers changed from: private */
    public static class ChangeTVStatus extends Handler {
        WeakReference<TVView> mWeak;

        public ChangeTVStatus(TVView tvView) {
            this.mWeak = new WeakReference<>(tvView);
        }

        public void handleMessage(Message msg) {
            TVView mTVView = this.mWeak.get();
            if (mTVView != null) {
                Log.d(mTVView.TAG, "msg.what====" + msg.what);
                if (msg.what == 10002) {
                    Log.d(mTVView.TAG, "message --- > close");
                    mTVView.close();
                } else if (msg.what == 10001) {
                    Log.d(mTVView.TAG, "message --- > open");
                    mTVView.open();
                } else if (msg.what == 10003) {
                    mTVView.scaleSmallWindow(false);
                    if (mTVView.mOnCheckSignalListener != null) {
                        mTVView.mOnCheckSignalListener.postRunnable();
                    }
                }
                super.handleMessage(msg);
            }
        }
    }

    public TVView(Context context) {
        super(context);
        this.SHOW_TV = 10001;
        this.CLOSE_TV = 10002;
        this.SCALE_WINDOW = 10003;
        this.TAG = TVView.class.getSimpleName();
        this.bSyncT = false;
        this.bSyncC = false;
        this.isSetStorageInput = false;
        this.isSetTVInput = true;
        this.mConditionChanged = false;
        this.mContext = null;
        this.mCreateFromLayout = false;
        this.mExpectState = false;
        this.mRealState = true;
        this.mSurfaceHolder = null;
        this.mWM = null;
        this.mWindowHeight = 0;
        this.mWindowLeft = 0;
        this.mWindowTop = 0;
        this.mWindowWidth = 0;
        this.surfaceParams = null;
        this.saveInputSource = 44;
        this.preInputSource = 44;
        this.mAudioManager = null;
        this.mTvCecManager = null;
        this.mSystemMute = MuteType.MuteType_FRIST;
        this.mCreateFromLayout = true;
        this.mContext = context;
        Log.d(this.TAG, "tv view 1 params");
    }

    public TVView(Context context, int left, int top, int width, int height) {
        super(context);
        this.SHOW_TV = 10001;
        this.CLOSE_TV = 10002;
        this.SCALE_WINDOW = 10003;
        this.TAG = TVView.class.getSimpleName();
        this.bSyncT = false;
        this.bSyncC = false;
        this.isSetStorageInput = false;
        this.isSetTVInput = true;
        this.mConditionChanged = false;
        this.mContext = null;
        this.mCreateFromLayout = false;
        this.mExpectState = false;
        this.mRealState = true;
        this.mSurfaceHolder = null;
        this.mWM = null;
        this.mWindowHeight = 0;
        this.mWindowLeft = 0;
        this.mWindowTop = 0;
        this.mWindowWidth = 0;
        this.surfaceParams = null;
        this.saveInputSource = 44;
        this.preInputSource = 44;
        this.mAudioManager = null;
        this.mTvCecManager = null;
        this.mSystemMute = MuteType.MuteType_FRIST;
        this.mWindowTop = top;
        this.mWindowLeft = left;
        this.mWindowWidth = width;
        this.mWindowHeight = height;
        Log.d(this.TAG, "xx widows x,y,w,h===" + this.mWindowLeft + "," + this.mWindowTop + "," + this.mWindowWidth + "," + this.mWindowHeight);
        this.mCreateFromLayout = false;
        this.mContext = context;
        init();
        Log.d(this.TAG, "tv view 5 params");
    }

    public TVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.SHOW_TV = 10001;
        this.CLOSE_TV = 10002;
        this.SCALE_WINDOW = 10003;
        this.TAG = TVView.class.getSimpleName();
        this.bSyncT = false;
        this.bSyncC = false;
        this.isSetStorageInput = false;
        this.isSetTVInput = true;
        this.mConditionChanged = false;
        this.mContext = null;
        this.mCreateFromLayout = false;
        this.mExpectState = false;
        this.mRealState = true;
        this.mSurfaceHolder = null;
        this.mWM = null;
        this.mWindowHeight = 0;
        this.mWindowLeft = 0;
        this.mWindowTop = 0;
        this.mWindowWidth = 0;
        this.surfaceParams = null;
        this.saveInputSource = 44;
        this.preInputSource = 44;
        this.mAudioManager = null;
        this.mTvCecManager = null;
        this.mSystemMute = MuteType.MuteType_FRIST;
        this.mCreateFromLayout = true;
        this.mContext = context;
        this.mRealState = false;
        this.mExpectState = true;
        this.mConditionChanged = false;
        this.mWindowTop = 0;
        this.mWindowLeft = 0;
        this.mWindowWidth = 0;
        this.mWindowHeight = 0;
        this.mSurfaceHolder = null;
        this.surfaceParams = null;
        this.isSetTVInput = false;
        this.isSetStorageInput = false;
        this.bSyncT = false;
        this.bSyncC = false;
        init();
        Log.d(this.TAG, "tv view 2 params");
    }

    public TVView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.SHOW_TV = 10001;
        this.CLOSE_TV = 10002;
        this.SCALE_WINDOW = 10003;
        this.TAG = TVView.class.getSimpleName();
        this.bSyncT = false;
        this.bSyncC = false;
        this.isSetStorageInput = false;
        this.isSetTVInput = true;
        this.mConditionChanged = false;
        this.mContext = null;
        this.mCreateFromLayout = false;
        this.mExpectState = false;
        this.mRealState = true;
        this.mSurfaceHolder = null;
        this.mWM = null;
        this.mWindowHeight = 0;
        this.mWindowLeft = 0;
        this.mWindowTop = 0;
        this.mWindowWidth = 0;
        this.surfaceParams = null;
        this.saveInputSource = 44;
        this.preInputSource = 44;
        this.mAudioManager = null;
        this.mTvCecManager = null;
        this.mSystemMute = MuteType.MuteType_FRIST;
        this.mCreateFromLayout = true;
        this.mContext = context;
        init();
        Log.d(this.TAG, "tv view 5 params");
    }

    public void checkView() {
        if (TvCommonManager.getInstance().getCurrentTvInputSource() != 34) {
            this.mExpectState = true;
            this.mRealState = true;
            this.saveInputSource = 44;
            this.preInputSource = 44;
            return;
        }
        this.mExpectState = false;
        this.mRealState = false;
    }

    public void close() {
        Log.d(this.TAG, "tv view close");
        if (!this.mRealState || this.mRealState == this.mExpectState) {
            Log.w(this.TAG, "the window is already closed mRealState: " + this.mRealState + " mRealState: " + this.mRealState + " mExpectState: " + this.mExpectState);
            return;
        }
        this.mRealState = false;
        if (this.mCreateFromLayout) {
            setTVshow(false);
            Log.d(this.TAG, "***********close tv view*****************");
            setVisibility(4);
            return;
        }
        this.mWM.removeViewImmediate(this);
        Log.d(this.TAG, "rm view ++++++++++++");
    }

    public void open() {
        Log.d(this.TAG, "tv view open");
        if (this.mRealState || this.mRealState == this.mExpectState) {
            Log.w(this.TAG, "the window is already open mRealState: " + this.mRealState + " mRealState: " + this.mRealState + " mExpectState: " + this.mExpectState);
            return;
        }
        this.mRealState = true;
        if (this.mCreateFromLayout) {
            setTVshow(true);
            Log.d(this.TAG, "***********open tv view*****************");
            setVisibility(0);
            return;
        }
        this.mWM.addView(this, this.surfaceParams);
        Log.d(this.TAG, "add view ++++++++++++");
    }

    private void setTVshow(boolean parem) {
        if (parem) {
            setTVInput();
        } else {
            setStorageInput();
        }
    }

    public void setSystemMute(MuteType mType) {
        this.mSystemMute = mType;
    }

    public void setMuteVideo(boolean parem) {
        int tmp;
        synchronized (this.bSyncT) {
            tmp = TvCommonManager.getInstance().getCurrentTvInputSource();
        }
        if (parem) {
            if (TvCommonManager.getInstance() != null && tmp != 44) {
                if (!AudioManagerEX.isMasterMute(this.mAudioManager)) {
                    this.mSystemMute = MuteType.MuteType_UNMUTE;
                    AudioManagerEX.setMasterMute(this.mAudioManager,true, 8);
                    this.mTvCecManager.sendCecKey(164);
                } else {
                    this.mSystemMute = MuteType.MuteType_MUTE;
                }
                Log.d(this.TAG, "setMuteVideo...." + tmp + "....by hide " + false);
            }
        } else if (TvCommonManager.getInstance() != null) {
            Log.d(this.TAG, "setMuteVideo...." + tmp + "....by show " + TvCommonManager.getInstance().setVideoMute(false, 0, 0, tmp));
            if (this.mSystemMute != MuteType.MuteType_MUTE) {
                AudioManagerEX.setMasterMute(this.mAudioManager,false, 8);
                this.mTvCecManager.sendCecKey(164);
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mHandler.sendEmptyMessageDelayed(10003, 500);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (holder.getSurface().isValid()) {
            int[] iArr = new int[2];
            getLocationOnScreen(iArr);
            if (iArr[0] >= 0 && iArr[1] >= 0 && iArr[0] < 500 && iArr[1] < 500) {
                this.mWindowLeft = iArr[0];
                this.mWindowTop = iArr[1];
            }
            this.mWindowWidth = getWidth();
            this.mWindowHeight = getHeight();
            Log.d(this.TAG, "*****surfaceCreated!!!!!!!!");
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getPlayerManager().setDisplay(this.mSurfaceHolder);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
                Log.e(this.TAG, "surfaceCreated error!!!!!!!");
            }
            setBackgroundColor(0);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(this.TAG, "surfaceDestroyed");
        this.mHandler.removeMessages(10003);
        try {
            TvCommonManager.getInstance().closeSurfaceView();
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(this.TAG, "error..............destory");
        }
        if (this.mOnCheckSignalListener != null) {
            this.mOnCheckSignalListener.removeCallback();
        }
    }

    public void init() {
        Log.d(this.TAG, "mCreateFromLayout===" + this.mCreateFromLayout);
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mTvCecManager = TvCecManager.getInstance();
        this.mHandler = new ChangeTVStatus(this);
        if (this.mCreateFromLayout) {
            initVideoView();
        } else {
            initTVWindow();
        }
    }

    private void initTVWindow() {
        if (this.surfaceParams == null) {
            this.surfaceParams = new WindowManager.LayoutParams();
            this.surfaceParams.x = this.mWindowLeft;
            this.surfaceParams.y = this.mWindowTop;
            this.surfaceParams.width = this.mWindowWidth;
            this.surfaceParams.height = this.mWindowHeight;
            this.surfaceParams.type = 2;
            this.surfaceParams.flags = 24;
            Log.v(this.TAG, "openSurfaceView===" + this.surfaceParams);
            this.surfaceParams.gravity = 51;
            this.mWM = (WindowManager) this.mContext.getSystemService("window");
        }
        initVideoView();
    }

    private void initVideoView() {
        if (this.mSurfaceHolder == null) {
            Log.d(this.TAG, "mSurfaceHolder.addCallback");
            this.mSurfaceHolder = getHolder();
            this.mSurfaceHolder.addCallback(this);
            this.mSurfaceHolder.setType(3);
        }
    }

    public void setStorageInput() {
        synchronized (this) {
            this.isSetStorageInput = true;
        }
    }

    public void setTVInput() {
        synchronized (this) {
            this.isSetTVInput = true;
        }
    }

    public void resetState() {
        this.mConditionChanged = true;
        this.mExpectState = true;
        this.mRealState = false;
    }

    public synchronized void updateState() {
        Log.d(this.TAG, "update state====" + this.mExpectState);
        if (this.mExpectState) {
            this.mHandler.sendEmptyMessage(10001);
        } else {
            this.mHandler.sendEmptyMessage(10002);
        }
    }

    public synchronized void setState(boolean paramBoolean) {
        this.mConditionChanged = true;
        this.mExpectState = paramBoolean;
        notifyAll();
    }

    public synchronized void waitForStateChange() throws InterruptedException {
        while (true) {
            if (!this.mConditionChanged || this.mExpectState == this.mRealState) {
                wait();
            } else {
                this.mConditionChanged = false;
            }
        }
    }

    public boolean getState() {
        return this.mRealState;
    }

    public boolean scaleSmallWindow(boolean flag) {
        int[] iArr = new int[2];
        int w2tmp = 0;
        Log.d(this.TAG, "scaleSmallWindow");
        Log.d(this.TAG, "thread scaleSmallWindow ==================:" + is1280x720(this.mContext));
        VideoWindowType videoWindowType = new VideoWindowType();
        if (SagittariusActivity.tvM_wWidth == SagittariusActivity.tvOsdWidth) {
            videoWindowType.x = this.mWindowLeft;
            videoWindowType.y = this.mWindowTop;
            videoWindowType.width = getMeasuredWidth();
            videoWindowType.height = getMeasuredHeight();
        } else {
            float hScale = ((float) SagittariusActivity.tvM_wHeight) / ((float) SagittariusActivity.tvOsdHeight);
            float mScale = ((float) SagittariusActivity.tvM_wWidth) / ((float) SagittariusActivity.tvOsdWidth);
            float hScale2 = new BigDecimal((double) hScale).setScale(2, 4).floatValue();
            float mScale2 = new BigDecimal((double) mScale).setScale(2, 4).floatValue();
            videoWindowType.x = (int) (((float) this.mWindowLeft) * hScale2);
            videoWindowType.y = (int) (((float) this.mWindowTop) * mScale2);
            videoWindowType.width = (int) (((float) getMeasuredWidth()) * mScale2);
            videoWindowType.height = (int) (((float) getMeasuredHeight()) * hScale2);
        }
        if (flag || videoWindowType.x <= 0 || videoWindowType.y <= 0 || videoWindowType.width < 0 || videoWindowType.height < 0) {
            getLocationOnScreen(iArr);
            if (iArr[0] < 0) {
                w2tmp = iArr[0];
                iArr[0] = 0;
            }
            Log.d(this.TAG, "iArr: " + iArr[0] + "," + iArr[1]);
            this.mWindowLeft = iArr[0];
            this.mWindowTop = iArr[1];
            this.mWindowWidth = getWidth() + w2tmp;
            this.mWindowHeight = getHeight();
            if (SagittariusActivity.tvM_wWidth == SagittariusActivity.tvOsdWidth) {
                videoWindowType.x = this.mWindowLeft;
                videoWindowType.y = this.mWindowTop;
                videoWindowType.width = getMeasuredWidth();
                videoWindowType.height = getMeasuredHeight();
            } else {
                float hScale3 = ((float) SagittariusActivity.tvM_wHeight) / ((float) SagittariusActivity.tvOsdHeight);
                float mScale3 = ((float) SagittariusActivity.tvM_wWidth) / ((float) SagittariusActivity.tvOsdWidth);
                float hScale4 = new BigDecimal((double) hScale3).setScale(2, 4).floatValue();
                float mScale4 = new BigDecimal((double) mScale3).setScale(2, 4).floatValue();
                videoWindowType.x = (int) (((float) this.mWindowLeft) * hScale4);
                videoWindowType.y = (int) (((float) this.mWindowTop) * mScale4);
                videoWindowType.width = (int) (((float) getMeasuredWidth()) * mScale4);
                videoWindowType.height = (int) (((float) getMeasuredHeight()) * hScale4);
            }
        }
        Log.d(this.TAG, "[the TVSmallWindow size() is  [x,y][w,h] =====[" + videoWindowType.x + "," + videoWindowType.y + "][" + videoWindowType.width + "," + videoWindowType.height + "]");
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
                TvManager.getInstance().getPictureManager().setDisplayWindow(videoWindowType);
                TvManager.getInstance().getPictureManager().scaleWindow();
                return true;
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean scaleWindow() {
        VideoWindowType videoWindowType = new VideoWindowType();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        videoWindowType.x = 0;
        videoWindowType.y = 0;
        videoWindowType.width = dm.widthPixels;
        videoWindowType.height = dm.heightPixels;
        Log.d(this.TAG, "[the TVSmallWindow size() is  [x,y][w,h] =====[" + videoWindowType.x + "," + videoWindowType.y + "][" + videoWindowType.width + "," + videoWindowType.height + "]");
        try {
            if (TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null) {
                return false;
            }
            TvManager.getInstance().getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
            TvManager.getInstance().getPictureManager().setDisplayWindow(videoWindowType);
            TvManager.getInstance().getPictureManager().scaleWindow();
            return true;
        } catch (TvCommonException e) {
            e.printStackTrace();
            Log.e(this.TAG, "error ............scaleWindow");
            return false;
        }
    }

    public void stopInputSourceThread() {
        if (this.inputSourceThread != null) {
            this.inputSourceThread.bExitThread = true;
        }
    }

    public void startInputSourceThread() {
        stopInputSourceThread();
        this.inputSourceThread = new InputSourceThread();
        this.inputSourceThread.start();
    }

    public void setInputSource2TV() {
        int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        this.saveInputSource = this.mContext.getSharedPreferences("SETTING", 0).getInt("SAVED_INPUT_SOURCE", 1);
        if (curInputSource != this.preInputSource || this.saveInputSource != 44) {
            if (curInputSource == 34) {
                curInputSource = this.saveInputSource;
                this.saveInputSource = 44;
            }
            this.preInputSource = curInputSource;
            TvCommonManager.getInstance().setInputSource(curInputSource);
            int curInputSource2 = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (curInputSource2 == 1) {
                int channel = TvChannelManager.getInstance().getCurrentChannelNumber();
                if (channel < 0 || channel > 255) {
                    channel = 0;
                }
                TvChannelManager.getInstance().setAtvChannel(channel);
            } else if (curInputSource2 == 28) {
                int channel2 = TvChannelManager.getInstance().getCurrentChannelNumber();
                if (channel2 < 0) {
                    channel2 = 0;
                }
                TvChannelManager.getInstance().selectProgram(channel2, 1);
            }
        }
    }

    public boolean is1280x720(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels == 1280 && displayMetrics.heightPixels == 720;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setInputSource2Storage() {
        this.saveInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (this.saveInputSource != 34) {
            int inputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            ShareData.getInstance().setmCurrentInput(inputSource);
            this.mContext.getSharedPreferences("SETTING", 0).edit().putInt("SAVED_INPUT_SOURCE", inputSource).commit();
            TvCommonManager.getInstance().setInputSource(34);
        }
        Log.d(this.TAG, "setInputSource2Storage saveInputSource: " + this.saveInputSource);
    }

    /* access modifiers changed from: package-private */
    public class InputSourceThread extends Thread {
        private boolean bExitThread = false;
        private boolean tempIsSetTVInput = false;
        private boolean tempIsStorageInput = false;

        InputSourceThread() {
        }

        public void run() {
            while (!this.bExitThread) {
                synchronized (TVView.this.bSyncT) {
                    this.tempIsSetTVInput = TVView.this.isSetTVInput.booleanValue();
                    TVView.this.isSetTVInput = false;
                    this.tempIsStorageInput = TVView.this.isSetStorageInput.booleanValue();
                    TVView.this.isSetStorageInput = false;
                }
                if (this.tempIsSetTVInput) {
                    TVView.this.setInputSource2TV();
                    TVView.this.scaleSmallWindow(false);
                } else if (this.tempIsStorageInput) {
                    TVView.this.setInputSource2Storage();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnCheckSignalListener(OnCheckSignalListener mOnCheckSignalListener2) {
        this.mOnCheckSignalListener = mOnCheckSignalListener2;
    }
}
