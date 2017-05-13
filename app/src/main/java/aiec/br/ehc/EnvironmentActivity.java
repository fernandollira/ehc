package aiec.br.ehc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class EnvironmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.addEventForCreateEnvironment();
    }

    public void fillEnvironmentList()
    {
        return;
    }

    /**
     * Adiciona o comportamento para o botão adicionar local
     */
    public void addEventForCreateEnvironment()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_environment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Em desenvolvimento", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
