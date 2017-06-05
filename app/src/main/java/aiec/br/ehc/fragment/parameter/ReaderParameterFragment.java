package aiec.br.ehc.fragment.parameter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.adapter.FileTypeArrayAdapter;
import aiec.br.ehc.dao.ParameterDAO;
import aiec.br.ehc.model.Parameter;
import aiec.br.ehc.model.Resource;

/**
 * Fragmento para parametrização de recursos de leitura
 * Normalmente utilizados para exibição de informações de sensores
 */
public class ReaderParameterFragment extends Fragment implements IParameterFragment {
    static public final String TYPE = "reader";
    static public final String DEFAULT_RESPONSE = "txt";

    private EditText txtParamValue;
    private EditText txtNodeValue;
    private Resource resource;
    private List<Parameter> parameters;
    private ImageView paramIcon;
    private ImageView nodeIcon;
    private GridView gridFiles;
    private Bundle properties;
    private String selectedOutput = DEFAULT_RESPONSE;
    private View nodeBlock;
    private TextView tvOutputQuest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parameter_view_reader, container, false);
        paramIcon = (ImageView) view.findViewById(R.id.parameter_reader_param_icon);
        nodeIcon = (ImageView) view.findViewById(R.id.parameter_reader_node_icon);

        txtParamValue = (EditText) view.findViewById(R.id.parameter_reader_param_value);
        txtNodeValue = (EditText) view.findViewById(R.id.parameter_reader_node_value);
        tvOutputQuest = (TextView) view.findViewById(R.id.parameter_reader_file_question);
        nodeBlock = view.findViewById(R.id.parameter_reader_node_block);
        gridFiles = (GridView) view.findViewById(R.id.parameter_reader_file_type);

        properties = getArguments().getBundle("properties");
        resource = getArguments().getParcelable("resource");

        this.fillGridFiles(resource.getReadFormat());

        paramIcon.setColorFilter(Color.rgb(255, 255, 255));
        nodeIcon.setColorFilter(Color.rgb(255, 255, 255));

        this.fillFields();
        this.applyEffects();
        this.addGridSelectItem();
        return view;
    }

    private void fillGridFiles(String responseType)
    {
        String[] images = getContext().getResources().getStringArray(R.array.file_type_icons);
        ArrayAdapter adapter = new FileTypeArrayAdapter(this.getContext(), images, responseType);
        gridFiles.setAdapter(adapter);
    }

    @Override
    public void fillFields() {
        resource.setType(TYPE);
        resource.setState("read");

        txtNodeValue.setText(resource.getReadNode());
        selectedOutput = resource.getReadFormat();

        for (Parameter parameter : this.getParameters()) {
            if (parameter.getAction().equals("read")) {
                if (parameter.getName().equals(Parameter.EXTRA_PARAM_ARGS)) {
                    txtParamValue.setText(parameter.getValue());
                    break;
                }
            }
        }

        if (!isShowNodeField()) {
            nodeBlock.setVisibility(View.GONE);
        }
    }

    private void addGridSelectItem() {
        gridFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int visibility = View.VISIBLE;
                ImageView img = (ImageView) view.findViewById(R.id.parameter_reader_file_type_icon);
                img.setColorFilter(Color.parseColor("#22d5e5"));
                selectedOutput = img.getTag().toString();
                if (!isShowNodeField()) {
                    visibility = View.GONE;
                }

                fillGridFiles(selectedOutput);
                nodeBlock.setVisibility(visibility);
                nodeBlock.requestFocus();
            }
        });
    }

    private List<Parameter> getParameters() {
        if (parameters != null) {
            return parameters;
        }

        ParameterDAO dao = new ParameterDAO(getContext());
        parameters = dao.getAllFromResource(resource);
        dao.close();

        return parameters;
    }

    /**
     * Verifica se o campo para inclusão do campo a ser lido na resposta deverá ser exibido
     * @return boolean
     */
    private boolean isShowNodeField() {
        return selectedOutput.equals("xml") || selectedOutput.equals("json");
    }

    @Override
    public boolean isValid() {
        if (txtParamValue.getText().toString().isEmpty()) {
            txtParamValue.setError(getString(R.string.required_field_message));
            txtParamValue.requestFocus();
            return false;
        }

        if (isShowNodeField() && txtNodeValue.getText().toString().isEmpty()) {
            txtNodeValue.setError(getString(R.string.required_field_message));
            txtNodeValue.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(selectedOutput)) {
            tvOutputQuest.setTextColor(Color.RED);
            return false;
        }

        return true;
    }

    /**
     * Salva os parâmetros obtidos
     */
    public void saveParameters() {
        Parameter param = new Parameter();
        for (Parameter parameter : this.getParameters()) {
            if (parameter.isExtraParameterUrl()) {
                param = parameter;
            }
        }

        resource.setReadFormat(selectedOutput);
        resource.setReadNode(txtNodeValue.getText().toString());
        resource.save(getContext());

        param.setResourceId(resource.getId());
        param.setAction("read");
        param.setName(Parameter.EXTRA_PARAM_ARGS);
        param.setValue(txtParamValue.getText().toString());
        param.save(getContext());
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void applyEffects() {

    }
}
