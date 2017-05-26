package aiec.br.ehc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import aiec.br.ehc.adapter.ImageArrayAdapter;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Resource;
import aiec.br.ehc.parameter.IParameterFragment;
import aiec.br.ehc.parameter.TurnOnOffFragment;

public class ResourceEditorActivity extends AppCompatActivity {
    private Resource resource;
    private Environment environment;
    private EditText txtName;
    private TextView txtDescription;
    private Spinner spinner_icon;
    private Spinner spinner_http_method;

    private IParameterFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_editor);

        txtName = (EditText) findViewById(R.id.resource_edit_name);
        txtDescription = (TextView) findViewById(R.id.resource_view_description);
        spinner_icon = (Spinner) findViewById(R.id.resource_edit_icon);
        spinner_http_method = (Spinner) findViewById(R.id.resource_edit_method);

        // recebe os objetos serializados
        this.environment = getIntent().getParcelableExtra("EXTRA_ENVIRONMENT");
        this.resource = getIntent().getParcelableExtra("EXTRA_RESOURCE");
        if (this.resource == null) {
            this.resource = new Resource();
        }
        this.fillParameters();
        this.addButtonEvents();
    }

    /**
     * Adiciona os eventos para os botões Salvar e Cancelar
     */
    private void addButtonEvents()
    {
        final ResourceEditorActivity self = this;
        Button btnSave = (Button) findViewById(R.id.resource_btn_yes);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resource.setIcon(spinner_icon.getSelectedItem().toString());
                resource.setMethod(spinner_http_method.getSelectedItem().toString());
                resource.setName(txtName.getText().toString());
                resource.setEnvironmentId(environment.getId());
                resource.save(view.getContext());
                self.fragment.saveParameters();
                self.finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.resource_btn_no);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                self.finish();
            }
        });
    }

    /**
     * Preenche os parâmetros com base no recurso
     */
    private void fillParameters()
    {
        String[] images = getResources().getStringArray(R.array.object_resource_icons);
        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(ResourceEditorActivity.this, images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_icon.setAdapter(spinnerAdapter);
        spinner_icon.setBackgroundResource(R.color.colorPrimaryDark);

        this.setTitle(getString(R.string.add_resource));
        if (!resource.isNew()) {
            this.setTitle(getString(R.string.edit_resource));
            txtName.setText(resource.getName());
            txtDescription.setText(resource.getDescription());

            int pos = spinnerAdapter.getPosition(resource.getIcon());
            spinner_icon.setSelection(pos);

            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner_http_method.getAdapter();
            pos = adapter.getPosition(resource.getMethod());
            spinner_http_method.setSelection(pos);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        this.fragment = new TurnOnOffFragment();
        Bundle args = new Bundle();
        args.putParcelable("resource", resource);
        fragment.setArguments(args);
        tx.replace(R.id.frame_resource_parameter, (Fragment) fragment);
        tx.commit();
    }
}
