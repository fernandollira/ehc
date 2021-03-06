package aiec.br.ehc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.helper.ResourceHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Place;

/**
 * Adapter para exibição dos ambientes por local
 */
public class EnvironmentAdapter
        extends RecyclerView.Adapter<EnvironmentViewHolder>
        implements View.OnClickListener {
    private Context context;
    public final Place place;
    public List<Environment> environments;

    public EnvironmentAdapter(Context context, List<Environment> itemList, Place place) {
        this.environments = itemList;
        this.context = context;
        this.place = place;
    }

    @Override
    public EnvironmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_environment_item, null);
        RecyclerView.ViewHolder viewHolder = new EnvironmentViewHolder(layoutView, this);
        return (EnvironmentViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(EnvironmentViewHolder holder, int position) {
        Environment environment = environments.get(position);
        holder.setEnvironment(environment);
        holder.name.setText(environment.getName());
        int resId = ResourceHelper.from(context).getIdentifierFromDrawable(environment.getIcon());
        holder.icon.setImageResource(resId);
        holder.icon.setColorFilter(Color.rgb(255, 255, 255));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return environments.size();
    }

    @Override
    public void onClick(View view) {

    }
}


