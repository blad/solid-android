package com.btellez.solidandroid.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class FadeAnimator {
    private Animator.AnimatorListener listener = new SimpleAnimatorListener();
    private List<ObjectAnimator> animations = new ArrayList<>();

    public FadeAnimator fadeOut(View view, long duration, long delay) {
        if (view != null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
            objectAnimator.setDuration(duration);
            objectAnimator.setStartDelay(delay);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.addListener(listener);
            animations.add(objectAnimator);
        }
        return this;
    }

    public FadeAnimator fadeIn(View view, long duration, long delay) {
        if (view != null) {
            view.setAlpha(0);
            view.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
            objectAnimator.setDuration(duration);
            objectAnimator.setStartDelay(delay);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.addListener(listener);
            objectAnimator.start();
            animations.add(objectAnimator);
        }
        return this;
    }

    public FadeAnimator withListener(Animator.AnimatorListener listener) {
        if (listener == null)
            listener = new SimpleAnimatorListener();
        this.listener = listener;
        return this;
    }

    public void start() {
        for (ObjectAnimator animator : animations) {
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
