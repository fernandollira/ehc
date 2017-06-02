package aiec.br.ehc.fragment.place;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.AnimationHelper;
import aiec.br.ehc.model.Place;

/**
 * Fragmento para edição para autenticação via credenciais
 */
public class PlaceCredentialFragment extends Fragment implements IPlaceFragment {
    private EditText username;
    private EditText password;
    private Place place;
    private EditText credentialFlag;
    private TextView moreConfig;
    private View configBlock;
    private TextView configInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_credentials, container, false);
        ImageView icon = (ImageView) view.findViewById(R.id.place_credential_icon);
        icon.setColorFilter(Color.rgb(255, 255, 255));
        username = (EditText) view.findViewById(R.id.place_credential_username);
        password = (EditText) view.findViewById(R.id.place_credential_password);
        credentialFlag = (EditText) view.findViewById(R.id.place_send_credential_tag);
        moreConfig = (TextView) view.findViewById(R.id.place_credential_advanced_options);
        configBlock = view.findViewById(R.id.place_send_credential_block);
        configInfo = (TextView) view.findViewById(R.id.place_credential_label);

        place = getArguments().getParcelable("place");
        String flag = String.format(getString(R.string.send_credential_tag), place.getCredentialFlag());
        configInfo.setText(flag);
        if (place.getUserCredentials() != null && place.getUserCredentials().contains(":")) {
            String[] credentials = place.getUserCredentials().split(":");
            if (credentials.length == 2) {
                username.setText(credentials[0]);
                password.setText(credentials[1]);
            }
        }

        this.addParamTokenEditEvent();
        this.addMoreOptionsEvents();
        return view;
    }

    public boolean isValid()
    {
        // Os campos são obrigatórios somente no caso do preenchimento de pelo menos um campo
        if (username.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
            return true;
        }

        if (username.getText().toString().isEmpty()) {
            username.setError(getString(R.string.required_field_message));
            username.requestFocus();
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError(getString(R.string.required_field_message));
            password.requestFocus();
            return false;
        }

        return true;
    }

    public void fillPlace()
    {
        String user = username.getText().toString().trim();
        String credentials = "";
        if (!user.isEmpty()) {
            credentials = user.concat(":").concat(password.getText().toString().trim());
        }

        place.setUserCredentials(credentials);
        place.setCredentialFlag(credentialFlag.getText().toString());
    }

    private void addMoreOptionsEvents() {
        AnimationHelper.collapse(configBlock);
        moreConfig.setTag("collapsed");
        final Drawable iconMore = getContext().getDrawable(R.drawable.add_circle_white);
        final Drawable iconLess = getContext().getDrawable(R.drawable.less_circle_white);
        iconMore.setBounds(0, 0, 40, 40);
        iconLess.setBounds(0, 0, 40, 40);
        this.moreConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreConfig.getTag() == "collapsed") {
                    moreConfig.setTag("expand");
                    moreConfig.setCompoundDrawables(iconLess, null, null, null);
                    AnimationHelper.expand(configBlock);
                }
                else {
                    moreConfig.setTag("collapsed");
                    moreConfig.setCompoundDrawables(iconMore, null, null, null);
                    AnimationHelper.collapse(configBlock);
                }
            }
        });
    }

    private void addParamTokenEditEvent() {
        credentialFlag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                configInfo.setText(String.format(getString(R.string.send_credential_tag), s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
