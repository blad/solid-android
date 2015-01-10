package com.btellez.solidandroid.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.btellez.solidandroid.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchResultsView extends FrameLayout {
    
    @InjectView(R.id.result_list) ListView listView;
    @InjectView(R.id.progress_indicator) ProgressBar progress;

    protected EmptyView emptyView;
    private EmptyView.Listener emptyActionListener;
    private EmptyView.Listener errorActionListener;

    public enum State {Loading, Error, Empty, Loaded}

    public SearchResultsView(Context context) {
        super(context);
        init(context);
    }

    public SearchResultsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchResultsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchResultsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.activity_search_results, this);
        ButterKnife.inject(this);
        emptyView = new EmptyView(context);
        addView(emptyView);
        listView.setEmptyView(emptyView);
        setState(State.Loading);
    }
    
    public void setAdapter(BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void setEmptyActionListener(EmptyView.Listener listener) {
        if (listener == null)
            listener = new EmptyView.SimpleListener();
        this.emptyActionListener = listener;
    }

    public void setErrorActionListener(EmptyView.Listener listener) {
        if (listener == null)
            listener = new EmptyView.SimpleListener();
        this.errorActionListener = listener;
    }
    
    public void setState(State state) {
        switch (state) {
            case Loading:
                emptyView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
                break;
            case Error:
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.INVISIBLE);
                onErrorState();
                break;
            case Empty:
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.INVISIBLE);
                onEmptyState();
                break;
            case Loaded:
                emptyView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void onEmptyState() {
        emptyView.setHeadline(R.string.no_results);
        emptyView.setSubheadline(R.string.no_results_detail);
        emptyView.setPrimaryActionName(R.string.change_search);
        emptyView.setSecondaryActionName(R.string.go_back);
        emptyView.setListener(emptyActionListener);
    }

    private void onErrorState() {
        emptyView.setHeadline(R.string.unable_to_load);
        emptyView.setSubheadline(R.string.unable_to_load_detail);
        emptyView.setPrimaryActionName(R.string.try_again);
        emptyView.setSecondaryActionName(R.string.go_back);
        emptyView.setListener(errorActionListener);
    }
}
