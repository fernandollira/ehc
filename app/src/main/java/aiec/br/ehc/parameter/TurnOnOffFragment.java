package aiec.br.ehc.parameter;

import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
            int resID = getResources().getIdentifier(resource.getIcon(), "drawable", getContext().getPackageName());
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
     * Aplica os efeitos suportados, definidos no xml de images
     */
    public void applyEffects()
    {
        Bundle properties = getArguments().getBundle("properties");
        if (properties == null) {
            return;
        }

        String color = properties.getString("color");
        String iconOnImage = properties.getString("on");
        String iconOffImage = properties.getString("off");

        if (color != null) {
            icon_on.setColorFilter(Color.parseColor(color));
        }

        int resID = getResources().getIdentifier(iconOffImage, "drawable", getContext().getPackageName());
        icon_off.setImageResource(resID);

        resID = getResources().getIdentifier(iconOnImage, "drawable", getContext().getPackageName());
        icon_on.setImageResource(resID);

        String animate = properties.getString("animation");
        if (animate != null && animate.equals("rotate")) {
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(700);
            icon_on.setAnimation(anim);
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }
}