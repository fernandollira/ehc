package aiec.br.ehc.helper;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * Classe para facilitar o uso de recursos do user interface do sistema
 */
public class SystemUiHelper {
    private Activity activity;

    public SystemUiHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * Facilita a utilização desta classe
     *
     * @param activity   Activity
     * @return SystemUiHelper
     */
    public static SystemUiHelper from(Activity activity) {
        return new SystemUiHelper(activity);
    }

    /**
     * Define as configurações para não exibição da barra de navegação
     */
    public void hideNavegationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * Define a como tela cheia, oculta barra de nevegação e notificação
     */
    public void fullScreenMode()
    {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                       | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                       | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                       | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                       | View.SYSTEM_UI_FLAG_FULLSCREEN
                       | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
