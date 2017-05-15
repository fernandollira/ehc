package aiec.br.ehc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import aiec.br.ehc.adapter.EnvironmentAdapter;
import aiec.br.ehc.dao.EnvironmentDAO;
import aiec.br.ehc.dialog.EnvironmentEditDialog;
import aiec.br.ehc.helper.SharedPreferenceHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Place;

public class EnvironmentActivity extends AppCompatActivity {
    private Place place;
    private RecyclerView rViewEnvironments;
    private List<Environment> environments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.rViewEnvironments = (RecyclerView) findViewById(R.id.environment_list);
        registerForContextMenu(rViewEnvironments);

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        // recebe o objeto serializado do local
        this.place = (Place) getIntent().getParcelableExtra("EXTRA_PLACE");

        // obtem todos os ambientes para do local
        EnvironmentDAO dao = new EnvironmentDAO(this);
        environments = dao.getAllFromPlace(this.place);

        // define o título (Nome do local)
        String title = String.format("%s / %s", place.getName(), place.getDescription());
        this.setTitle(title);

        this.addEventForCreateEnvironment();
        this.fillEnvironmentList();
    }

    /**
     * Prenche a lista com os itens de ambientes para o local selecionado
     */
    public void fillEnvironmentList()
    {
        // determina o layout para utilização do tipo Grade
        GridLayoutManager lLayout = new GridLayoutManager(EnvironmentActivity.this, 2);
        rViewEnvironments.setLayoutManager(lLayout);
        rViewEnvironments.setHasFixedSize(true);

        // inclui o adapter
        EnvironmentAdapter adapter = new EnvironmentAdapter(this, environments);
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
    protected void onDestroy() {
        // se há itens cadastrados, salva o local como preferência do usuário
        if (rViewEnvironments.getChildCount() > 0) {
            SharedPreferenceHelper.from(this).savePreference(
                    SharedPreferenceHelper.PLACE_PREFERENCE_DEFAULT,
                    this.place.getId()
            );
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getFlags() == Intent.FLAG_ACTIVITY_NO_HISTORY) {
            Intent it = new Intent(EnvironmentActivity.this, PlaceActivity.class);
            startActivity(it);
        }

        super.onBackPressed();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuDelete = menu.add("Excluir");
        menuDelete.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        EnvironmentDAO dao = new EnvironmentDAO(EnvironmentActivity.this);
                        Toast.makeText(EnvironmentActivity.this, "Em desenvolvimento", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
        );
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
