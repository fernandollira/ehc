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

import aiec.br.ehc.R;
import aiec.br.ehc.model.Place;

/**
 * Fragmento para edição para autenticação via credenciais
 */
public class PlaceTokenFragment extends Fragment implements IPlaceFragment {
    private EditText token;
    private Place place;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_token, container, false);
        ImageView icon = (ImageView) view.findViewById(R.id.place_token_icon);
        icon.setColorFilter(Color.rgb(255, 255, 255));
        token = (EditText) view.findViewById(R.id.place_token);

        place = getArguments().getParcelable("place");
        if (place.getAccessToken() != null) {
            token.setText(place.getAccessToken());
        }

        return view;
    }

    public boolean isValid()
    {
        return true;
    }

    public void fillPlace()
    {
        place.setAccessToken(token.getText().toString());
    }
}
