package com.edu.nuc.selftip;
import android.animation.TypeEvaluator;

public class KickBackAnimator implements TypeEvaluator<Float> {

    private final float s = 1.70158f;
    float mDuration = 0f;

    public void setDuration(float duration) {
        mDuration = duration;
    }

    public Float evaluate(float fraction, Float startValue, Float endValue) {
        float t = mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = mDuration;
        float result = calculate(t, c, b, d);
        return result;
    }

    public Float calculate(float t, float c, float b, float d) {
        return b * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + c;
    }
}