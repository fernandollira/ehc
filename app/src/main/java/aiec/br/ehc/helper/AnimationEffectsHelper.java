package aiec.br.ehc.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import aiec.br.ehc.R;

/**
 * Classe responsável por aplicar os efeitos visuais às imagens
 */
public class AnimationEffectsHelper {
    private static final String ANIMATION_ROTATE = "rotate";
    private static final String ANIMATION_FRAME = "frame";
    public static final int SPEED_DEFAULT = 800;

    private final AnimationHelper helper;
    private Context context;
    private Bundle properties;

    public AnimationEffectsHelper(Context context, Bundle properties) {
        this.context = context;
        this.properties = properties;
        this.helper = new AnimationHelper(context);
    }

    public Context getContext() {
        return this.context;
    }

    public void applyEffects(ImageView image, String state) {
        String animationType = properties.getString("animation");
        if (animationType == null) {
            animationType = "none";
        }

        switch (animationType) {
            case ANIMATION_ROTATE:
                int speed = SPEED_DEFAULT;
                if (properties.getInt("speed") > 0 ) {
                    speed = properties.getInt("speed");
                }

                Animation anim = AnimationHelper.getRotateAroundSelfCenter(speed);
                image.setAnimation(anim);
                break;
            case ANIMATION_FRAME:
                AnimationDrawable drawable = helper.getFrameAnimation(image, properties.getString("drawable"), state.equals("off"));
                if (properties.getString("oneShot") != null) {
                    drawable.setOneShot(true);
                }
                if (state.equals("off") && !drawable.isOneShot()) {
                    drawable.stop();
                } else {
                    drawable.start();
                }
        }

        this.applyColorFilter(image, state);
    }

    /**
     * Altera a cor da imagem conforme definições
     *
     * @param image Image a ser alterada
     * @param state Stado atual (Ligado/Desligado)
     */
    public void applyColorFilter(ImageView image, String state) {
        String color = "#FFFFFF";
        if (state.equals("on")) {
            String newColor = properties.getString("color");
            color = (newColor != null) ? newColor : color;
        } else if (image.getAnimation() != null) {
            image.getAnimation().cancel();
        }

        image.setColorFilter(Color.parseColor(color));
    }
}
