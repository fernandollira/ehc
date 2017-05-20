package aiec.br.ehc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import aiec.br.ehc.EnvironmentActivity;
import aiec.br.ehc.R;
import aiec.br.ehc.adapter.ImageArrayAdapter;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Place;

/**
 * Cria uma dialog em forma de um formulário que permite a edição de um local
 */
public class EnvironmentEditDialog extends Dialog implements View.OnClickListener {
    private final Environment environment;
    private final Activity activity;
    private final Place place;
    private EditText txtName;
    private EditText txtDescription;
    private Spinner spinner;
    private TextView txtTitle;

    public EnvironmentEditDialog(Activity activity, Place place, Environment environment) {
        super(activity);
        this.activity = activity;
        this.environment = environment;
        this.place = place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_environment);
        Button btnYes = (Button) findViewById(R.id.btn_yes);
        Button btnNo = (Button) findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        String[] images = getContext().getResources().getStringArray(R.array.object_environment_icons);

        txtTitle = (TextView) findViewById(R.id.environment_dialog_title);
        txtName = (EditText) findViewById(R.id.environment_edit_name);
        txtDescription = (EditText) findViewById(R.id.environment_edit_description);
        spinner = (Spinner) findViewById(R.id.environment_edit_icon);

        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(getContext(), images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setBackgroundResource(R.color.colorPrimaryDark);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinner.getLayoutParams();
        params.height = 256;
        spinner.setLayoutParams(params);

        if (environment.getId() != null) {
            txtTitle.setText(this.activity.getString(R.string.edit_environment));
            txtName.setText(environment.getName());
            txtDescription.setText(environment.getDescription());
            int pos = spinnerAdapter.getPosition(environment.getIcon());
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

                this.environment.setPlaceId(this.place.getId());
                this.environment.setIcon(spinner.getSelectedItem().toString());
                this.environment.setName(txtName.getText().toString());
                this.environment.setDescription(txtDescription.getText().toString());
                this.environment.save(getContext());

                // Invocamos aqui o método da activity para atualizar a lista
                EnvironmentActivity activity = (EnvironmentActivity) this.activity;
                activity.fillEnvironmentList();
                break;

            case R.id.btn_no:
                dismiss();
                break;
        }

        dismiss();
    }
}
