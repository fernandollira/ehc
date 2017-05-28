package aiec.br.ehc.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.ImageHelper;
import aiec.br.ehc.helper.ResourceHelper;

/**
 * Adapter simples para listagem de images
 */
public class ImageArrayAdapter extends ArrayAdapter<String> {
    private final String[] images;

    public ImageArrayAdapter(Context context, String[] images) {
        super(context, android.R.layout.simple_spinner_item, normalizeImages(images, context));
        this.images = images;
    }

    private static String[] normalizeImages(String[] images, Context context) {
        if (!images[0].contains(";")) {
            return images;
        }

        return ResourceHelper.from(context).getSimpleList(images, "off");
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
        imageView.setBackgroundResource(R.drawable.icon_item);

        String imageInfo = images[position];
        if (imageInfo.contains(";")) {
            Bundle properties = ResourceHelper.from(getContext()).getProperties(imageInfo);
            imageInfo = properties.getString("off");
            imageView.setTag(properties);
        }

        int resId = ResourceHelper.from(getContext()).getIdentifierFromDrawable(imageInfo);
        imageView.setImageResource(resId);
        ImageHelper.toNegativeColor(imageView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 256);
        imageView.setLayoutParams(params);

        return imageView;
    }
}
