package com.btellez.solidandroid.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.btellez.solidandroid.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EmptyView extends FrameLayout {
    public interface Listener {
        public void onPrimaryActionClicked();
        public void onSecondaryActionClicked();
    }
    
    public static class SimpleListener implements Listener {
        @Override public void onPrimaryActionClicked() {}
        @Override public void onSecondaryActionClicked() {}
    }
    
    private Listener listener;
    @InjectView(R.id.headline) TextView headline;
    @InjectView(R.id.subheadline) TextView subheadline;
    @InjectView(R.id.primary_action) Button buttonPrimary;
    @InjectView(R.id.secondary_action) Button buttonSecondary;
    
    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setHeadline(int resString) {
        setHeadline(getResources().getString(resString));
    }

    public void setHeadline(String value) {
        headline.setText(value);
    }

    public void setSubheadline(int resString) {
        setSubheadline(getResources().getString(resString));
    }

    public void setSubheadline(String value) {
        subheadline.setText(value);
    }

    public void setListener(Listener listener) {
        if (listener == null)
            listener = new SimpleListener();
        this.listener = listener;
    }

    private void init(Context context) {
        inflate(context, R.layout.error_view, this);
        ButterKnife.inject(this);
        listener = new SimpleListener();
    }

    @OnClick(R.id.primary_action)
    protected void onPrimaryActionClicked() {
        listener.onPrimaryActionClicked();
    }

    @OnClick(R.id.primary_action)
    protected void onSecondaryActionClicked() {
        listener.onSecondaryActionClicked();
    }
}
