package aiec.br.ehc;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import aiec.br.ehc.fragment.place.IPlaceFragment;
import aiec.br.ehc.fragment.place.PlaceCredentialFragment;
import aiec.br.ehc.fragment.place.PlaceTokenFragment;
import aiec.br.ehc.model.Place;

public class CredentialActivity extends AppCompatActivity {

    private Place place;
    private RadioGroup rg_auth_type;
    private IPlaceFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);

        this.place = getIntent().getParcelableExtra("EXTRA_PLACE");
        this.rg_auth_type = (RadioGroup) findViewById(R.id.place_edit_authorization_type);
        int item = place.isAuthorizationByCredentials() ? R.id.place_auth_type_credentials : R.id.place_auth_type_token;
        rg_auth_type.check(item);

        this.addEventAuthType();
        this.addButtonEvents();
        chooseFragment();
    }

    private void addEventAuthType()
    {
        this.rg_auth_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.place_auth_type_token:
                        place.setAuthorizationByToken();
                        break;
                    case R.id.place_auth_type_credentials:
                        place.setAuthorizationByCredentials();
                        break;
                }

                chooseFragment();
            }
        });
    }

    /**
     * Adiciona os eventos para os botões Salvar e Cancelar
     */
    private void addButtonEvents()
    {
        Button btnSave = (Button) findViewById(R.id.resource_btn_yes);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fragment.fillPlace();
                if (fragment.isValid()) {
                    place.save(CredentialActivity.this);
                    finish();
                }
            }
        });

        Button btnCancel = (Button) findViewById(R.id.resource_btn_no);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void chooseFragment()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable("place", place);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();

        if (place.isAuthorizationByCredentials()) {
            fragment = new PlaceCredentialFragment();
        }
        else {
            fragment = new PlaceTokenFragment();
        }

        fragment.setArguments(bundle);
        tx.replace(R.id.frame_authorization, (Fragment) fragment);
        tx.commit();
    }
}
