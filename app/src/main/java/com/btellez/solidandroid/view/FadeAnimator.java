package com.btellez.solidandroid.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

public class FadeAnimator {
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private Animator.AnimatorListener hideListener = new SimpleAnimatorListener();
    private Animator.AnimatorListener showListener = new SimpleAnimatorListener();
    private List<ObjectAnimator> showAnimations = new ArrayList<>();
    private List<ObjectAnimator> hideAnimations = new ArrayList<>();

    public FadeAnimator hide(View view, long duration, long delay) {
        if (view != null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
            objectAnimator.setDuration(duration);
            objectAnimator.setStartDelay(delay);
            objectAnimator.setInterpolator(interpolator);
            hideAnimations.add(objectAnimator);
        }
        return this;
    }

    public FadeAnimator show(View view, long duration, long delay) {
        if (view != null) {
            view.setAlpha(0);
            view.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
            objectAnimator.setDuration(duration);
            objectAnimator.setStartDelay(delay);
            objectAnimator.setInterpolator(interpolator);
            showAnimations.add(objectAnimator);
        }
        return this;
    }

    public FadeAnimator withShowListener(Animator.AnimatorListener listener) {
        this.hideListener = validate(listener);
        return this;
    }
    
    public FadeAnimator withHideListener(Animator.AnimatorListener listener) {
        this.hideListener = validate(listener);
        return this;
    }
    
    private Animator.AnimatorListener validate(Animator.AnimatorListener listener) {
        if (listener == null)
            listener = new SimpleAnimatorListener();
        return listener;
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = new AccelerateDecelerateInterpolator();
        }
        this.interpolator = interpolator;
    }

    public void start() {
        startAnimations(hideAnimations, hideListener);
        startAnimations(showAnimations, showListener);
    }
    
    private void startAnimations(List<ObjectAnimator> animations, Animator.AnimatorListener listener) {
        for (ObjectAnimator animator : animations) {
            animator.addListener(listener);
            animator.start();
        }
    }

    public static class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override public void onAnimationStart(Animator animation) {}
        @Override public void onAnimationEnd(Animator animation) {}
        @Override public void onAnimationCancel(Animator animation) {}
        @Override public void onAnimationRepeat(Animator animation) {}
    }
}
