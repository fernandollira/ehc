package aiec.br.ehc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import aiec.br.ehc.dao.PlaceDAO;
import aiec.br.ehc.helper.SharedPreferenceHelper;
import aiec.br.ehc.model.Place;

public class SplashActivity extends AppCompatActivity {
     private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  int place_id = SharedPreferenceHelper.from(getBaseContext()).getPreferenceForDefaultPlace();
                  Class activityClass = place_id > 0 ? EnvironmentActivity.class : PlaceActivity.class;
                  Intent it = new Intent(SplashActivity.this, activityClass);

                  // Se o usuário tem um local preferido definido, logo vamos direto para tela de ambientes deste local
                  if (place_id > 0) {
                      PlaceDAO dao = new PlaceDAO(getBaseContext());
                      Place place = dao.getById(place_id);
                      it.putExtra("EXTRA_PLACE", place);
                      it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                  }

                 startActivity(it);
                 finish();
              }
       }, SPLASH_TIME_OUT);
    }
}
