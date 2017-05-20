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
import aiec.br.ehc.PlaceActivity;
import aiec.br.ehc.R;
import aiec.br.ehc.adapter.ImageArrayAdapter;
import aiec.br.ehc.model.Place;

/**
 * Cria uma dialog em forma de um formulário que permite a edição de um local
 */
public class PlaceEditDialog extends Dialog implements View.OnClickListener {
    private final Place place;
    private final Activity activity;
    private Spinner spinner;
    private EditText txtName;
    private EditText txtDescription;
    private EditText txtHost;
    private EditText txtPort;
    private TextView txtTitle;

    public PlaceEditDialog(Activity activity, Place place) {
        super(activity);
        this.activity = activity;
        this.place = place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_place);
        Button btnYes = (Button) findViewById(R.id.btn_yes);
        Button btnNo = (Button) findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        String[] images = getContext().getResources().getStringArray(R.array.object_place_icons);
        spinner = (Spinner) findViewById(R.id.place_edit_icon);
        txtName = (EditText) findViewById(R.id.place_edit_name);
        txtDescription = (EditText) findViewById(R.id.place_edit_description);
        txtHost = (EditText) findViewById(R.id.place_edit_host);
        txtPort = (EditText) findViewById(R.id.place_edit_port);
        txtTitle = (TextView) findViewById(R.id.place_dialog_title);

        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(getContext(), images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setBackgroundResource(R.color.colorPrimaryDark);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinner.getLayoutParams();
        params.height = 256;
        spinner.setLayoutParams(params);

        if (place.getId() != null) {
            txtTitle.setText(this.activity.getString(R.string.edit_place));
            txtName.setText(place.getName());
            txtDescription.setText(place.getDescription());
            txtHost.setText(place.getHost());
            String port = place.getPort() != null ? place.getPort().toString() : "";
            txtPort.setText(port);
            int pos = spinnerAdapter.getPosition(place.getIcon());
            spinner.setSelection(pos);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                String port = txtPort.getText().toString();
                if (port.isEmpty() ) {
                    port = getContext().getString(R.string.default_place_port);
                }

                if (txtName.getText().toString().isEmpty()) {
                    String message = getContext().getString(R.string.place_name_required_message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    txtName.requestFocus();
                    return;
                }

                if (txtHost.getText().toString().isEmpty()) {
                    String message = getContext().getString(R.string.place_host_required_message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    txtHost.requestFocus();
                    return;
                }

                this.place.setIcon(spinner.getSelectedItem().toString());
                this.place.setName(txtName.getText().toString());
                this.place.setDescription(txtDescription.getText().toString());
                this.place.setHost(txtHost.getText().toString());
                this.place.setPort(Integer.parseInt(port));
                this.place.save(getContext());

                // Invocamos aqui o método da activity para atualizar a lista
                PlaceActivity activity = (PlaceActivity) this.activity;
                activity.fillPlaces(true);
                break;

            case R.id.btn_no:
                dismiss();
                break;
        }

        dismiss();
    }
}
