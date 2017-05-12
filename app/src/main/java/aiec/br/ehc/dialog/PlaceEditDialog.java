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
import android.widget.Toast;
import aiec.br.ehc.MainActivity;
import aiec.br.ehc.R;
import aiec.br.ehc.adapter.ImageArrayAdapter;
import aiec.br.ehc.model.Place;

/**
 * Cria uma dialog em forma de um formulário que permite a edição de um local
 */
public class PlaceEditDialog extends Dialog implements View.OnClickListener {
    private final Place place;
    private final Activity activity;

    public PlaceEditDialog(Activity activity, Place place) {
        super(activity);
        this.activity = activity;
        this.place = place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.place_edit_dialog);
        Button btnYes = (Button) findViewById(R.id.btn_yes);
        Button btnNo = (Button) findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        String[] images = getContext().getResources().getStringArray(R.array.object_place_icons);
        final Spinner spinner = (Spinner) findViewById(R.id.place_edit_icon);
        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(getContext(), images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setBackgroundResource(R.color.colorPrimaryDark);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinner.getLayoutParams();
        params.height = 256;
        spinner.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                EditText txtName = (EditText) findViewById(R.id.place_edit_name);
                EditText txtDescription = (EditText) findViewById(R.id.place_edit_description);
                EditText txtHost = (EditText) findViewById(R.id.place_edit_host);
                EditText txtPort = (EditText) findViewById(R.id.place_edit_port);
                Spinner spinner = (Spinner)findViewById(R.id.place_edit_icon);

                String port = txtPort.getText().toString();
                if (port.isEmpty() ) {
                    port = getContext().getString(R.string.default_place_port);
                }

                if (txtName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "O campo 'Nome' é obrigatório!", Toast.LENGTH_LONG).show();
                    txtName.requestFocus();
                    return;
                }

                if (txtHost.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "O campo 'Host' é obrigatório!", Toast.LENGTH_LONG).show();
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
                MainActivity activity = (MainActivity) this.activity;
                activity.fillPlaces();
                break;

            case R.id.btn_no:
                dismiss();
                break;
        }

        dismiss();
    }
}
