package aiec.br.ehc.fragment.place;

import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.AnimationHelper;
import aiec.br.ehc.model.Place;

/**
 * Fragmento para edição para autenticação via credenciais
 */
public class PlaceTokenFragment extends Fragment implements IPlaceFragment {
    private EditText token;
    private Place place;
    private TextView moreOptions;
    private View tokenAdvancedOptions;
    private RadioGroup rgSendTokenOption;
    private EditText paramToken;
    private RadioButton rbTokenHeader;
    private RadioButton rbTokenUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_token, container, false);
        ImageView icon = (ImageView) view.findViewById(R.id.place_token_icon);
        icon.setColorFilter(Color.rgb(255, 255, 255));
        token = (EditText) view.findViewById(R.id.place_token);
        moreOptions = (TextView) view.findViewById(R.id.place_token_advanced_options);
        tokenAdvancedOptions = view.findViewById(R.id.place_send_token_block);
        rgSendTokenOption = (RadioGroup) view.findViewById(R.id.place_send_token_type);
        paramToken = (EditText) view.findViewById(R.id.place_send_token_param);
        rbTokenHeader = (RadioButton) view.findViewById(R.id.place_send_token_type_header);
        rbTokenUrl = (RadioButton) view.findViewById(R.id.place_send_token_type_query_string);

        place = getArguments().getParcelable("place");

        String flag = place.getTokenFlag();
        rbTokenHeader.setText(String.format(getString(R.string.send_token_by_headers), flag));
        rbTokenUrl.setText(String.format(getString(R.string.send_token_by_headers), flag));
            if (place.getAccessToken() != null) {
            token.setText(place.getAccessToken());
        }

        int item = place.isSendTokenByUrl() ? R.id.place_send_token_type_query_string : R.id.place_send_token_type_header;
        rgSendTokenOption.check(item);

        if (place.getTokenFlag() != null) {
            paramToken.setText(place.getTokenFlag());
        }

        this.addMoreOptionsEvents();
        this.addParamTokenEditEvent();

        return view;
    }

    private void addMoreOptionsEvents() {
        AnimationHelper.collapse(tokenAdvancedOptions);
        moreOptions.setTag("collapsed");
        this.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreOptions.getTag() == "collapsed") {
                    moreOptions.setTag("expand");
                    AnimationHelper.expand(tokenAdvancedOptions);
                }
                else {
                    moreOptions.setTag("collapsed");
                    AnimationHelper.collapse(tokenAdvancedOptions);
                }
            }
        });
    }

    private void addParamTokenEditEvent() {
        paramToken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rbTokenHeader.setText(String.format(getString(R.string.send_token_by_headers), s));
                rbTokenUrl.setText(String.format(getString(R.string.send_token_by_headers), s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isValid()
    {
        return true;
    }

    public void fillPlace()
    {
        place.setAccessToken(token.getText().toString());
        String method = place.TOKEN_SEND_BY_URL;
        if (rgSendTokenOption.getCheckedRadioButtonId() == R.id.place_send_token_type_header) {
            method = place.TOKEN_SEND_BY_HEADERS;
        }
        place.setTokenSendMethod(method);
        place.setTokenFlag(paramToken.getText().toString());
    }
}
