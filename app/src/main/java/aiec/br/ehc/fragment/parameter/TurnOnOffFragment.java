package aiec.br.ehc.fragment.parameter;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.dao.ParameterDAO;
import aiec.br.ehc.helper.AnimationEffectsHelper;
import aiec.br.ehc.helper.ResourceHelper;
import aiec.br.ehc.model.Parameter;
import aiec.br.ehc.model.Resource;

/**
 * Fragmento para configuração de recursos do tipo On/Off
 */
public class TurnOnOffFragment extends Fragment implements IParameterFragment {
    static final String TYPE = "switch";

    private EditText txt_parameter_name;
    private EditText txt_parameter_on_value;
    private EditText txt_parameter_off_value;
    private Resource resource;
    private List<Parameter> parameters;
    private ImageView icon_off;
    private ImageView icon_on;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parameter_view_on_off, container, false);
        icon_off = (ImageView) view.findViewById(R.id.paramenter_off_icon);
        icon_on = (ImageView) view.findViewById(R.id.paramenter_on_icon);
        ImageView icon_param = (ImageView) view.findViewById(R.id.paramenter_param_icon);
        txt_parameter_name = (EditText) view.findViewById(R.id.parameter_switch_param_name);
        txt_parameter_off_value = (EditText) view.findViewById(R.id.parameter_switch_off_value);
        txt_parameter_on_value = (EditText) view.findViewById(R.id.parameter_switch_on_value);

        icon_param.setColorFilter(Color.rgb(255, 255, 255));
        icon_off.setColorFilter(Color.rgb(255, 255, 255));
        icon_on.setColorFilter(Color.rgb(255, 255, 255));

        this.fillFields();
        this.applyEffects();
        return view;
    }

    /**
     * Preenche os parâmetros com dados persistidos no banco
     */
    public void fillFields() {
        resource = getArguments().getParcelable("resource");
        resource.setType(TYPE);
        if (resource.isNew()) {
            resource.setState("off");
        }
        else {
            int resID = ResourceHelper.from(getContext()).getIdentifierFromDrawable(resource.getIcon());
            icon_off.setImageResource(resID);
            icon_on.setImageResource(resID);
        }

        ParameterDAO dao = new ParameterDAO(getContext());
        parameters = dao.getAllFromResource(resource);
        dao.close();

        for (Parameter parameter : parameters) {
            if (parameter.getAction().equals("off")) {
                txt_parameter_name.setText(parameter.getName());
                txt_parameter_off_value.setText(parameter.getValue());
                continue;
            }

            if (parameter.getAction().equals("on")) {
                txt_parameter_on_value.setText(parameter.getValue());
            }
        }
    }

    /**
     * Salva os parâmetros obtidos
     */
    public void saveParameters()
    {
        ParameterDAO dao = new ParameterDAO(getContext());
        for (Parameter parameter : parameters) {
            dao.delete(parameter.getId());
        }

        Parameter param = new Parameter();
        param.setResourceId(resource.getId());
        param.setName(txt_parameter_name.getText().toString());
        param.setValue(txt_parameter_off_value.getText().toString());
        param.setAction("off");
        param.save(getContext());

        param.setId(null);
        param.setValue(txt_parameter_on_value.getText().toString());
        param.setAction("on");
        param.save(getContext());
    }

    /**
     * Verifica se os campos obrigatórios foram preenchidos corretamente
     *
     * @return valid
     */
    public boolean isValid()
    {
        if (txt_parameter_name.getText().toString().isEmpty()) {
            txt_parameter_name.setError(getString(R.string.required_field_message));
            txt_parameter_name.requestFocus();
            return false;
        }

        if (txt_parameter_on_value.getText().toString().isEmpty()) {
            txt_parameter_on_value.setError(getString(R.string.required_field_message));
            txt_parameter_on_value.requestFocus();
            return false;
        }

        if (txt_parameter_off_value.getText().toString().isEmpty()) {
            txt_parameter_off_value.setError(getString(R.string.required_field_message));
            txt_parameter_off_value.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Aplica os efeitos suportados, definidos no xml de images
     */
    public void applyEffects()
    {
        Bundle properties = getArguments().getBundle("properties");
        if (properties == null) {
            return;
        }

        String iconOnImage = properties.getString("on");
        String iconOffImage = properties.getString("off");

        int resID = ResourceHelper.from(getContext()).getIdentifierFromDrawable(iconOffImage);
        icon_off.setImageResource(resID);

        resID = ResourceHelper.from(getContext()).getIdentifierFromDrawable(iconOnImage);
        icon_on.setImageResource(resID);


        AnimationEffectsHelper helper = new AnimationEffectsHelper(getContext(), properties);
        helper.applyEffects(icon_on, "on");
        helper.applyEffects(icon_off, "off");
    }

    @Override
    public String getType() {
        return TYPE;
    }
}