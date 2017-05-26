package aiec.br.ehc.parameter;

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
import aiec.br.ehc.model.Parameter;
import aiec.br.ehc.model.Resource;

/**
 * Fragmento para configuração de recursos do tipo On/Off
 */
public class TurnOnOffFragment extends Fragment implements IParameterFragment {
    static final String TYPE = "switch";
    static final String STATE_OFF = "of";
    static final String STATE_ON = "on";

    private EditText txt_parameter_name;
    private EditText txt_parameter_on_value;
    private EditText txt_parameter_off_value;
    private Resource resource;
    private List<Parameter> parameters;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parameter_view_on_off, container, false);
        ImageView icon_off = (ImageView) view.findViewById(R.id.paramenter_off_icon);
        ImageView icon_on = (ImageView) view.findViewById(R.id.paramenter_on_icon);
        ImageView icon_param = (ImageView) view.findViewById(R.id.paramenter_param_icon);
        txt_parameter_name = (EditText) view.findViewById(R.id.parameter_switch_param_name);
        txt_parameter_off_value = (EditText) view.findViewById(R.id.parameter_switch_off_value);
        txt_parameter_on_value = (EditText) view.findViewById(R.id.parameter_switch_on_value);
        icon_param.setColorFilter(Color.rgb(255, 255, 255));
        icon_off.setColorFilter(Color.rgb(255, 255, 255));
        icon_on.setColorFilter(Color.rgb(255, 255, 0));

        this.fillFields();
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

    @Override
    public String getType() {
        return TYPE;
    }
}