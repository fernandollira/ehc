package aiec.br.ehc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import aiec.br.ehc.adapter.PlaceAdapter;
import aiec.br.ehc.dao.PlaceDAO;
import aiec.br.ehc.dialog.PlaceEditDialog;
import aiec.br.ehc.model.Place;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listViewPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }

    /**
     * Preenche todos os items de locais na lista
     */
    public void fillPlaces()
    {
        PlaceDAO dao = new PlaceDAO(this);
        List<Place> places = dao.getAll();

        PlaceAdapter adapter = new PlaceAdapter(this, places);
        listViewPlaces.setAdapter(adapter);
        dao.close();
    }

    /**
     * Adiciona o envento ao botão para adicionar novos itens
     */
    private void addCreateEvent() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place place = new Place();
                PlaceEditDialog placeEditor = new PlaceEditDialog(MainActivity.this, place);
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
        final MainActivity activity = this;

        MenuItem menuEditName = menu.add("Alterar nome");
        menuEditName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                activity.showAlertEditBox("Editar nome", "Entre com o nome desejado", "name", place);
                return false;
            }
        });

        MenuItem menuEditDescription = menu.add("Alterar descrição");
        menuEditDescription.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                activity.showAlertEditBox("Editar descrição", "Entre com a descrição do local", "description", place);
                return false;
            }
        });

        MenuItem menuChangeIcon = menu.add("Alterar ícone");

        MenuItem menuDelete = menu.add("Excluir local");
        menuDelete.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        PlaceDAO dao = new PlaceDAO(MainActivity.this);
                        dao.delete(place);
                        dao.close();
                        fillPlaces();
                        Toast.makeText(MainActivity.this, "Removido", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
        );
    }

    /**
     * Exibe uma caixa de dialogo com um input text para alteração de uma determinada propriedade
     * @param title     Titulo
     * @param message   Mensagem
     * @param field     Campo a ser modificado
     * @param place     Instância do ojeto local a ser alterado
     */
    public void showAlertEditBox(
            String title,
            String message,
            final String field,
            final Place place) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        alert.setView(editText);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PlaceDAO dao = new PlaceDAO(MainActivity.this);
                String newValue = editText.getText().toString();
                if (newValue == null || newValue.isEmpty()) {
                    String message = "O campo não pode ficar vazio.";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
                    return;
                }
                switch (field){
                    case "name":
                        place.setName(newValue);
                        break;
                    case "description":
                        place.setDescription(newValue);
                        break;
                    case "icon":
                        place.setIcon(newValue);
                        break;
                }

                dao.save(place);
                dao.close();
                fillPlaces();
            }
        });

        alert.setCancelable(true);
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
