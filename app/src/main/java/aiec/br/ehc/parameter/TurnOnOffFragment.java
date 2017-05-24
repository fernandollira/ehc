package aiec.br.ehc.parameter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * Created by gilmar on 24/05/17.
 */

public class TurnOnOffFragment extends Fragment {
    private ImageView icon_on;
    private ImageView icon_off;
    private ImageView icon_param;
    private EditText txt_parameter_off_name;
    private EditText txt_parameter_on_name;
    private EditText txt_parameter_on_value;
    private EditText txt_parameter_off_value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parameter_view_on_off, container, false);
        icon_off = (ImageView) view.findViewById(R.id.paramenter_off_icon);
        icon_on = (ImageView) view.findViewById(R.id.paramenter_on_icon);
        icon_param  = (ImageView) view.findViewById(R.id.paramenter_param_icon);
        txt_parameter_off_name = (EditText) view.findViewById(R.id.parameter_switch_param_name);
        txt_parameter_off_value = (EditText) view.findViewById(R.id.parameter_switch_off_value);
        icon_param.setColorFilter(Color.rgb(255, 255, 255));
        icon_off.setColorFilter(Color.rgb(255, 255, 255));
        icon_on.setColorFilter(Color.rgb(255, 255, 0));
        return view;
    }

    public void fillFields(Resource resource) {
        ParameterDAO dao = new ParameterDAO(getContext());
        List<Parameter> resources = dao.getAllFromResource(resource);
        txt_parameter_off_name.setText(resources.get(0).getName());
        txt_parameter_off_value.setText(resources.get(0).getValue());
    }
}