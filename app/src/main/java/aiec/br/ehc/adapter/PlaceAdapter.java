package aiec.br.ehc.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.converter.ImageConverter;
import aiec.br.ehc.model.Place;

/**
 * Adapter personalizado para listagem de locais
 *
 * @author Gilmar Soares <professorgilmagro@gmail.com>
 * @author Ricardo Boreto <ricardoboreto@gmail.com>
 * @since 2017-05-07
 */
public class PlaceAdapter extends BaseAdapter {
    private Context context;
    private List<Place> listPlaces;

    public PlaceAdapter(Context context, List<Place> listPlaces) {
        this.listPlaces = listPlaces;
        this.context = context;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return this.listPlaces.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return this.listPlaces.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return this.listPlaces.get(position).getId();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        // se a view já foi instanciada, logo não precisamos instanciá-la novamente
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.list_place_item, parent, false);
        }

        TextView txtName = (TextView) view.findViewById(R.id.place_item_name);
        TextView txtDescription = (TextView) view.findViewById(R.id.place_item_description);
        TextView txtHost = (TextView) view.findViewById(R.id.place_item_host);
        ImageView imgIcon = (ImageView) view.findViewById(R.id.place_item_icon);
        Place place = this.listPlaces.get(position);

        String port = place.getPort() != null ? place.getPort().toString() : "";
        txtName.setText(place.getName());
        txtDescription.setText(place.getDescription());
        txtHost.setText(place.getHost() + ": " + port);

        String icon = place.getIcon() == null ? context.getString(R.string.default_place_icon) : place.getIcon();
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(icon, "drawable", context.getPackageName());
        imgIcon.setImageResource(resourceId);
        ImageConverter.toNegativeColor(imgIcon);

        return view;
    }
}
