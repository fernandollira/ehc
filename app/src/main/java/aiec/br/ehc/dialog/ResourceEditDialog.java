package aiec.br.ehc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import aiec.br.ehc.R;
import aiec.br.ehc.ResourceActivity;
import aiec.br.ehc.adapter.ImageArrayAdapter;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Resource;
import aiec.br.ehc.parameter.TurnOnOffFragment;

/**
 * Cria uma dialog em forma de um formulário que permite a edição de um local
 */
public class ResourceEditDialog extends Dialog implements View.OnClickListener {
    private final Resource resource;
    private final Environment environment;
    private final ResourceActivity activity;
    private EditText txtName;
    private TextView txtDescription;
    private Spinner spinner;
    private TextView txtTitle;
    private FrameLayout frameParameter;

    public ResourceEditDialog(Activity activity, Environment environment, Resource resource) {
        super(activity);
        this.activity = (ResourceActivity) activity;
        this.environment = environment;
        this.resource = resource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_resource);
        Button btnYes = (Button) findViewById(R.id.btn_yes);
        Button btnNo = (Button) findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        String[] images = getContext().getResources().getStringArray(R.array.object_resource_icons);

        txtTitle = (TextView) findViewById(R.id.resource_dialog_title);
        txtName = (EditText) findViewById(R.id.resource_edit_name);
        txtDescription = (TextView) findViewById(R.id.resource_view_description);
        spinner = (Spinner) findViewById(R.id.resource_edit_icon);

        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(getContext(), images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setBackgroundResource(R.color.colorPrimaryDark);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinner.getLayoutParams();
        params.height = 256;
        spinner.setLayoutParams(params);

        if (resource.getId() != null) {
            txtTitle.setText(this.activity.getString(R.string.edit_resource));
            txtName.setText(resource.getName());
            txtDescription.setText(resource.getDescription());
            int pos = spinnerAdapter.getPosition(resource.getIcon());
            spinner.setSelection(pos);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                if (txtName.getText().toString().isEmpty()) {
                    String message = getContext().getString(R.string.environment_name_required_message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    txtName.requestFocus();
                    return;
                }

                this.resource.setEnvironmentId(this.environment.getId());
                this.resource.setIcon(spinner.getSelectedItem().toString());
                this.resource.setName(txtName.getText().toString());
                this.resource.setDescription(txtDescription.getText().toString());
                this.resource.save(getContext());

                // Invocamos aqui o método da activity para atualizar a lista
                ResourceActivity activity = (ResourceActivity) this.activity;
                activity.fillResourceList();
                break;

            case R.id.btn_no:
                dismiss();
                break;
        }

        dismiss();
    }
}
