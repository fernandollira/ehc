package aiec.br.ehc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
    private RadioGroup rgProtocol;

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
        rgProtocol = (RadioGroup) findViewById(R.id.place_edit_protocol);

        ImageArrayAdapter spinnerAdapter = new ImageArrayAdapter(getContext(), images);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setBackgroundResource(R.color.colorPrimaryDark);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinner.getLayoutParams();
        params.height = 256;
        spinner.setLayoutParams(params);

        String http_port = getContext().getString(R.string.default_http_port);
        String portHint = String.format(getContext().getString(R.string.edit_place_port_hint), http_port);
        txtPort.setHint(portHint);
        if (place.getId() != null) {
            txtTitle.setText(this.activity.getString(R.string.edit_place));
            txtName.setText(place.getName());
            txtDescription.setText(place.getDescription());
            txtHost.setText(place.getHost());
            int item = place.isSecurityProtocol() ? R.id.place_protocol_https : R.id.place_protocol_http;
            rgProtocol.check(item);
            String port = place.getPort() != null ? place.getPort().toString() : "";
            txtPort.setText(port);
            int pos = spinnerAdapter.getPosition(place.getIcon());
            spinner.setSelection(pos);
        }

        this.addOnProtocolChange();
        this.addOnHostTextChange();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                String port = txtPort.getText().toString();
                if (port.isEmpty() ) {
                    port = getContext().getString(R.string.default_http_port);
                }

                String requiredMessage = getContext().getString(R.string.required_field_message);
                if (txtName.getText().toString().isEmpty()) {
                    txtName.setError(requiredMessage);
                    txtName.requestFocus();
                    return;
                }

                if (txtHost.getText().toString().isEmpty()) {
                    txtHost.setError(requiredMessage);
                    txtHost.requestFocus();
                    return;
                }

                RadioButton selProtocol = (RadioButton) findViewById(rgProtocol.getCheckedRadioButtonId());
                this.place.setIcon(spinner.getSelectedItem().toString());
                this.place.setName(txtName.getText().toString());
                this.place.setDescription(txtDescription.getText().toString());
                this.place.setProtocol(selProtocol.getText().toString());
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

    private void addOnHostTextChange()
    {
        final String allow_text_digits = "abcdefghijklmnopqrstuvwxyz1234567890.-_";
        final String allow_number_digits = "0123456789.";
        txtHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence value, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence value, int i, int i1, int i2) {
                if (value.length() == 1) {
                    if (TextUtils.isDigitsOnly(value)) {
                        txtHost.setInputType(InputType.TYPE_CLASS_NUMBER);
                        txtHost.setKeyListener(DigitsKeyListener.getInstance(allow_number_digits));
                    }
                    else {
                        txtHost.setKeyListener(DigitsKeyListener.getInstance(allow_text_digits));
                        txtHost.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                    }

                    txtHost.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    txtHost.setKeyListener(DigitsKeyListener.getInstance(allow_text_digits));
                    txtHost.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                }
            }
        });
    }

    /**
     * Adiciona o envento para escutar a alteração do protocolo
     */
    private void addOnProtocolChange()
    {
        rgProtocol.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int item) {
                int portResId = R.string.default_http_port;
                if (item == R.id.place_protocol_https) {
                    portResId = R.string.default_https_port;
                }

                String port = getContext().getString(portResId);
                String message = getContext().getString(R.string.edit_place_port_hint);
                txtPort.setHint(String.format(message, port));
            }
        });
    }
}
