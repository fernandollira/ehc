package aiec.br.ehc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import aiec.br.ehc.adapter.PlaceAdapter;
import aiec.br.ehc.dao.PlaceDAO;
import aiec.br.ehc.dialog.PlaceEditDialog;
import aiec.br.ehc.helper.SharedPreferenceHelper;
import aiec.br.ehc.helper.SystemUiHelper;
import aiec.br.ehc.model.Place;

public class PlaceActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listViewPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // define a visibilidade de barra de navegação
        SystemUiHelper.from(this).hideNavegationBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        listViewPlaces = (ListView) this.findViewById(R.id.place_list_items);
        registerForContextMenu(listViewPlaces);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.addCreateEvent();
        this.addEventPlaceList();
    }

    /**
     * Preenche todos os items de locais na lista
     */
    public void fillPlaces() {
        fillPlaces(false);
    }

    /**
     * Preenche todos os items de locais na lista
     *
     * @param  scrollToBottom   Rolar para o final da lista?
     */
    public void fillPlaces(boolean scrollToBottom)
    {
        PlaceDAO dao = new PlaceDAO(this);
        List<Place> places = dao.getAll();

        PlaceAdapter adapter = new PlaceAdapter(this, places);
        listViewPlaces.setAdapter(adapter);
        listViewPlaces.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        if (places.size() > 5) {
            listViewPlaces.setStackFromBottom(scrollToBottom);
        }

        dao.close();
    }

    /**
     * Adiciona as ações para o evento de click no item da lista de locais
     */
    private void addEventPlaceList()
    {
        listViewPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place place = (Place) listViewPlaces.getItemAtPosition(position);
                Intent it = new Intent(PlaceActivity.this, EnvironmentActivity.class);
                it.putExtra("EXTRA_PLACE", place);
                startActivity(it);
            }
        });
    }

    /**
     * Adiciona o envento ao botão para adicionar novos itens
     */
    private void addCreateEvent() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_place_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place place = new Place();
                PlaceEditDialog placeEditor = new PlaceEditDialog(PlaceActivity.this, place);
                placeEditor.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.fillPlaces();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Place place = (Place) listViewPlaces.getItemAtPosition(info.position);
        final SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.from(PlaceActivity.this);
        final Integer placeDefault = preferenceHelper.getIntegerOf(SharedPreferenceHelper.PLACE_PREFERENCE_DEFAULT);

        // verifica se o local fora definido como padrão
        final Boolean isPlaceDefault = placeDefault.equals(place.getId());
        String menuDefaultTitle = isPlaceDefault ? getString(R.string.unmark_default) : getString(R.string.mark_default);
        MenuItem menuDefault = menu.add(menuDefaultTitle);
        menuDefault.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int newDefaultPlaceId = isPlaceDefault ? 0 : place.getId();
                preferenceHelper.savePreference(
                        SharedPreferenceHelper.PLACE_PREFERENCE_DEFAULT,
                        newDefaultPlaceId
                );
                fillPlaces();
                return false;
            }
        });

        MenuItem menuEdit = menu.add(R.string.edit);
        menuEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                PlaceEditDialog dialog = new PlaceEditDialog(PlaceActivity.this, place);
                dialog.show();
                return false;
            }
        });

        MenuItem menuCredentials = menu.add(R.string.access_credentials);
        menuCredentials.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent it = new Intent(PlaceActivity.this, CredentialActivity.class);
                it.putExtra("EXTRA_PLACE", place);
                startActivity(it);
                return false;
            }
        });

        MenuItem menuDelete = menu.add(R.string.delete);
        menuDelete.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        PlaceDAO dao = new PlaceDAO(PlaceActivity.this);
                        dao.delete(place.getId());
                        dao.close();
                        fillPlaces();
                        Toast.makeText(PlaceActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_place:
                break;
            case R.id.nav_environment:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_about:
                Intent it = new Intent(this, AboutActivity.class);
                startActivity(it);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
