package aiec.br.ehc;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import aiec.br.ehc.adapter.ImageArrayAdapter;
import aiec.br.ehc.fragment.parameter.ReaderParameterFragment;
import aiec.br.ehc.helper.AnimationHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Resource;
import aiec.br.ehc.fragment.parameter.IParameterFragment;
import aiec.br.ehc.fragment.parameter.SwitchParameterFragment;

public class ResourceEditorActivity extends AppCompatActivity {
    private Resource resource;
    private Environment environment;
    private EditText txtName;
    private Spinner spinner_icon;
    private Spinner spinner_http_method;

    private IParameterFragment fragment;
    private String[] images;
    private Switch intensityControl;
    private View intensityBlock;
    private EditText txtMinValue;
    private EditText txtMaxValue;
    private EditText txtIntensityParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_editor);

        txtName = (EditText) findViewById(R.id.resource_edit_name);
        txtMinValue = (EditText) findViewById(R.id.resource_edit_min_value);
        txtMaxValue = (EditText) findViewById(R.id.resource_edit_max_value);
        txtIntensityParam = (EditText) findViewById(R.id.resource_edit_intensity_param);
        spinner_icon = (Spinner) findViewById(R.id.resource_edit_icon);
        spinner_http_method = (Spinner) findViewById(R.id.resource_edit_method);
        images = getResources().getStringArray(R.array.object_resource_icons);
        intensityControl = (Switch) findViewById(R.id.resource_intensity_control);
        intensityBlock = findViewById(R.id.resource_intensity_control_block);
        ImageView imgMax = (ImageView) findViewById(R.id.resource_intensity_max_icon);
        ImageView imgMin = (ImageView) findViewById(R.id.resource_intensity_min_icon);
        ImageView imgParam = (ImageView) findViewById(R.id.resource_intensity_param_icon);

        imgMax.setColorFilter(Color.rgb(255, 255, 255));
        imgMin.setColorFilter(Color.rgb(255, 255, 255));
        imgParam.setColorFilter(Color.rgb(255, 255, 255));

        // recebe os objetos serializados
        this.environment = getIntent().getParcelableExtra("EXTRA_ENVIRONMENT");
        this.resource = getIntent().getParcelableExtra("EXTRA_RESOURCE");
        AnimationHelper.collapse(intensityBlock);
        if (this.resource == null) {
            this.resource = new Resource();
        }

        // exibe a parte de controle de intensidade caso o recurso esteja habilitado pra tal
        if (resource.hasIntensityControl()) {
            AnimationHelper.expand(intensityBlock);
        }

        this.fillParameters();
        this.addButtonEvents();
        this.addSpinnerEvents();
        this.addSwitchIntensityEvents();
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
                resource.setIntensityParam(txtIntensityParam.getText().toString());
                resource.setMinValue(Integer.parseInt(txtMinValue.getText().toString()));
                resource.setMaxValue(Integer.parseInt(txtMaxValue.getText().toString()));
                if (txtName.getText().toString().isEmpty()) {
                    txtName.setError(getString(R.string.required_field_message));
                    txtName.requestFocus();
                    return;
                }

                resource.setName(txtName.getText().toString());
                resource.setEnvironmentId(environment.getId());
                resource.save(view.getContext());
                if (self.fragment.isValid()) {
                    self.fragment.saveParameters();
                    self.finish();
                }
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
        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(ResourceEditorActivity.this, this.images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_icon.setAdapter(spinnerAdapter);
        spinner_icon.setBackgroundResource(R.color.colorPrimaryDark);

        this.setTitle(getString(R.string.add_resource));
        txtMinValue.setText(resource.getMinValue().toString());
        txtMaxValue.setText(resource.getMaxValue().toString());

        if (!resource.isNew()) {
            this.setTitle(getString(R.string.edit_resource));
            txtName.setText(resource.getName());
            intensityControl.setChecked(resource.hasIntensityControl());
            txtIntensityParam.setText(resource.getIntensityParam());
            intensityControl.setText(getSwitchDescription(resource.hasIntensityControl()));
            int pos = spinnerAdapter.getPosition(resource.getIcon());
            spinner_icon.setSelection(pos);

            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner_http_method.getAdapter();
            pos = adapter.getPosition(resource.getMethod());
            spinner_http_method.setSelection(pos);
        }
    }

    private void addSpinnerEvents()
    {
        spinner_icon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle properties = (Bundle) parent.getSelectedView().getTag();
                setFragmentFrom(properties);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addSwitchIntensityEvents()
    {
        intensityControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isEnabled) {
                resource.setIntensityControl(isEnabled);
                if(isEnabled) {
                    AnimationHelper.expand(intensityBlock);
                }
                else {
                    AnimationHelper.collapse(intensityBlock);
                }

                compoundButton.setText(getSwitchDescription(isEnabled));
            }
        });
    }

    /**
     * Retorna a descrição do swith para habilitação/desabilitação de parâmetros de intensidade
     * @param isEnabled Habilitado?
     * @return descrição a ser utilizada
     */
    private String getSwitchDescription(Boolean isEnabled) {
        if (isEnabled) {
            return getString(R.string.intensity_control_deactivate);
        }

        return getString(R.string.intensity_control_active);
    }

    public void setFragmentFrom(Bundle properties)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        String type = properties.getString("type");
        switch (type) {
            case SwitchParameterFragment.TYPE:
                fragment = new SwitchParameterFragment();
                break;

            case ReaderParameterFragment.TYPE:
                intensityControl.setVisibility(View.GONE);
                fragment = new ReaderParameterFragment();
                break;
        }

        Bundle args = new Bundle();
        args.putParcelable("resource", resource);
        args.putBundle("properties", properties);
        fragment.setArguments(args);
        tx.replace(R.id.frame_resource_parameter, (Fragment) fragment);
        tx.commit();
    }
}
