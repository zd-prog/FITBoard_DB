package by.ziziko.fitboard;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

public class Animation {

    public static void Animate(View view, int colorFrom, int colorTo) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(colorFrom, colorTo);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(valueAnimator -> view.setBackgroundColor((Integer)valueAnimator.getAnimatedValue()));

        anim.setDuration(300);
        anim.start();
    }
}
