package com.toptech.launcher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.toptech.launcher.ui.TextViewWithTTF;
import com.toptech.launchersagittarius.R;
import java.util.ArrayList;
import java.util.List;

public class OpenTabTitleAdapter extends BaseTabTitleAdapter {
    private List<Integer> ids = new ArrayList<Integer>() {
        /* class com.toptech.launcher.adapter.OpenTabTitleAdapter.AnonymousClass1 */

        {
            add(Integer.valueOf((int) R.id.title_bar1));
            add(Integer.valueOf((int) R.id.title_bar2));
            add(Integer.valueOf((int) R.id.title_bar3));
            add(Integer.valueOf((int) R.id.title_bar4));
        }
    };
    private List<String> titleList = new ArrayList();

    public OpenTabTitleAdapter() {
        for (int i = 0; i < 4; i++) {
            this.titleList.add("标题栏" + i);
        }
    }

    @Override // com.toptech.launcher.adapter.BaseTabTitleAdapter
    public int getCount() {
        return this.titleList.size();
    }

    @Override // com.toptech.launcher.adapter.BaseTabTitleAdapter
    public View getView(int position, View convertView, ViewGroup parent) {
        parent.getContext();
        String title = this.titleList.get(position);
        if (convertView != null) {
            return convertView;
        }
        View convertView2 = newTabIndicator(parent.getContext(), title, false);
        convertView2.setId(this.ids.get(position).intValue());
        return convertView2;
    }

    private View newTabIndicator(Context context, String tabName, boolean focused) {
        View viewC = View.inflate(context, R.layout.tab_view_indicator_item, null);
        TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R.id.tv_tab_indicator);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
        lp.setMargins(20, 0, 20, 0);
        view.setLayoutParams(lp);
        view.setText(tabName);
        if (focused) {
            view.setTextColor(context.getResources().getColor(17170443));
            view.setTypeface(null, 1);
            view.requestFocus();
        }
        return viewC;
    }
}
