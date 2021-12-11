package com.toptech.launchersagittarius;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.toptech.SystemProperties;
import com.toptech.launcher.PackageUpdateType;
import com.toptech.launcher.SagittariusTool;
import com.toptech.launcher.app.AllAppAdapter;
import com.toptech.launcher.app.AppItemViewHolder;
import com.toptech.launcher.app.PackageUtils;
import com.toptech.launcher.beans.CommendBean;
import com.toptech.launcher.data.LauncherDatabaseHelper;
import com.toptech.launcher.net.NetworkMonitor;
import com.toptech.launcher.tools.ConstantUtil;
import com.toptech.launcher.tools.FileUtil;
import com.toptech.launcher.ui.MainLayout;
import com.toptech.launcher.ui.MainUpView;
import com.toptech.launcher.ui.PopInputSource;
import com.toptech.launcher.ui.ReflectItemView;
import com.toptech.launcher.ui.SmoothHorizontalScrollView;
import com.toptech.launcher.ui.TopRightStatusBar;
import com.toptech.launchersagittarius.tv.GetFileDataFromBottom;
import com.toptech.launchersagittarius.tv.TVView;
import com.toptech.launchersagittarius.tv.TVWindowManager;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"NewApi", "ClickableViewAccessibility"})
public class SagittariusActivity extends FragmentActivity implements ViewTreeObserver.OnGlobalFocusChangeListener, AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory, PopInputSource.ScaleTVWindowListener, View.OnClickListener, TVView.OnCheckSignalListener {
    public static boolean IsScreensend = true;
    private static final String TAG = SagittariusActivity.class.getSimpleName();
    private static final int UDATE_MAIN_SELECT = 2;
    private static final int UDATE_UI = 1;
    public static boolean flash_flag = false;
    public static int tvM_wHeight;
    public static int tvM_wWidth;
    public static int tvOsdHeight;
    public static int tvOsdWidth;
    private String IR_TYPE;
    private final String IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";
    private GridView appGridView;
    private Boolean bSyncC = false;
    private ImageSwitcher child;
    private ConnectivityManager connectivityManager;
    private ImageSwitcher dangbei;
    private List<Drawable> data = new ArrayList();
    private boolean flag = false;
    private int flaglocation;
    private boolean frist_status = false;
    private Animation hide;
    private FrameLayout histroy;
    private boolean isMute = true;
    private boolean isShowTvView = false;
    private boolean isSignalStable = false;
    private ImageSwitcher jilu;
    private ImageSwitcher liveImageView;
    private AllAppAdapter mAllAppAdapter;
    private AudioManager mAudioManager = null;
    private Bitmap mBitmap;
    private String mBitmapTitle;
    private String mBitmapView;
    private Runnable mCheckSignalStable = new Runnable() {
        /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass8 */

        public void run() {
            if (SagittariusActivity.this.mTvCommonManager != null) {
                Log.e(SagittariusActivity.TAG, "mTvCommonManager != null");
                SagittariusActivity.this.isSignalStable = SagittariusActivity.this.mTvCommonManager.isSignalStable(SagittariusActivity.this.mTvCommonManager.getCurrentTvInputSource());
            } else {
                SagittariusActivity.this.isSignalStable = false;
            }
            SagittariusActivity.this.ShowTvView();
            Log.e(SagittariusActivity.TAG, "isSignalStable -->> " + SagittariusActivity.this.isSignalStable);
            SagittariusActivity.this.mHandler.postDelayed(SagittariusActivity.this.mCheckSignalStable, 2000);
        }
    };
    private ArrayList<CommendBean> mCommendList;
    public int mCommendPosition = 0;
    private int mDelay_Time = 10000;
    private boolean mFristFlag;
    private Handler mHandler;
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass6 */
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";
        String SYSTEM_REASON = "reason";

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS") && TextUtils.equals(intent.getStringExtra(this.SYSTEM_REASON), this.SYSTEM_HOME_KEY)) {
                Log.d("zsr", "get home key");
                SagittariusActivity.this.killActivityThrPKG("net.myvst.v2");
                for (int i = 0; i < 2; i++) {
                    SagittariusActivity.this.SendKey(4);
                    try {
                        Thread.sleep(100);
                        Log.d("zsr", "do back");
                    } catch (Exception e) {
                    }
                }
            }
        }
    };
    private ArrayList<String> mJsonPng;
    private int mJsonPngcount = 0;
    private ArrayList<String> mJsonTitle;
    private ArrayList<String> mJsonView;
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks;
    private LoaderManager mLoaderManager;
    private NetworkMonitor mNetworkMonitor;
    private NetworkMonitor.INetworkUpdateListener mNetworkStateChangeListener = null;
    private View mOldFocus;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass1 */

        public boolean onTouch(View v, MotionEvent event) {
            View focus = null;
            switch (event.getAction()) {
                case 0:
                case 2:
                default:
                    return false;
                case 1:
                    if (SagittariusActivity.this.getAbsLocal(SagittariusActivity.this.title0) >= 0) {
                        SagittariusActivity.this.mTvView.requestFocus();
                        focus = SagittariusActivity.this.mTvView;
                    } else if (SagittariusActivity.this.getAbsLocal(SagittariusActivity.this.title1) >= 0) {
                        SagittariusActivity.this.liveImageView.requestFocus();
                        focus = SagittariusActivity.this.liveImageView;
                    } else if (SagittariusActivity.this.getAbsLocal(SagittariusActivity.this.title2) >= 0) {
                        SagittariusActivity.this.dangbei.requestFocus();
                        focus = SagittariusActivity.this.dangbei;
                    } else if (SagittariusActivity.this.getAbsLocal(SagittariusActivity.this.title3) >= 0) {
                        SagittariusActivity.this.appGridView.requestFocus();
                        focus = SagittariusActivity.this.appGridView.getChildAt(0);
                    }
                    if (focus != null) {
                        SagittariusActivity.this.mainUpView1.setFocusView(focus, (View) null, 1.0f);
                        SagittariusActivity.this.mOldFocus = null;
                    }
                    return true;
            }
        }
    };
    private PackageManager mPackageManager;
    private BroadcastReceiver mPackageUpdateReceiver = new BroadcastReceiver() {
        /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass5 */

        public void onReceive(Context context, Intent intent) {
            SagittariusActivity.this.mHandler.sendEmptyMessage(intent.getIntExtra("com.toptech.launcher.LOCAL_BROADCAST_PACKAGE_UPDATE_EXTRA", 0));
            Log.w(SagittariusActivity.TAG, "LOCAL_BROADCAST_PACKAGE_UPDATE_EXTRA send");
        }
    };
    private String mPicturePath;
    private int mPngnum;
    private PopInputSource mPopInputSource;
    private ImageSwitcher mRecommendation;
    private TextView mRecommendation_title;
    private Runnable mRunnable = new Runnable() {
        /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass2 */

        public void run() {
            if (SagittariusActivity.this.netInfo != null && SagittariusActivity.this.netInfo.isConnected() && SagittariusActivity.this.mCommendList == null) {
                SagittariusActivity.this.sendBroadcast(new Intent("com.toptech.launcher.json.update_date"));
            }
            if (SagittariusActivity.this.mCommendList != null) {
                Message msg = SagittariusActivity.this.mHandler.obtainMessage();
                msg.what = 1;
                SagittariusActivity.this.mHandler.sendMessageDelayed(msg, 1000);
            }
            SagittariusActivity.this.mHandler.postDelayed(this, (long) SagittariusActivity.this.mDelay_Time);
        }
    };
    private SagittariusTool mSagittariusTool;
    private SmoothHorizontalScrollView mScrollView;
    private TVWindowManager mTVWindowManager;
    private TopRightStatusBar mTopRightStatusBar;
    protected TvCommonManager mTvCommonManager;
    private boolean mTvFlag;
    private TVView mTvView;
    private IntentFilter mUpadteImagesFilter;
    private UpdateImagesReceiver mUpdateImagesReceiver;
    private MainUpView mainUpView1;
    private FrameLayout main_TV;
    private MainLayout main_lay11;
    private ActivityManager manager;
    private ImageSwitcher movies;
    private NetworkInfo netInfo;
    private ImageSwitcher quanjuhe;
    private boolean refocusview = false;
    private ReflectItemView search;
    private boolean settingflag = false;
    private ReflectItemView settings;
    private ImageView shade;
    private ImageSwitcher teleplay;
    private FrameLayout title0;
    private FrameLayout title1;
    private FrameLayout title2;
    private FrameLayout title3;
    private boolean tvClick = false;
    private ImageSwitcher variety_show;
    private FrameLayout vediopaly;

    public static class AppFragmentHandler extends Handler {
        final WeakReference<SagittariusActivity> mContext;

        public AppFragmentHandler(SagittariusActivity f) {
            this.mContext = new WeakReference<>(f);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SagittariusActivity Apps = this.mContext.get();
            if (Apps != null) {
                if (msg.what == 1) {
                    if (Apps.mCommendList != null) {
                        Apps.mCommendPosition = getPosition(Apps.mCommendPosition, Apps.mCommendList.size());
                        Apps.UpdateImage(Apps.mRecommendation, Apps.mRecommendation_title, FileUtil.DIR_COMMEND_LIST, ((CommendBean) Apps.mCommendList.get(Apps.mCommendPosition)).pic_name, ((CommendBean) Apps.mCommendList.get(Apps.mCommendPosition)).title);
                    }
                } else if (msg.what == PackageUpdateType.PACKAGE_UPDATE_TYPE_ADDED.ordinal()) {
                    Apps.notifyAllAppsDataChanged();
                } else if (msg.what == PackageUpdateType.PACKAGE_UPDATE_TYPE_REMOVED.ordinal()) {
                    Apps.notifyAllAppsDataChanged();
                }
            }
        }

        private int getPosition(int position, int limit) {
            if (position + 1 < limit) {
                return position + 1;
            }
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        this.mAudioManager = (AudioManager) getSystemService("audio");
        setContentView(R.layout.activity_main);
        getWindow().addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
        this.IR_TYPE = (String) SystemProperties.get("mstar.toptech.remote", "0");
        this.mHandler = new AppFragmentHandler(this);
        this.mPackageManager = getPackageManager();
        this.manager = (ActivityManager) getSystemService("activity");
        this.mLoaderManager = getLoaderManager();
        this.connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        this.netInfo = this.connectivityManager.getActiveNetworkInfo();
        this.mSagittariusTool = SagittariusTool.getInstance();
        this.mTvFlag = false;
        this.main_lay11 = (MainLayout) findViewById(R.id.main_layout);
        this.main_lay11.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        init();
        initGridView();
        initDir();
        initReceivers();
        if (this.IR_TYPE.equals("TOPTECH_AT070_FANQIDIANZI")) {
            startAppbyPackage("com.example.screensender", this);
        } else if (this.IR_TYPE.equals("TOPTECH_AT070_Mips")) {
            startAppbyPackage("com.example.Mips", this);
        }
        this.mOldFocus = this.mainUpView1;
        this.mNetworkMonitor.startMonitor();
        setupTVWindow();
        this.mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass3 */

            @Override // android.app.LoaderManager.LoaderCallbacks
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(SagittariusActivity.this.getApplicationContext(), LauncherDatabaseHelper.MostVisited.CONTENT_URI, new String[]{"_id", "appname", "packagename", "activityname", "frequency", "time"}, null, null, "frequency DESC,time DESC");
            }

            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                SagittariusActivity.this.mAllAppAdapter.swapCursor(data);
            }

            @Override // android.app.LoaderManager.LoaderCallbacks
            public void onLoaderReset(Loader<Cursor> loader) {
                SagittariusActivity.this.mAllAppAdapter.swapCursor(null);
            }
        };
        this.mLoaderManager.initLoader(2016, null, this.mLoaderCallbacks);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mPackageUpdateReceiver, new IntentFilter("com.toptech.launcher.LOCAL_BROADCAST_PACKAGE_UPDATE"));
        this.mHandler.postDelayed(this.mRunnable, (long) this.mDelay_Time);
        new Thread(new Runnable() {
            /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass4 */

            public void run() {
                boolean isBootFinish = false;
                while (!isBootFinish) {
                    if ("stopped".equals(SystemProperties.get("init.svc.bootanim", "0"))) {
                        isBootFinish = true;
                        if (SystemProperties.getBoolean("persist.sys.isfirstttpw", false)) {
                            Log.d("tvos", "--------unmuteAfterPowerOn---------");
                            SystemProperties.set("persist.sys.ispvrlist.play", "false");
                            SystemProperties.set("persist.sys.isfirstttpw", "false");
                            TvCommonManager.getInstance().setTvosCommonCommand("unmuteAfterPowerOn");
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        if (getSystemAging()) {
            startApplication(new ComponentName("com.toptech.factorytools", "com.toptech.factorytools.AgingActivity"), this);
        }
        registerReceiver(this.mHomeKeyEventReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        this.hide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_out);
        this.shade = (ImageView) findViewById(R.id.shade);
        this.mTvCommonManager = TvCommonManager.getInstance();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        this.mPngnum = 0;
        this.mFristFlag = false;
        this.mTopRightStatusBar = (TopRightStatusBar) findViewById(R.id.top_right_status);
        this.mScrollView = (SmoothHorizontalScrollView) findViewById(R.id.myHorizontalScrollView);
        this.title0 = (FrameLayout) findViewById(R.id.title0);
        this.title1 = (FrameLayout) findViewById(R.id.title1);
        this.title2 = (FrameLayout) findViewById(R.id.title2);
        this.title3 = (FrameLayout) findViewById(R.id.title3);
        this.appGridView = (GridView) findViewById(R.id.page3_gridview);
        this.mTvView = (TVView) findViewById(R.id.tv_surface_view);
        this.mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        this.mRecommendation_title = (TextView) findViewById(R.id.recommendation_title);
        this.mPopInputSource = (PopInputSource) findViewById(R.id.pop_inputsource);
        this.main_TV = (FrameLayout) findViewById(R.id.main_TV);
        this.histroy = (FrameLayout) findViewById(R.id.page_record);
        this.search = (ReflectItemView) findViewById(R.id.page_search);
        this.vediopaly = (FrameLayout) findViewById(R.id.page_localmm);
        this.settings = (ReflectItemView) findViewById(R.id.page_settings);
        this.quanjuhe = (ImageSwitcher) findViewById(R.id.quanjuhe);
        this.liveImageView = (ImageSwitcher) findViewById(R.id.liveshow);
        this.child = (ImageSwitcher) findViewById(R.id.child);
        this.variety_show = (ImageSwitcher) findViewById(R.id.variety_show);
        this.movies = (ImageSwitcher) findViewById(R.id.movies);
        this.teleplay = (ImageSwitcher) findViewById(R.id.telepaly);
        this.jilu = (ImageSwitcher) findViewById(R.id.jilu);
        this.dangbei = (ImageSwitcher) findViewById(R.id.dangbei);
        this.mRecommendation = (ImageSwitcher) findViewById(R.id.recommendation);
        this.mTvView.startInputSourceThread();
        this.main_TV.setNextFocusRightId(R.id.liveshow);
        this.mTVWindowManager = new TVWindowManager(this, this.mTvView.getPaddingLeft(), this.mTvView.getPaddingTop(), this.mTvView.getWidth(), this.mTvView.getHeight(), this.mTvView);
        this.mPopInputSource.setOnScaleTVWindowListener(this);
        this.mNetworkStateChangeListener = this.mTopRightStatusBar;
        this.mNetworkMonitor = new NetworkMonitor(this, this.mNetworkStateChangeListener);
        this.mainUpView1.setUpRectResource(R.drawable.test_rectangle);
        this.mainUpView1.setShadowResource(R.drawable.item_shadow);
        this.mScrollView.setOnTouchListener(this.mOnTouchListener);
        this.main_TV.setOnClickListener(this);
        this.mRecommendation.setOnClickListener(this);
        this.mTvView.setOnClickListener(this);
        this.mTvView.setOnCheckSignalListener(this);
        this.liveImageView.setFactory(this);
        this.child.setFactory(this);
        this.variety_show.setFactory(this);
        this.movies.setFactory(this);
        this.teleplay.setFactory(this);
        this.jilu.setFactory(this);
        this.dangbei.setFactory(this);
        this.mRecommendation.setFactory(this);
        this.mRecommendation.setInAnimation(this, R.anim.push_in);
        this.mRecommendation.setOutAnimation(this, R.anim.push_out);
        this.dangbei.setImageDrawable(readDrawable(this, R.drawable.dangbei1, false));
        this.mRecommendation.setImageDrawable(readDrawable(this, R.drawable.variety_show, false));
        this.liveImageView.setImageDrawable(readDrawable(this, R.drawable.recommend, false));
        this.liveImageView.setOnClickListener(this);
        this.child.setImageDrawable(readDrawable(this, R.drawable.shaoer2, false));
        this.child.setOnClickListener(this);
        this.variety_show.setImageDrawable(readDrawable(this, R.drawable.variety_show2, false));
        this.variety_show.setOnClickListener(this);
        this.movies.setImageDrawable(readDrawable(this, R.drawable.movies, false));
        this.movies.setOnClickListener(this);
        this.teleplay.setImageDrawable(readDrawable(this, R.drawable.teleplay, false));
        this.teleplay.setOnClickListener(this);
        this.jilu.setImageDrawable(readDrawable(this, R.drawable.jilu, false));
        this.jilu.setOnClickListener(this);
        this.quanjuhe.setFactory(this);
        this.quanjuhe.setImageDrawable(readDrawable(this, R.drawable.quanjuhe, false));
        this.quanjuhe.setOnClickListener(this);
        this.histroy.setOnClickListener(this);
        this.search.setOnClickListener(this);
        this.dangbei.setOnClickListener(this);
        this.vediopaly.setOnClickListener(this);
        this.settings.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addDataScheme("file");
        registerReceiver(this.mTopRightStatusBar.mUsbListener, filter);
        getWidth_HeightFromBottom();
    }

    /* access modifiers changed from: package-private */
    public void getWidth_HeightFromBottom() {
        String getfilename = GetFileDataFromBottom.getFileDataString("/config/model/Customer_1.ini", "panel", "m_pPanelName", "null");
        if (getfilename.contains("\"")) {
            getfilename = getfilename.substring(1, getfilename.length() - 1);
        }
        if (getfilename.contains(".ini")) {
            tvM_wWidth = Integer.parseInt(GetFileDataFromBottom.getFileDataString(getfilename, "panel", "m_wPanelWidth", "get nothing"));
            tvM_wHeight = Integer.parseInt(GetFileDataFromBottom.getFileDataString(getfilename, "panel", "m_wPanelHeight", "get nothing"));
            tvOsdWidth = Integer.parseInt(GetFileDataFromBottom.getFileDataString(getfilename, "panel", "osdWidth", "get nothing"));
            tvOsdHeight = Integer.parseInt(GetFileDataFromBottom.getFileDataString(getfilename, "panel", "osdHeight", "get nothing"));
            Log.d("zsr_file", "[x,y][w,h] =====[" + tvM_wWidth + "," + tvOsdWidth + "][" + tvM_wWidth + "," + tvOsdWidth + "]");
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onResume() {
        killActivityAll(this);
        if (SystemProperties.get("mstar.factory_test_mode").equals("true") || SystemProperties.get("mstar.factory_aging_mode").equals("true")) {
            Log.i(TAG, "<<<<<<----------PowerOn == true ---------->>>>>");
            SystemProperties.set("mstar.factory_test_mode", "false");
            SystemProperties.set("mstar.factory_aging_mode", "false");
            ComponentName componentName = new ComponentName("com.mstar.tv.tvplayer.ui", "com.mstar.tv.tvplayer.ui.RootActivity");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(componentName);
            intent.setFlags(270532608);
            this.tvClick = true;
            startActivity(intent);
        } else {
            if (this.frist_status) {
                openTVWindow();
                flash_flag = true;
            }
            this.frist_status = true;
            ShowTvView();
        }
        super.onResume();
    }

    public boolean isPowerOn() {
        Log.d(TAG, "Is Fist Power On: " + SystemProperties.getBoolean("mstar.launcher.1stinit", false));
        if (SystemProperties.getBoolean("mstar.launcher.1stinit", false)) {
            return false;
        }
        SystemProperties.set("mstar.launcher.1stinit", "true");
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("setting", this.settingflag);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.refocusview = savedInstanceState.getBoolean("setting");
        Log.d("zsr_save", "text :  " + this.refocusview);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onStart() {
        registerReceiver(this.mUpdateImagesReceiver, this.mUpadteImagesFilter);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onPause() {
        if (!this.tvClick) {
            closeTVWindow();
        } else {
            this.tvClick = false;
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onStop() {
        unregisterReceiver(this.mUpdateImagesReceiver);
        System.gc();
        muteTvWindow(false);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onDestroy() {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            System.gc();
        }
        this.mNetworkMonitor.stopMonitor();
        this.mTvView.stopInputSourceThread();
        destroyTVWindow();
        this.mLoaderManager.destroyLoader(2016);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mPackageUpdateReceiver);
        this.main_lay11.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        unregisterReceiver(this.mTopRightStatusBar.mUsbListener);
        super.onDestroy();
        Log.d("zsr_save", "ondestroy");
    }

    private void killActivity(Activity context) {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = this.manager.getRunningTasks(3);
        if (runningTaskInfos != null) {
            try {
                Method forceStopPackage = this.manager.getClass().getDeclaredMethod("forceStopPackage", String.class);
                forceStopPackage.setAccessible(true);
                forceStopPackage.invoke(this.manager, runningTaskInfos.get(1).topActivity.getPackageName());
            } catch (Exception e) {
            }
        }
    }

    private void killActivity(Activity context, String pkg) {
        try {
            Method forceStopPackage = this.manager.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(this.manager, pkg);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void killActivityThrPKG(String pkg) {
        ((ActivityManager) getSystemService("activity")).killBackgroundProcesses(pkg);
    }

    private void killActivityAll(Activity context) {
        String[] pkgList;
        Iterator<ResolveInfo> list = PackageUtils.queryAplicationInfo(context).iterator();
        if (list != null) {
            while (list.hasNext()) {
                ResolveInfo rinfo = list.next();
                if (!rinfo.activityInfo.packageName.equals("com.toptech.launchersagittarius") && !rinfo.activityInfo.packageName.equals("com.ledxxx.mips") && !rinfo.activityInfo.packageName.equals("com.toptech.floatball")) {
                    try {
                        Method forceStopPackage = this.manager.getClass().getDeclaredMethod("forceStopPackage", String.class);
                        forceStopPackage.setAccessible(true);
                        this.manager.killBackgroundProcesses(rinfo.activityInfo.packageName);
                        forceStopPackage.invoke(this.manager, rinfo.activityInfo.packageName);
                    } catch (Exception e) {
                        Log.e("sch1", "error :    " + e.getMessage());
                    }
                }
            }
            ActivityManager am = (ActivityManager) getSystemService("activity");
            List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
            if (infoList != null) {
                for (int i = 0; i < infoList.size(); i++) {
                    ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
                    Log.d(TAG, "process name : " + appProcessInfo.processName);
                    Log.d(TAG, "importance : " + appProcessInfo.importance);
                    if ((appProcessInfo.processName.lastIndexOf(":") != -1 && appProcessInfo.processName.substring(0, appProcessInfo.processName.lastIndexOf(":")).equals("net.myvst.v2")) || appProcessInfo.processName.equals("com.vst.live")) {
                        Process.killProcess(appProcessInfo.pid);
//                        Process.killProcessQuiet(appProcessInfo.pid);
                        for (String str : appProcessInfo.pkgList) {
                            Log.d(TAG, "It will be killed, package name : " + appProcessInfo.processName.substring(0, appProcessInfo.processName.lastIndexOf(":")));
                            am.killBackgroundProcesses(str);
                        }
                    }
                }
            }
        }
    }

    public void notifyAllAppsDataChanged() {
        this.mLoaderManager.restartLoader(2016, null, this.mLoaderCallbacks);
    }

    private void initGridView() {
        this.mAllAppAdapter = new AllAppAdapter(this, null);
        this.appGridView.setAdapter((ListAdapter) this.mAllAppAdapter);
        this.appGridView.setOnItemClickListener(new AllAppItemcListener());
        this.appGridView.setOnItemSelectedListener(this);
        this.appGridView.setFocusable(false);
        this.appGridView.setFocusableInTouchMode(false);
        SharedPreferences localSharedPreferences = getPreferences(0);
        if (!localSharedPreferences.getBoolean("com.toptech.launcher.ALL_APPSINFO_IN_DATABASE", false)) {
            PackageUtils.updateAppInfoToDataBase(this, true, null, null, null);
            SharedPreferences.Editor e = localSharedPreferences.edit();
            e.putBoolean("com.toptech.launcher.ALL_APPSINFO_IN_DATABASE", true);
            e.commit();
        }
    }

    public boolean getSystemAging() {
        try {
            String env_aging = TvManager.getInstance().getEnvironment("env_aging_Running");
            Log.d("lhf", "********env_aging get frome uboot:-" + env_aging + "-for aging!");
            if (env_aging != null && env_aging.equals("1")) {
                Log.d("lhf", "getSystemAging********1 for aging!~~~~~~~hafieng@@@@@@@");
                return true;
            } else if (env_aging == null || !env_aging.equals("0")) {
                Log.d("lhf", "getSystemAging********no setting for aging!~~~~~~~hafieng@@@@@@@");
                return false;
            } else {
                Log.d("lhf", "getSystemAging********0 for aging!~~~~~~~hafieng@@@@@@@");
                return false;
            }
        } catch (Exception e) {
            Log.d("lhf", "getSystemAging********env_aging get frome uboot: error!");
            return false;
        }
    }

    /* access modifiers changed from: private */
    public class AllAppItemcListener implements AdapterView.OnItemClickListener {
        private AllAppItemcListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            AppItemViewHolder localAppItemViewHolder = (AppItemViewHolder) view.getTag();
            if (localAppItemViewHolder.packageName != null && localAppItemViewHolder.activityName != null) {
                Log.d(SagittariusActivity.TAG, "OnItemClickListener " + localAppItemViewHolder.packageName + "      " + localAppItemViewHolder.activityName);
                SagittariusActivity.this.startApplication(new ComponentName(localAppItemViewHolder.packageName, localAppItemViewHolder.activityName), view.getContext());
                PackageUtils.updateAppInfoToDataBase(view.getContext(), false, localAppItemViewHolder.packageName, localAppItemViewHolder.activityName, "com.toptech.launcher.DataBaseAction.ACTION_UPDATE");
                SagittariusActivity.this.mLoaderManager.restartLoader(2016, null, SagittariusActivity.this.mLoaderCallbacks);
            }
        }
    }

    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        int xleft = this.mainUpView1.getLeft();
        int ytop = this.mainUpView1.getTop();
        Log.d("pan_test", "left:" + xleft + "  top:" + ytop + "  right:" + this.mainUpView1.getRight() + "  bottom:" + this.mainUpView1.getBottom());
        ShowTvView();
        Log.d("zsr_focus", "global_ refocusview/mFristFlag: " + this.refocusview + "  " + this.mFristFlag);
        synchronized (this.bSyncC) {
            if (this.refocusview) {
                this.settings.requestFocus();
                this.mainUpView1.setFocusView(this.settings, this.mOldFocus, 1.0f);
                this.appGridView.setFocusable(true);
                this.appGridView.setFocusableInTouchMode(true);
                this.mOldFocus = newFocus;
                this.refocusview = false;
            } else if (newFocus.getId() != 2131099699) {
                this.mainUpView1.setFocusView(newFocus, this.mOldFocus, 1.0f);
                this.mOldFocus = newFocus;
            } else {
                this.mainUpView1.setFocusView(this.appGridView.getSelectedView(), this.mOldFocus, 1.0f);
                this.mOldFocus = this.appGridView.getSelectedView();
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ShowTvView() {
        boolean z = false;
        int[] location = new int[2];
        this.main_TV.getLocationOnScreen(location);
        Log.e(TAG, "main_tv x -->> " + location[0]);
        Log.e(TAG, "isMute -->> " + this.isMute);
        if (this.isSignalStable) {
            if (location[0] >= 0) {
                z = true;
            }
            isShowTvView(z);
            return;
        }
        closeTvView();
    }

    private void closeTvView() {
        if (this.isShowTvView) {
            this.shade.setVisibility(0);
            muteTvWindow(true);
            this.isShowTvView = false;
        }
    }

    private void isShowTvView(boolean isShow) {
        boolean z = true;
        if (!isShow) {
            closeTvView();
        } else if (!this.isShowTvView) {
            this.shade.clearAnimation();
            this.shade.startAnimation(this.hide);
            this.shade.setVisibility(4);
            muteTvWindow(false);
            this.isShowTvView = true;
        }
        if (this.isShowTvView) {
            z = false;
        }
        this.isMute = z;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        Log.d("zsr_focus", "refocusview/mFristFlag: " + this.refocusview + "  " + this.mFristFlag);
        synchronized (this.bSyncC) {
            if (this.refocusview) {
                this.settings.requestFocus();
                this.mainUpView1.setFocusView(this.settings, this.mOldFocus, 1.0f);
                this.appGridView.setFocusable(true);
                this.appGridView.setFocusableInTouchMode(true);
                this.mOldFocus = arg1;
                this.refocusview = false;
            } else if (!this.mFristFlag) {
                this.main_TV.requestFocus();
                this.appGridView.setFocusable(true);
                this.appGridView.setFocusableInTouchMode(true);
                this.mainUpView1.setFocusView(this.main_TV, this.mOldFocus, 1.0f);
                this.mOldFocus = arg1;
                this.mFristFlag = true;
                Log.d("zsr_save", "selected");
            } else if (arg2 < this.appGridView.getCount()) {
                this.mainUpView1.setFocusView(arg1, this.mOldFocus, 1.0f);
                this.mOldFocus = arg1;
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override // com.toptech.launcher.ui.PopInputSource.ScaleTVWindowListener
    public boolean updateTVWindow() {
        if (this.mTVWindowManager != null) {
            return this.mTVWindowManager.resetTVWindow(false);
        }
        return false;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_TV /*{ENCODED_INT: 2131099663}*/:
                break;
            case R.id.hostory /*{ENCODED_INT: 2131099664}*/:
            case R.id.shade /*{ENCODED_INT: 2131099668}*/:
            case R.id.pop_inputsource /*{ENCODED_INT: 2131099669}*/:
            case R.id.history /*{ENCODED_INT: 2131099671}*/:
            case R.id.search /*{ENCODED_INT: 2131099672}*/:
            case R.id.title1 /*{ENCODED_INT: 2131099673}*/:
            case R.id.page_liveshow /*{ENCODED_INT: 2131099674}*/:
            case R.id.page_child /*{ENCODED_INT: 2131099676}*/:
            case R.id.page_variety_show /*{ENCODED_INT: 2131099677}*/:
            case R.id.page_movies /*{ENCODED_INT: 2131099679}*/:
            case R.id.page_jilu /*{ENCODED_INT: 2131099681}*/:
            case R.id.page_teleplay /*{ENCODED_INT: 2131099682}*/:
            case R.id.page_quanjuhe /*{ENCODED_INT: 2131099684}*/:
            case R.id.title2 /*{ENCODED_INT: 2131099687}*/:
            case R.id.page_dangbei /*{ENCODED_INT: 2131099688}*/:
            case R.id.localmm /*{ENCODED_INT: 2131099691}*/:
            case R.id.page_recommendation /*{ENCODED_INT: 2131099692}*/:
            case R.id.recommendation_title /*{ENCODED_INT: 2131099694}*/:
            default:
                return;
            case R.id.liveshow /*{ENCODED_INT: 2131099665}*/:
                startAppbyPackage("com.vst.live", this);
                return;
            case R.id.page_search /*{ENCODED_INT: 2131099666}*/:
                startActivity(new Intent("myvst.intent.action.SearchActivity"));
                return;
            case R.id.tv_surface_view /*{ENCODED_INT: 2131099667}*/:
                this.main_TV.requestFocus();
                break;
            case R.id.page_record /*{ENCODED_INT: 2131099670}*/:
                startActivity(new Intent("myvst.intent.action.RecodeActivity"));
                return;
            case R.id.child /*{ENCODED_INT: 2131099675}*/:
                Intent intent = new Intent("myvst.intent.action.VodTypeActivity");
                intent.putExtra("vodtype", "3");
                startActivity(intent);
                return;
            case R.id.variety_show /*{ENCODED_INT: 2131099678}*/:
                Intent intent2 = new Intent("myvst.intent.action.VodTypeActivity");
                intent2.putExtra("vodtype", "4");
                startActivity(intent2);
                return;
            case R.id.movies /*{ENCODED_INT: 2131099680}*/:
                Log.d("ppan", "movies");
                Intent intent3 = new Intent("myvst.intent.action.VodTypeActivity");
                intent3.putExtra("vodtype", "1");
                startActivity(intent3);
                return;
            case R.id.telepaly /*{ENCODED_INT: 2131099683}*/:
                Intent intent4 = new Intent("myvst.intent.action.VodTypeActivity");
                intent4.putExtra("vodtype", "2");
                startActivity(intent4);
                return;
            case R.id.jilu /*{ENCODED_INT: 2131099685}*/:
                Intent intent5 = new Intent("myvst.intent.action.VodTypeActivity");
                intent5.putExtra("vodtype", "5");
                startActivity(intent5);
                return;
            case R.id.quanjuhe /*{ENCODED_INT: 2131099686}*/:
                startActivity(new Intent("myvst.intent.action.LancherActivity"));
                return;
            case R.id.dangbei /*{ENCODED_INT: 2131099689}*/:
                startApplication(new ComponentName("com.dangbeimarket", "com.dangbeimarket.activity.WelcomeActivity"), this);
                return;
            case R.id.page_localmm /*{ENCODED_INT: 2131099690}*/:
                startApplication(new ComponentName("com.jrm.localmm", "com.jrm.localmm.ui.main.FileBrowserActivity"), this);
                return;
            case R.id.recommendation /*{ENCODED_INT: 2131099693}*/:
                if (this.mCommendList == null || this.mCommendPosition >= this.mCommendList.size()) {
                    Toast.makeText(this, "Please check your network connection", 0).show();
                    return;
                }
                Log.e(TAG, "Commend list url -->> " + this.mCommendList.get(this.mCommendPosition).view);
                Intent intent6 = new Intent("com.dangbeimarket.action.act.detail");
                intent6.putExtra("url", this.mCommendList.get(this.mCommendPosition).view);
                intent6.putExtra("transfer", "dingke");
                intent6.setFlags(268435456);
                startActivity(intent6);
                return;
            case R.id.page_settings /*{ENCODED_INT: 2131099695}*/:
                startApplication(new ComponentName("com.android.settings", "com.android.settings.Settings"), this);
                this.settingflag = true;
                return;
        }
        startApplication(new ComponentName("com.mstar.tv.tvplayer.ui", "com.mstar.tv.tvplayer.ui.RootActivity"), this);
        this.tvClick = true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startApplication(ComponentName componentName, Context context) {
        try {
            this.mPackageManager.getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("SCH20150821", "startApplication the packagename:" + componentName.getPackageName() + " is error");
            e.printStackTrace();
        }
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        localIntent.setFlags(268435456);
        localIntent.setComponent(componentName);
        context.startActivity(localIntent);
    }

    private void startAppbyPackage(String componentName, Context context) {
        Intent localIntent = this.mPackageManager.getLaunchIntentForPackage(componentName);
        if (localIntent != null) {
            context.startActivity(localIntent);
        }
    }

    private String getTopActivity(Context context) {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (runningTaskInfos != null) {
            return runningTaskInfos.get(0).topActivity.toString();
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void SendKey(final int keycode) {
        new Thread() {
            /* class com.toptech.launchersagittarius.SagittariusActivity.AnonymousClass7 */

            public void run() {
                try {
                    new Instrumentation().sendKeyDownUpSync(keycode);
                } catch (Exception e) {
                }
            }
        }.start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    @Override // android.support.v4.app.FragmentActivity
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int r5, android.view.KeyEvent r6) {
        /*
        // Method dump skipped, instructions count: 184
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toptech.launchersagittarius.SagittariusActivity.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    public void checkTvWindwon() {
        if (this.mTVWindowManager != null) {
            this.mTVWindowManager.checkState();
        }
    }

    public void closeTVWindow() {
        if (this.mTVWindowManager != null) {
            this.mTVWindowManager.setState(TVWindowManager.TV_STATE.CLOSE);
        }
    }

    public void destroyTVWindow() {
        if (this.mTVWindowManager != null) {
            this.mTVWindowManager.stop();
            this.mTVWindowManager = null;
        }
    }

    public void openTVWindow() {
        if (this.mTVWindowManager != null && !this.mTVWindowManager.getState()) {
            this.mTVWindowManager.setState(TVWindowManager.TV_STATE.OPEN);
            this.isShowTvView = true;
        }
    }

    public void muteTvWindow(boolean parem) {
        if (this.mTvView != null) {
            this.mTvView.setMuteVideo(parem);
        }
    }

    public void setupTVWindow() {
        if (this.mTVWindowManager != null) {
            this.mTVWindowManager.start();
        }
    }

    public void showInputSourceMenu() {
        this.mPopInputSource.show(5000);
    }

    public TVView getTvView() {
        return this.mTvView;
    }

    public int getAbsLocal(View view) {
        int[] num = new int[2];
        view.getLocationOnScreen(num);
        return num[0];
    }

    public Drawable readDrawable(Context context, int resId, boolean zoom) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        if (zoom) {
            opt.inSampleSize = 2;
        }
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream imageInputStream = context.getResources().openRawResource(resId);
        Drawable newDrawable = new BitmapDrawable(context.getResources(), new SoftReference<>(BitmapFactory.decodeStream(imageInputStream, null, opt)).get());
        if (imageInputStream != null) {
            try {
                imageInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newDrawable;
    }

    @Override // com.toptech.launchersagittarius.tv.TVView.OnCheckSignalListener
    public void postRunnable() {
        this.mHandler.postDelayed(this.mCheckSignalStable, 2000);
    }

    @Override // com.toptech.launchersagittarius.tv.TVView.OnCheckSignalListener
    public void removeCallback() {
        this.mHandler.removeCallbacks(this.mCheckSignalStable);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void UpdateImage(ImageSwitcher image, TextView title, String dir, String fileName, String description) {
        Log.d(TAG, "file path -->> " + this.mPicturePath + dir + "/" + fileName);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(this.mPicturePath + dir + "/" + fileName, AutoScale(this.mPicturePath + dir + "/" + fileName, opt, image.getWidth(), image.getHeight()));
        if (bitmap != null) {
            image.setImageDrawable(new BitmapDrawable(getResources(), new SoftReference<>(bitmap).get()));
            title.setText(description);
            return;
        }
        Log.e(TAG, "can't read file " + this.mPicturePath + dir + "/" + fileName);
    }

    private BitmapFactory.Options AutoScale(String path, BitmapFactory.Options opt, int width, int height) {
        if (path != null && opt != null && width > 0 && height > 0) {
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);
            float w = Math.min((((float) opt.outWidth) * 1.0f) / ((float) width), (((float) opt.outHeight) * 1.0f) / ((float) height));
            Log.d(TAG, "w -->> " + w);
            if (w > 1.0f) {
                opt.inSampleSize = (int) w;
            } else {
                opt.inSampleSize = 1;
            }
            Log.d(TAG, "opt.inSampleSize -->> " + opt.inSampleSize);
            opt.inJustDecodeBounds = false;
        }
        return opt;
    }

    private void initDir() {
        this.mPicturePath = FileUtil.getCachePath(getApplicationContext(), FileUtil.IMAGE_PATH);
    }

    private void initReceivers() {
        this.mUpdateImagesReceiver = new UpdateImagesReceiver();
        this.mUpadteImagesFilter = new IntentFilter();
        this.mUpadteImagesFilter.addAction(ConstantUtil.ACTION_UPDATE_COMMEND_LIST);
    }

    /* access modifiers changed from: private */
    public class UpdateImagesReceiver extends BroadcastReceiver {
        private UpdateImagesReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (ConstantUtil.ACTION_UPDATE_COMMEND_LIST.equals(intent.getAction())) {
                SagittariusActivity.this.mCommendList = intent.getParcelableArrayListExtra("mCommendList");
            }
            SagittariusActivity.this.removeStickyBroadcast(intent);
        }
    }
}
