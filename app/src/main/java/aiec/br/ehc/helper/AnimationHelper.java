package aiec.br.ehc.helper;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Helper para facilitar a criação de animação
 */
public class AnimationHelper {
    public static Animation getRotateAroundSelfCenter(long duration)
    {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(duration);
        anim.start();
        return anim;
    }
}
