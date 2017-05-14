package aiec.br.ehc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import aiec.br.ehc.dialog.EnvironmentEditDialog;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Place;

public class EnvironmentActivity extends AppCompatActivity {
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.place = (Place) getIntent().getParcelableExtra("EXTRA_PLACE");
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
        final Place place = this.place;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_environment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Environment environment = new Environment();
                EnvironmentEditDialog dialog = new EnvironmentEditDialog(EnvironmentActivity.this, place, environment);
                dialog.show();
                Snackbar.make(view, "Em desenvolvimento", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
