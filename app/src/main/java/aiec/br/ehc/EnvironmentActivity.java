package aiec.br.ehc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import aiec.br.ehc.adapter.EnvironmentAdapter;
import aiec.br.ehc.dao.EnvironmentDAO;
import aiec.br.ehc.dialog.EnvironmentEditDialog;
import aiec.br.ehc.helper.SystemUiHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Place;

public class EnvironmentActivity extends AppCompatActivity {
    private Place place;
    private RecyclerView rViewEnvironments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.rViewEnvironments = (RecyclerView) findViewById(R.id.environment_list);
        registerForContextMenu(rViewEnvironments);

        // define a visibilidade de barra de navegação
        SystemUiHelper.from(this).fullScreenMode();

        // recebe o objeto serializado do local
        this.place = (Place) getIntent().getParcelableExtra("EXTRA_PLACE");

        // define o título (Nome do local)
        String title = String.format("%s / %s", place.getName(), place.getDescription());
        this.setTitle(title);

        this.addEventForCreateEnvironment();
        this.fillEnvironmentList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Prenche a lista com os itens de ambientes para o local selecionado
     */
    public void fillEnvironmentList()
    {
        EnvironmentDAO dao = new EnvironmentDAO(this);
        List<Environment> environments = dao.getAllFromPlace(this.place);

        // determina o layout para utilização do tipo Grade
        GridLayoutManager lLayout = new GridLayoutManager(EnvironmentActivity.this, 2);
        rViewEnvironments.setLayoutManager(lLayout);
        rViewEnvironments.setHasFixedSize(true);

        // inclui o adapter
        EnvironmentAdapter adapter = new EnvironmentAdapter(this, environments, this.place);
        rViewEnvironments.setAdapter(adapter);
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
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getFlags() == Intent.FLAG_ACTIVITY_NO_HISTORY) {
            Intent it = new Intent(EnvironmentActivity.this, PlaceActivity.class);
            startActivity(it);
        }

        super.onBackPressed();
    }
}
