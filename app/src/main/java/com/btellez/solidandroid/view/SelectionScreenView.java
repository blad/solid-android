package com.btellez.solidandroid.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import hugo.weaving.DebugLog;

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
        public void onSeachGroupSelected();
        public void onRecentGroupSelected();
        public void onSubmitSearchQuery(String query);
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

    @DebugLog
    private void setState(State state) {
        switch (state) {
            case ShowSelection:
                hideOverlay();
                break;
            case ShowOverlay:
                showOverlay();
                setError(0);
                break;
        }
    }

    @DebugLog
    private void showOverlay() {
        if (!isVisible(overlay)) {
            overlay.setVisibility(VISIBLE);
            fadeIn(overlayBackground, 250, 0);
            fadeIn(searchInputGroup, 500, 200);

            // Show Keyboard:
            searchInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(searchInput.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @DebugLog
    private void hideOverlay() {
        if (isVisible(overlay)) {
            Animator.AnimatorListener onAnimationEnd = new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    overlay.setVisibility(INVISIBLE);
                }
            };
            fadeOut(overlayBackground, 500, 0, null);
            fadeOut(searchInputGroup, 250, 200, onAnimationEnd);
            setError(0);

            // Hide Keyboard:
            searchInput.clearFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }
    
    private boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    private void fadeOut(View view, long duration, long delay, Animator.AnimatorListener animatorListener) {
        ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(delay);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (animatorListener!=null) {
            objectAnimator.addListener(animatorListener);
        }
        objectAnimator.start();
    }

    private void fadeIn(View view, long duration, long delay) {
        view.setAlpha(0);
        view.setVisibility(VISIBLE);
        ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(delay);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    private void setError(int resString) {
        if (resString == 0) {
            fadeOut(error, 250, 0, null);
        } else {
            error.setText(resString);
            fadeIn(error, 250, 0);
        }
    }

    private class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override public void onAnimationStart(Animator animation) {}
        @Override public void onAnimationEnd(Animator animation) {}
        @Override public void onAnimationCancel(Animator animation) {}
        @Override public void onAnimationRepeat(Animator animation) {}
    }
}
