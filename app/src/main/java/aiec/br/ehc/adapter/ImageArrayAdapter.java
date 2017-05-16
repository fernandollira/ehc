package aiec.br.ehc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.ImageHelper;

/**
 * Adapter simples para listagem de images
 */
public class ImageArrayAdapter extends ArrayAdapter<String> {
    private final String[] images;

    public ImageArrayAdapter(Context context, String[] images) {
        super(context, android.R.layout.simple_spinner_item, images);
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    /**
     * Monta a view para a imagem de uma determinada posição do array
     * @param position  posição
     * @return View
     */
    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.color.colorPrimaryDark);
        int resId = getContext().getResources().getIdentifier(images[position], "drawable", getContext().getPackageName());
        imageView.setImageResource(resId);
        ImageHelper.toNegativeColor(imageView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 256);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
