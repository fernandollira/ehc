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
import aiec.br.ehc.model.Resource;

/**
 * Adapter para exibição dos recursoss por ambiente
 */
public class ResourceAdapter extends RecyclerView.Adapter<ResourceViewHolder> implements View.OnClickListener {
    private Context context;
    public final Environment environment;
    public List<Resource> resources;

    public ResourceAdapter(Context context, List<Resource> itemList, Environment environment) {
        this.resources = itemList;
        this.context = context;
        this.environment = environment;
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resource_item, null);
        RecyclerView.ViewHolder viewHolder = new ResourceViewHolder(layoutView, this);
        return (ResourceViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(ResourceViewHolder holder, int position) {
        Resource resource = resources.get(position);
        holder.resource = resource;
        holder.name.setText(resource.getName());
        int resId = ResourceHelper.from(context).getIdentifierFromDrawable(resource.getIcon());
        holder.icon.setImageResource(resId);
        holder.icon.setColorFilter(Color.rgb(255, 255, 255));
        holder.applyEffects(resource.getState());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return resources.size();
    }

    @Override
    public void onClick(View view) {

    }
}
