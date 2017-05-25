package aiec.br.ehc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import aiec.br.ehc.ParameterActivity;
import aiec.br.ehc.R;
import aiec.br.ehc.ResourceActivity;
import aiec.br.ehc.dao.ResourceDAO;
import aiec.br.ehc.dialog.ResourceEditDialog;
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
        int resId = context.getResources().getIdentifier(resource.getIcon(), "drawable", context.getPackageName());
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
        return resources.size();
    }

    @Override
    public void onClick(View view) {

    }
}

/**
 * View Holder para cada item de recursos
 */
class ResourceViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView name;
    public ImageView icon;
    public Resource resource;
    public CardView cardView;
    private ResourceAdapter adapter;

    public ResourceViewHolder(View itemView, ResourceAdapter adapter) {
        super(itemView);
        this.adapter = adapter;
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        name = (TextView)itemView.findViewById(R.id.resource_item_name);
        icon = (ImageView)itemView.findViewById(R.id.resource_item_icon);
        cardView = (CardView) itemView.findViewById(R.id.resource_card_view);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Em desenvolvimento", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo contextMenuInfo) {
        final Context context = view.getContext();
        final Resource resource = this.resource;
        final Environment environment = this.adapter.environment;
        MenuItem menuEdit = menu.add("Editar");
        menuEdit.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent it = new Intent(context, ParameterActivity.class);
                        it.putExtra("EXTRA_ENVIRONMENT", environment);
                        it.putExtra("EXTRA_RESOURCE", resource);
                        context.startActivity(it);
                        return false;
                    }
                }
        );

        MenuItem menuDelete = menu.add("Excluir");
        menuDelete.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ResourceDAO dao = new ResourceDAO(context);
                        dao.delete(resource.getId());
                        adapter.resources.remove(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                        return false;
                    }
                }
        );
    }
}