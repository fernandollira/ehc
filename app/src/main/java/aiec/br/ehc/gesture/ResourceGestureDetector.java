package aiec.br.ehc.gesture;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Centraliza os comportarmento de gestos para a tela de recursos
 */
public class ResourceGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 500;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private final Activity activity;

    public ResourceGestureDetector(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            //right to left
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // implementacao futura
                return true;
            }
            //left to right
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                activity.onBackPressed();
                return true;
            }
        } catch (Exception e) {
            Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
        }
        return false;
    }
}
