package com.osc.randochat.helper;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public class AnimateView {

    public static void startAnimation(final int view, final Activity activity , long duration) {
        final int start = Color.parseColor("#FFE53B");
        final int mid = Color.parseColor("#FFBE3B");
        final int end = Color.parseColor("#FF6F00");;


        final ArgbEvaluator evaluator = new ArgbEvaluator();
        View preloader = activity.findViewById(view);
        preloader.setVisibility(View.VISIBLE);
        final GradientDrawable gradient = (GradientDrawable) preloader.getBackground();
        ValueAnimator animator = TimeAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(valueAnimator -> {
            float fraction = valueAnimator.getAnimatedFraction();
            int newStart = (int) evaluator.evaluate(fraction, start, end);
            int newMid = (int) evaluator.evaluate(fraction, mid, start);
            int newEnd = (int) evaluator.evaluate(fraction, end, mid);
            int[] newArray = {newStart, newMid, newEnd};
            gradient.setColors(newArray);
        });
        animator.start();
    }




    public static void stopAnimation(final int view, final Activity activity){

        ObjectAnimator.ofFloat(activity.findViewById(view), "alpha", 0f).setDuration(125).start();
    }
}
