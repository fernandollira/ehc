package aiec.br.ehc.helper;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import aiec.br.ehc.R;

/**
 * Helper para facilitar a criação de animação
 */
public class AnimationHelper {
    public Context context;

    public AnimationHelper(Context context) {
        this.context = context;
    }

    public static Animation getRotateAroundSelfCenter(long duration)
    {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(duration);
        return anim;
    }

    public AnimationDrawable getFrameAnimation(ImageView img, String drawableXmlName, boolean reverse) {
        int resID = ResourceHelper.from(context).getIdentifierFromDrawable(drawableXmlName);
        img.setImageResource(resID);
        AnimationDrawable drawable = (AnimationDrawable) img.getDrawable();

        if (reverse) {
            drawable = copyReversedAnim(drawable);
            img.setImageDrawable(drawable);
        }

        drawable.setOneShot(false);
        return drawable;
    }

    private AnimationDrawable copyReversedAnim(AnimationDrawable src){
        AnimationDrawable newAnim = new AnimationDrawable();
        int numberOfFrame = src.getNumberOfFrames();

        for(int i = 0; i < numberOfFrame; i++){
            newAnim.addFrame(
                    src.getFrame(numberOfFrame - i - 1),
                    src.getDuration(numberOfFrame - i - 1));
        }
        newAnim.setOneShot(false);

        return newAnim;
    }
}
