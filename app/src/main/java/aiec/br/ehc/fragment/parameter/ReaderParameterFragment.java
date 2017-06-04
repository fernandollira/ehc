package aiec.br.ehc.fragment.parameter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.model.Parameter;
import aiec.br.ehc.model.Resource;

/**
 * Fragmento para parametrização de recursos de leitura
 * Normalmente utilizados para exibição de informações de sensores
 */
public class ReaderParameterFragment extends Fragment implements IParameterFragment {
    static public final String TYPE = "reader";

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
        View view = inflater.inflate(R.layout.parameter_view_reader, container, false);
        icon_off = (ImageView) view.findViewById(R.id.parameter_reader_off_icon);
        icon_on = (ImageView) view.findViewById(R.id.parameter_reader_on_icon);
        ImageView icon_param = (ImageView) view.findViewById(R.id.parameter_reader_param_icon);
        txt_parameter_name = (EditText) view.findViewById(R.id.parameter_reader_param_name);
        txt_parameter_off_value = (EditText) view.findViewById(R.id.parameter_switch_off_value);
        txt_parameter_on_value = (EditText) view.findViewById(R.id.parameter_switch_on_value);

        icon_param.setColorFilter(Color.rgb(255, 255, 255));
        icon_off.setColorFilter(Color.rgb(255, 255, 255));
        icon_on.setColorFilter(Color.rgb(255, 255, 255));

        this.fillFields();
        this.applyEffects();
        return view;
    }

    @Override
    public void fillFields() {

    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void saveParameters() {

    }

    @Override
    public void setArguments(Bundle args) {

    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void applyEffects() {

    }
}
