package aiec.br.ehc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import aiec.br.ehc.MainActivity;
import aiec.br.ehc.R;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                EditText txtName = (EditText) findViewById(R.id.place_edit_name);
                EditText txtDescription = (EditText) findViewById(R.id.place_edit_description);
                EditText txtHost = (EditText) findViewById(R.id.place_edit_host);
                EditText txtPort = (EditText) findViewById(R.id.place_edit_port);

                if (txtName.getText() == null || txtName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "O campo 'Nome' é obrigatório!", Toast.LENGTH_LONG).show();
                    txtName.requestFocus();
                    return;
                }

                if (txtHost.getText() == null || txtHost.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "O campo 'Host' é obrigatório!", Toast.LENGTH_LONG).show();
                    txtHost.requestFocus();
                    return;
                }

                this.place.setName(txtName.getText().toString());
                this.place.setDescription(txtDescription.getText().toString());
                this.place.setHost(txtHost.getText().toString());
                this.place.setPort(Integer.parseInt(txtPort.getText().toString()));
                this.place.save(getContext());
                Toast.makeText(getContext(), "Local criado com sucesso!", Toast.LENGTH_LONG).show();
                MainActivity activity = (MainActivity) this.activity;
                activity.onResume();
                break;

            case R.id.btn_no:
                dismiss();
                break;
        }

        dismiss();
    }
}
