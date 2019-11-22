package com.github.coxylicacid.takagi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.github.coxylicacid.takagi.bean.TakagiBean;

import org.xmlpull.v1.XmlPullParser;

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
    private float indicatorRadius;
    private float indicatorThickness;
    private Bitmap indicatorIcon;

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
        indicatorRadius = dp(8);
        indicatorThickness = dp(2);
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
        bean.getIndicator().setCircleRadius(indicatorRadius);
        if (indicatorIcon != null) bean.getIndicator().setIndicatorIcon(indicatorIcon);
        bean.getIndicator().setIndicatorThickness(indicatorThickness);
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
        bean.getIndicator().setCircleRadius(indicatorRadius);
        if (indicatorIcon != null) bean.getIndicator().setIndicatorIcon(indicatorIcon);
        bean.getIndicator().setIndicatorThickness(indicatorThickness);
        bean.getTitle().setTextColor(titleUnFinishedColor);
        bean.getSummary().setTextColor(summaryColor);
        takagiLists.add(bean);
        takagiContainer.addView(v);
        return bean;
    }

    public TakagiBean insert(int pos, String title, String summary) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.takagi_view_container, null);
        ((TextView) v.findViewById(android.R.id.title)).setText(title);
        ((TextView) v.findViewById(android.R.id.summary)).setText(summary);
        TakagiBean bean = new TakagiBean(v);
        bean.setTitle(title);
        bean.setSummary(summary);
        bean.setShow(false);
        bean.getIndicator().setIndicatorColor(takagiActiveColor, takagiNormalColor);
        bean.getIndicator().setCircleRadius(indicatorRadius);
        if (indicatorIcon != null) bean.getIndicator().setIndicatorIcon(indicatorIcon);
        bean.getIndicator().setIndicatorThickness(indicatorThickness);
        bean.getTitle().setTextColor(titleUnFinishedColor);
        bean.getSummary().setTextColor(summaryColor);
        takagiLists.add(pos, bean);
        takagiContainer.addView(v, pos);
        return null;
    }

    public TakagiBean insert(int pos, String title, String summary, View contentView) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.takagi_view_container, null);
        ((TextView) v.findViewById(android.R.id.title)).setText(title);
        ((TextView) v.findViewById(android.R.id.summary)).setText(summary);
        TakagiBean bean = new TakagiBean(v);
        bean.setTitle(title);
        bean.setSummary(summary);
        bean.setShow(false);
        bean.setContentView(contentView);
        bean.getIndicator().setIndicatorColor(takagiActiveColor, takagiNormalColor);
        bean.getIndicator().setCircleRadius(indicatorRadius);
        if (indicatorIcon != null) bean.getIndicator().setIndicatorIcon(indicatorIcon);
        bean.getIndicator().setIndicatorThickness(indicatorThickness);
        bean.getTitle().setTextColor(titleUnFinishedColor);
        bean.getSummary().setTextColor(summaryColor);
        takagiLists.add(pos, bean);
        takagiContainer.addView(v, pos);
        return null;
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

        if (selectedTakagi == 0) {
            bean.getIndicator().redo();
            bean.getTitle().setTextColor(titleUnFinishedColor);
            takagiLists.get(selectedTakagi).getIndicator().focus(true);
        }

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

    public void setIndicatorRadius(float radius) {
        this.indicatorRadius = radius;
    }

    public void setIndicatorThickness(float thickness) {
        this.indicatorThickness = thickness;
    }

    public void setIndicatorIcon(Drawable drawable) {
        this.indicatorIcon = ((BitmapDrawable) drawable).getBitmap();
    }

    public void setIndicatorIcon(Bitmap bitmap) {

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

    public TakagiBean get(int pos) {
        return takagiLists.get(pos);
    }

    public LinearLayout getRootView() {
        return rootView;
    }

    public void parse(int takagiRes) {
        try {
            XmlPullParser parser = activity.getResources().getXml(takagiRes);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        // TODO: Parser start.
                        break;
                    case XmlPullParser.START_TAG:
                        if ("takagi".equals(parser.getName())) {
                            View v;
                            String title = parser.getAttributeValue(null, "title");
                            String summary = parser.getAttributeValue(null, "summary");
                            String layout = parser.getAttributeValue(null, "layout");
                            int layoutRes = 0;

                            if (title != null && title.startsWith("@")) {
                                title = activity.getString(Integer.valueOf(title.substring(1)));
                            }

                            if (summary != null && summary.startsWith("@")) {
                                summary = activity.getString(Integer.valueOf(summary.substring(1)));
                            }

                            if (layout != null && layout.startsWith("@")) {
                                layoutRes = Integer.valueOf(layout.substring(1));
                            }

                            Log.e("TAKAGI", "title: " + title + ", summary: " + summary + ", layout: " + layoutRes);

                            if (layoutRes != 0) {
                                v = LayoutInflater.from(activity).inflate(layoutRes, null);
                                add(title == null ? "TITLE" : title, summary == null ? "SUMMARY" : summary, v);
                            } else {
                                add(title == null ? "TITLE" : title, summary == null ? "SUMMARY" : summary);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        // TODO: Parser done.
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dp(float value) {
        float density = activity.getResources().getDisplayMetrics().density;
        return value == 0 ? 0 : (int) Math.ceil(density * value);
    }

}
