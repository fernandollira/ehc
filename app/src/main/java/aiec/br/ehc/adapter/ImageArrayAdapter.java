package aiec.br.ehc.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.ImageHelper;

/**
 * Adapter simples para listagem de images
 */
public class ImageArrayAdapter extends ArrayAdapter<String> {
    private final String[] images;

    public ImageArrayAdapter(Context context, String[] images) {
        super(context, android.R.layout.simple_spinner_item, normalizeImages(images));
        this.images = images;
    }

    private static String[] normalizeImages(String[] images) {
        if (!images[0].contains(";")) {
            return images;
        }

        List<String> normalizedList = new ArrayList<>();
        for (String image : images) {
            String[] items = image.split(";");
            for (String item : items) {
                String[] params = item.split(":");
                if (params[0].equals("off")) {
                    normalizedList.add(params[1]);
                    break;
                }
            }
        }

        return normalizedList.toArray(new String[normalizedList.size()]);
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
            Bundle properties = new Bundle();
            String[] items = imageInfo.split(";");
            for (String item : items) {
                String[] params = item.split(":");
                if (params[0].equals("off")){
                    imageInfo = params[1];
                }
                properties.putString(params[0], params[1]);
            }
            imageView.setTag(properties);
        }

        int resId = getContext().getResources().getIdentifier(imageInfo, "drawable", getContext().getPackageName());
        imageView.setImageResource(resId);
        ImageHelper.toNegativeColor(imageView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 256);
        imageView.setLayoutParams(params);
        imageView.setPaddingRelative(10,10,10,10);

        return imageView;
    }
}
