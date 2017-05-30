package aiec.br.ehc.fragment.place;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import aiec.br.ehc.R;
import aiec.br.ehc.model.Place;

/**
 * Fragmento para edição para autenticação via credenciais
 */
public class PlaceCredentialFragment extends Fragment implements IPlaceFragment {
    private EditText username;
    private EditText password;
    private Place place;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_credentials, container, false);
        ImageView icon = (ImageView) view.findViewById(R.id.place_credential_icon);
        icon.setColorFilter(Color.rgb(255, 255, 255));
        username = (EditText) view.findViewById(R.id.place_credential_username);
        password = (EditText) view.findViewById(R.id.place_credential_password);

        place = getArguments().getParcelable("place");
        if (place.getUserCredentials() != null && place.getUserCredentials().contains(":")) {
            String[] credentials = place.getUserCredentials().split(":");
            if (credentials.length == 2) {
                username.setText(credentials[0]);
                password.setText(credentials[1]);
            }
        }

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

    }
}
