package com.btellez.solidandroid.view;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.utility.Strings;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SelectionScreenView extends FrameLayout {

    // Static Screen items:
    @InjectView(R.id.search) View searchGroup;
    @InjectView(R.id.recent) View recentGroup;

    // Views to be animated in:
    @InjectView(R.id.overlay) RelativeLayout overlay;
    @InjectView(R.id.dark_background) View overlayBackground;
    @InjectView(R.id.search_input_group) View searchInputGroup;
    @InjectView(R.id.search_input) EditText searchInput;
    @InjectView(R.id.error) TextView error;
    @InjectView(R.id.submit) View submit;

    private Listener listener = new SimpleListener();

    private enum State {ShowSelection, ShowOverlay}

    /**
     * Interface to alert any listeners about events
     * in our view. This helps to keep non-view logic out of
     * the the view.
     */
    public interface Listener {
        void onSeachGroupSelected();
        void onRecentGroupSelected();
        void onSubmitSearchQuery(String query);
    }

    public static class SimpleListener implements Listener {
        @Override public void onSeachGroupSelected() {}
        @Override public void onRecentGroupSelected() {}
        @Override public void onSubmitSearchQuery(String query) {}
    }

    public SelectionScreenView(Context context) {
        super(context);
        init(context);
    }

    public SelectionScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectionScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectionScreenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setListener(Listener listener) {
        if (listener == null)
            listener = new SimpleListener();
        this.listener = listener;
    }

    private void init(Context context) {
        inflate(context, R.layout.activity_selection, this);
        ButterKnife.inject(this);
        setState(State.ShowSelection);
    }

    @OnClick(R.id.dark_background)
    protected void onOverlayBackgroundClicked() {
        setState(State.ShowSelection);
    }

    @OnClick(R.id.search)
    protected void onSearchSelected() {
        listener.onSeachGroupSelected();
        setState(State.ShowOverlay);
    }

    @OnClick(R.id.recent)
    protected void onRecentSelected() {
        listener.onRecentGroupSelected();
    }

    @OnClick(R.id.submit)
    protected void onSubmitClicked() {
        String query = searchInput.getText().toString();
        if (Strings.isEmpty(query)) {
            setError(R.string.input_error);
        } else {
            listener.onSubmitSearchQuery(searchInput.getText().toString());
        }
    }

    private void setState(State state) {
        switch (state) {
            case ShowSelection:
                setKeyboardVisibility(View.GONE);
                hideOverlay();
                break;
            case ShowOverlay:
                showOverlay();
                resetError();
                setKeyboardVisibility(View.VISIBLE);
                break;
        }
    }

    private void showOverlay() {
        if (!isVisible(overlay)) {
            new FadeAnimator()
                    .show(overlay, 0, 0)
                    .show(overlayBackground, 250, 0)
                    .show(searchInputGroup, 500, 200)
                    .start();
        }
    }

    private void hideOverlay() {
        if (isVisible(overlay)) {
            new FadeAnimator()
                    .hide(overlayBackground, 500, 0)
                    .hide(searchInputGroup, 250, 200)
                    .withHideListener(new FadeAnimator.SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            overlay.setVisibility(INVISIBLE);
                        }
                    })
                    .start();
        }
    }

    private void setKeyboardVisibility(int visiblity) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (visiblity == View.VISIBLE) {
            searchInput.requestFocus();
            imm.toggleSoftInputFromWindow(searchInput.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        } else {
            searchInput.clearFocus();
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    private boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    private void resetError() {
        new FadeAnimator()
                .hide(error, 250, 0)
                .start();
    }

    private void setError(int resString) {
        error.setText(resString);
        new FadeAnimator()
                .show(error, 250, 0)
                .start();
    }
}
