package com.toptech.launcher.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.toptech.SystemProperties;
import com.toptech.launchersagittarius.R;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PopInputSource extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener, Runnable {
    private static final String TAG = "PopInputSource";
    private int AV_num;
    private int HDMI_num;
    private String[] TvList;
    private Drawable drawabel;
    private Handler handler;
    private Context mContext;
    private int mInputSource;
    private String[] mInputSourcelist;
    private LinearLayout mPopInputSource;
    private ArrayList<Integer> mSourceList;
    private int[] mSourceListFlag;
    private TvChannelManager mTvChannelManager;
    private TvCommonManager mTvCommonmanager;
    LinearLayout.LayoutParams paramLine;
    LinearLayout.LayoutParams paramSourceName;
    private ScaleTVWindowListener scaleTvWindow;

    public interface ScaleTVWindowListener {
        boolean updateTVWindow();
    }

    public PopInputSource(Context context) {
        super(context);
        this.mSourceListFlag = new int[44];
        this.mTvCommonmanager = null;
        this.mTvChannelManager = null;
        this.mInputSource = 1;
        this.TvList = null;
        this.AV_num = 0;
        this.HDMI_num = 0;
        this.paramSourceName = new LinearLayout.LayoutParams(-1, -2, 1.0f);
        this.paramLine = new LinearLayout.LayoutParams(-1, -2);
        this.handler = new Handler() {
            /* class com.toptech.launcher.ui.PopInputSource.AnonymousClass1 */

            public void handleMessage(Message msg) {
                PopInputSource.this.setVisibility(8);
                super.handleMessage(msg);
            }
        };
        this.mContext = context;
        init();
        initEvent();
    }

    public PopInputSource(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSourceListFlag = new int[44];
        this.mTvCommonmanager = null;
        this.mTvChannelManager = null;
        this.mInputSource = 1;
        this.TvList = null;
        this.AV_num = 0;
        this.HDMI_num = 0;
        this.paramSourceName = new LinearLayout.LayoutParams(-1, -2, 1.0f);
        this.paramLine = new LinearLayout.LayoutParams(-1, -2);
        this.handler = new Handler() {
            /* class com.toptech.launcher.ui.PopInputSource.AnonymousClass1 */

            public void handleMessage(Message msg) {
                PopInputSource.this.setVisibility(8);
                super.handleMessage(msg);
            }
        };
        this.mContext = context;
        init();
        sourcelist_name();
        initEvent();
    }

    private void init() {
        this.mTvCommonmanager = TvCommonManager.getInstance();
        this.mTvChannelManager = TvChannelManager.getInstance();
        int[] list = this.mTvCommonmanager.getSourceList();
        this.TvList = getResources().getStringArray(R.array.str_arr_list_vals);
        if (SystemProperties.getBoolean("mstar.bluetooth.enable", false)) {
            this.mInputSourcelist = getResources().getStringArray(R.array.str_arr_input_source_vals_bluetooth);
        } else {
            this.mInputSourcelist = getResources().getStringArray(R.array.str_arr_input_source_vals);
        }
        this.mSourceList = new ArrayList<>();
        for (int i = 0; i < 34; i++) {
            if (list[i] > 0 && (SystemProperties.getBoolean("mstar.tuner.exist", true) || !(i == 1 || i == 28))) {
                this.mSourceList.add(Integer.valueOf(i));
            }
        }
        this.mInputSource = this.mTvCommonmanager.getCurrentTvInputSource();
        this.drawabel = getResources().getDrawable(R.drawable.selected_state);
        int postion = 0;
        for (int TvType = 0; TvType < this.TvList.length; TvType++) {
            for (int i2 = 0; i2 < this.mSourceList.size(); i2++) {
                int tmplen = Math.min(this.TvList[TvType].length(), this.mInputSourcelist[this.mSourceList.get(i2).intValue()].length());
                Log.e(TAG, "InputSource -->> " + this.mInputSourcelist[this.mSourceList.get(i2).intValue()]);
                if (this.mInputSourcelist[this.mSourceList.get(i2).intValue()].subSequence(0, tmplen).equals(this.TvList[TvType])) {
                    addTv(i2, postion);
                    postion++;
                }
            }
            Log.e(TAG, "TvType -->> " + this.TvList[TvType]);
        }
    }

    private void addTv(int i, int postion) {
        ButtonTv btn = new ButtonTv(this.mContext);
        btn.setBackgroundColor(0);
        btn.setTextColor(-1);
        btn.setText(this.mInputSourcelist[this.mSourceList.get(i).intValue()]);
        btn.setId(i);
        btn.setpostion(postion);
        btn.setOnClickListener(this);
        btn.setOnFocusChangeListener(this);
        btn.setOnKeyListener(this);
        btn.setSingleLine(true);
        btn.setFocusable(true);
        btn.setFocusableInTouchMode(true);
        btn.setGravity(17);
        btn.setMarqueeRepeatLimit(-1);
        if (this.mSourceList.get(i).intValue() == checkInputSource(this.mInputSource)) {
            btn.requestFocus();
        }
        addView(btn, this.paramSourceName);
        ImageView img = new ImageView(this.mContext);
        img.setBackgroundResource(R.drawable.input_source_divider);
        if (postion != this.mSourceList.size() - 1) {
            addView(img, this.paramLine);
        }
    }

    private void initEvent() {
        View firstItem = getChildAt(0);
        View lastItem = getChildAt(getChildCount() - 1);
        firstItem.setNextFocusUpId(lastItem.getId());
        lastItem.setNextFocusDownId(firstItem.getId());
    }

    private int checkInputSource(int inputsource) {
        switch (inputsource) {
            case 0:
            case 42:
            case 43:
                return 28;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 16;
            case 16:
            case 17 /*{ENCODED_INT: 17}*/:
                return 0;
            case 23:
                return 23;
            case 24:
                return 24;
            case 28:
            case 37:
                return 25;
            default:
                return -1;
        }
    }

    public void run() {
        ((ViewGroup) getParent()).requestFocus();
        setVisibility(8);
    }

    public boolean onKey(View view, int keyCode, KeyEvent event) {
        Log.d("sch", "pop keycode==" + keyCode + " arg0==" + ((ButtonTv) view).getpostion());
        int postion = ((ButtonTv) view).getpostion();
        if (event.getAction() == 0) {
            if (keyCode == 19) {
                if (postion == 0) {
                    getChildAt(getChildCount() - 1).requestFocus();
                    return true;
                }
            } else if (keyCode == 20 && postion == getChildCount() - 1) {
                getChildAt(0).requestFocus();
                return true;
            }
        }
        if (keyCode == 21 || keyCode == 22) {
            removeCallbacks(this);
            run();
            return true;
        } else if (keyCode == 4) {
            removeCallbacks(this);
            run();
            return true;
        } else {
            show(5000);
            return false;
        }
    }

    public void onFocusChange(View arg0, boolean arg1) {
        if (arg1) {
            arg0.setBackgroundDrawable(this.drawabel);
        } else {
            arg0.setBackgroundColor(0);
        }
    }

    public void onClick(View arg0) {
        Log.d("sch", "arg0.id===" + arg0.getId());
        show(1000);
        setTVInputSource(arg0.getId());
    }

    private void setTVInputSource(int position) {
        int tmpSource = this.mSourceList.get(position).intValue();
        if (tmpSource != TvCommonManager.getInstance().getCurrentTvInputSource()) {
            TvCommonManager.getInstance().setInputSource(tmpSource);
            if (tmpSource == 1) {
                int channel = TvChannelManager.getInstance().getCurrentChannelNumber();
                if (channel < 0 || channel > 255) {
                    channel = 0;
                }
                TvChannelManager.getInstance().setAtvChannel(channel);
            } else if (tmpSource == 28) {
                TvChannelManager.getInstance().selectProgram(TvChannelManager.getInstance().getCurrentChannelNumber(), 1);
            }
            this.scaleTvWindow.updateTVWindow();
        }
    }

    private int getCurrInputSource() {
        int curSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        for (int i = 0; i < this.mSourceList.size(); i++) {
            if (curSource == this.mSourceList.get(i).intValue()) {
                for (int j = 0; j < getChildCount(); j++) {
                    if (getChildAt(j).getId() == i) {
                        return ((ButtonTv) getChildAt(j)).getpostion();
                    }
                }
                return 0;
            }
        }
        return 0;
    }

    public void show(long paramLong) {
        if (isShown()) {
            removeCallbacks(this);
        } else {
            setVisibility(0);
            getChildAt(getCurrInputSource() * 2).requestFocus();
        }
        postDelayed(this, paramLong);
    }

    public void setOnScaleTVWindowListener(ScaleTVWindowListener paramScaleTVWindowListener) {
        this.scaleTvWindow = paramScaleTVWindowListener;
    }

    private void delayToRemoveMenu() {
        removeCallbacks(this);
        new Timer().schedule(new TimerTask() {
            /* class com.toptech.launcher.ui.PopInputSource.AnonymousClass2 */

            public void run() {
                PopInputSource.this.handler.sendEmptyMessage(0);
            }
        }, 100);
    }

    /* access modifiers changed from: private */
    public class ButtonTv extends TextView {
        int postion = 0;

        public ButtonTv(Context context) {
            super(context);
        }

        public int getpostion() {
            return this.postion;
        }

        public void setpostion(int post) {
            this.postion = post;
        }
    }

    private void sourcelist_name() {
        if (1 == this.AV_num) {
            int i = 0;
            int j = 0;
            while (i < this.mSourceList.size()) {
                ButtonTv btn = (ButtonTv) getChildAt(j);
                Log.d("ppan", "btn.getText()" + ((Object) btn.getText()));
                if (btn.getText().equals("视频1")) {
                    btn.setText("视频");
                }
                i++;
                j += 2;
            }
        }
        if (1 == this.HDMI_num) {
        }
    }
}
