package aiec.br.ehc;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import aiec.br.ehc.adapter.ResourceAdapter;
import aiec.br.ehc.dao.ResourceDAO;
import aiec.br.ehc.dialog.ResourceEditDialog;
import aiec.br.ehc.gesture.ResourceGestureDetector;
import aiec.br.ehc.helper.SystemUiHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Resource;

public class ResourceActivity extends AppCompatActivity {
    private Environment environment;
    private RecyclerView rViewResources;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        SystemUiHelper.from(this).fullScreenMode();

        // recebe o objeto serializado do local
        this.environment = (Environment) getIntent().getParcelableExtra("EXTRA_ENVIRONMENT");

        this.rViewResources = (RecyclerView) findViewById(R.id.resource_list);

        mGestureDetector = new GestureDetector(this, new ResourceGestureDetector(this));
        rViewResources.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        this.setTitle(getString(R.string.resources));
        this.addEventForCreateResource();
        this.fillResourceList();
    }

    /**
     * Preenche a lista de recursos no GridView
     */
    public void fillResourceList() {
        ResourceDAO dao = new ResourceDAO(this);
        List<Resource> resources = dao.getAllFromEnvironment(this.environment);

        // determina o layout para utilização do tipo Grade
        GridLayoutManager lLayout = new GridLayoutManager(ResourceActivity.this, 3);
        rViewResources.setLayoutManager(lLayout);
        rViewResources.setHasFixedSize(true);

        // inclui o adapter
        ResourceAdapter adapter = new ResourceAdapter(this, resources, this.environment);
        rViewResources.setAdapter(adapter);
    }

    /**
     * Adiciona o comportamento para o botão adicionar local
     */
    public void addEventForCreateResource()
    {
        final Environment environment = this.environment;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_resource);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resource resource = new Resource();
                ResourceEditDialog dialog = new ResourceEditDialog(ResourceActivity.this, environment, resource);
                dialog.show();
            }
        });
    }
}
