package com.github.coxylicacid.takagi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.github.coxylicacid.takagi.bean.TakagiBean;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class Takagi {

    private Activity activity;
    private LinearLayout rootView;
    private LinearLayout takagiContainer;
    private List<TakagiBean> takagiLists = new ArrayList<>();
    private TextView takagiTitle;
    private int selectedTakagi = 0;

    private int titleUnFinishedColor;
    private int takagiActiveColor;
    private int takagiNormalColor;
    private int summaryColor;

    public Takagi(Activity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        rootView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.takagi_container, null);
        takagiTitle = rootView.findViewById(android.R.id.title);
        takagiContainer = rootView.findViewById(R.id.takagiContainer);
        titleUnFinishedColor = 0xFF323232;
        takagiActiveColor = 0xFF1871EF;
        takagiNormalColor = 0xFF959595;
        summaryColor = 0xFF959595;
    }

    public void setTitle(String title) {
        takagiTitle.setText(title);
    }

    public void setTitle(@StringRes int title) {
        takagiTitle.setText(title);
    }

    public void setTitleColor(int color) {
        takagiTitle.setTextColor(color);
    }

    public void removeTitle() {
        ((ViewGroup) takagiTitle.getParent()).removeView(takagiTitle);
    }

    public TakagiBean add(String title, String summary) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.takagi_view_container, null);
        ((TextView) v.findViewById(android.R.id.title)).setText(title);
        ((TextView) v.findViewById(android.R.id.summary)).setText(summary);
        TakagiBean bean = new TakagiBean(v);
        bean.setTitle(title);
        bean.setSummary(summary);
        bean.setShow(false);
        bean.getIndicator().setIndicatorColor(takagiActiveColor, takagiNormalColor);
        bean.getTitle().setTextColor(titleUnFinishedColor);
        bean.getSummary().setTextColor(summaryColor);
        takagiLists.add(bean);
        takagiContainer.addView(v);
        return bean;
    }

    public TakagiBean add(String title, String summary, View contentView) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.takagi_view_container, null);
        ((TextView) v.findViewById(android.R.id.title)).setText(title);
        ((TextView) v.findViewById(android.R.id.summary)).setText(summary);
        TakagiBean bean = new TakagiBean(v);
        bean.setTitle(title);
        bean.setSummary(summary);
        bean.setShow(false);
        bean.setContentView(contentView);
        bean.getIndicator().setIndicatorColor(takagiActiveColor, takagiNormalColor);
        bean.getTitle().setTextColor(titleUnFinishedColor);
        bean.getSummary().setTextColor(summaryColor);
        takagiLists.add(bean);
        takagiContainer.addView(v);
        return bean;
    }

    public void next() {
        TakagiBean bean = takagiLists.get(selectedTakagi);
        if (selectedTakagi == takagiLists.size() - 1) {
            bean.getIndicator().finish();
            bean.getTitle().setTextColor(takagiActiveColor);
        } else if (selectedTakagi + 1 < takagiLists.size()) {
            bean.getIndicator().finish();
            bean.close();
            bean.getTitle().setTextColor(takagiActiveColor);
            takagiLists.get(selectedTakagi + 1).getIndicator().focus(true);
            takagiLists.get(selectedTakagi + 1).expand();
            selectedTakagi++;
        }
    }

    public void previous() {
        TakagiBean bean = takagiLists.get(selectedTakagi);
        if (selectedTakagi - 1 >= 0) {
            bean.getIndicator().redo();
            bean.close();
            bean.getTitle().setTextColor(titleUnFinishedColor);
            takagiLists.get(selectedTakagi).getIndicator().focus(false);
            takagiLists.get(selectedTakagi - 1).expand();
            selectedTakagi--;
        }
    }

    public void setTakagiActiveColor(int takagiActiveColor) {
        this.takagiActiveColor = takagiActiveColor;
    }

    public void setTakagiNormalColor(int normalColor) {
        this.takagiNormalColor = normalColor;
    }

    public void setTakagiTitleColor(int titleColor) {
        this.titleUnFinishedColor = titleColor;
    }

    public void setTakagiSummaryColor(int summaryColor) {
        this.summaryColor = summaryColor;
    }

    public void applyForThisActivity() {
        activity.setContentView(rootView);
    }

    public void applyForViewGroup(ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        viewGroup.addView(rootView);
    }

    public void select(int pos) {
        if (pos < takagiLists.size() && pos >= 0) {
            takagiLists.get(pos).setShow(true);
            takagiLists.get(pos).expand();
            takagiLists.get(pos).getIndicator().focus(true);
            selectedTakagi = pos;
        }
    }

    public void delete(int pos) {
        if (takagiLists.get(pos).isShow()) {
            if (pos + 1 < takagiLists.size()) {
                takagiLists.get(pos + 1).setShow(true);
                takagiLists.get(pos + 1).expand();
                takagiLists.get(pos + 1).getIndicator().focus(true);
                selectedTakagi = pos + 1;
            } else {
                takagiLists.get(pos - 1).setShow(true);
                takagiLists.get(pos - 1).expand();
                takagiLists.get(pos - 1).getIndicator().focus(true);
                selectedTakagi = pos - 1;
            }

            takagiContainer.removeView(takagiLists.get(pos).getContainer());
            takagiLists.remove(pos);
        } else {
            if (pos < takagiLists.size()) {
                takagiContainer.removeView(takagiLists.get(pos).getContainer());
                takagiLists.remove(pos);
            }
        }
    }

}
