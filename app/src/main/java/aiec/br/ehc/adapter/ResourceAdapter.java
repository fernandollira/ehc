package aiec.br.ehc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import aiec.br.ehc.ResourceEditorActivity;
import aiec.br.ehc.R;
import aiec.br.ehc.dao.ResourceDAO;
import aiec.br.ehc.helper.AnimationHelper;
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
    private Bundle properties;
    private ResourceHelper helper;

    public ResourceViewHolder(View itemView, ResourceAdapter adapter) {
        super(itemView);
        this.adapter = adapter;
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        name = (TextView)itemView.findViewById(R.id.resource_item_name);
        icon = (ImageView)itemView.findViewById(R.id.resource_item_icon);
        cardView = (CardView) itemView.findViewById(R.id.resource_card_view);
        helper = new ResourceHelper(itemView.getContext());
    }

    @Override
    public void onClick(View view) {
        String newState = resource.getState().equals("on") ? "off" : "on";
        String newIcon = properties.getString(newState);
        icon.setImageResource(helper.getIdentifierFromDrawable(newIcon));

        // aplica os efeitos com base nas propriedades do recurso
        this.applyEffects(newState);
        resource.setState(newState);
        resource.save(view.getContext());
    }

    /**
     * Aplica os efeitos visuais com base nas propriedades do recurso de relação com este objeto
     * @param state Estado atual
     */
    public void applyEffects(String state)
    {
        Bundle properties = this.getProperties();
        if (properties.isEmpty()) {
            return;
        }

        String newIcon = properties.getString(state);
        icon.setImageResource(helper.getIdentifierFromDrawable(newIcon));

        // define as propriedades para o estado de ligado
        String color = "#FFFFFF";
        if (state.equals("on")) {
            String newColor = properties.getString("color");
            color = (newColor != null) ? newColor : color;

            // verifica se deve aplicar alguma animação
            String animateType = properties.getString("animation");
            if (animateType != null && animateType.equals("rotate")) {
                Animation anim = AnimationHelper.getRotateAroundSelfCenter(700);
                icon.setAnimation(anim);
            }
        }
        else if(icon.getAnimation() != null) {
            icon.getAnimation().cancel();
        }

        icon.setColorFilter(Color.parseColor(color));
    }

    /**
     * Retorna as propriedades do recurso que são essenciais  para definição de efeitos e animação
     *
     * @return Bundle
     */
    private Bundle getProperties()
    {
        if (properties == null) {
            String[] images = itemView.getResources().getStringArray(R.array.object_resource_icons);
            ResourceHelper helper = new ResourceHelper(itemView.getContext());
            this.properties = helper.getPropertiesByDrawable(images, resource.getIcon());
        }

        return this.properties;
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
                        Intent it = new Intent(context, ResourceEditorActivity.class);
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