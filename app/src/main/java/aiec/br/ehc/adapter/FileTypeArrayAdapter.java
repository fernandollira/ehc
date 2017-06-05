package aiec.br.ehc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.ResourceHelper;

/**
 * Adapter simples para listagem de images
 */
public class FileTypeArrayAdapter extends ArrayAdapter<String> {
    private final String[] images;
    private final String responseType;

    public FileTypeArrayAdapter(Context context, String[] images, String responseType) {
        super(context, R.layout.parameter_reader_file_type_item, images);
        this.images = images;
        this.responseType = responseType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        // se a view já foi instanciada, logo não precisamos instanciá-la novamente
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.parameter_reader_file_type_item, parent, false);
        }

        String itemName = getItem(position);
        ImageView image = (ImageView) view.findViewById(R.id.parameter_reader_file_type_icon);
        int resID = ResourceHelper.from(getContext()).getIdentifierFromDrawable(itemName);
        image.setImageResource(resID);
        image.setColorFilter(Color.rgb(255, 255, 255));
        image.setTag(itemName.replace("file_type_", ""));

        if (!TextUtils.isEmpty(responseType) && image.getTag().toString().equalsIgnoreCase(responseType)) {
            image.setColorFilter(Color.parseColor("#22d5e5"));
        }

        return view;
    }
}
