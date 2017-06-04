package aiec.br.ehc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aiec.br.ehc.R;
import aiec.br.ehc.ResourceEditorActivity;
import aiec.br.ehc.dao.ResourceDAO;
import aiec.br.ehc.dialog.ResourceIntensityControlDialog;
import aiec.br.ehc.helper.AnimationEffectsHelper;
import aiec.br.ehc.helper.ResourceHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Resource;
import aiec.br.ehc.task.ResourceRequestTask;

/**
 * View Holder para cada item de recursos
 */
public class ResourceViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnCreateContextMenuListener, IResourceView{

    public TextView name;
    public ImageView icon;
    public Resource resource;
    public CardView cardView;
    public Context context;
    private ResourceAdapter adapter;
    private Bundle properties;
    private ResourceHelper helper;
    private boolean clicked = false;

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
        this.clicked = true;

        // aplica os efeitos com base nas propriedades do recurso

        if (isShowControl() && resource.getState().equals("on")) {
            applyEffects(resource.getState());
            return;
        }

        resource.setState(newState);
        new ResourceRequestTask(this).execute(resource);
    }

    // Verifica se o controle de intensidade deve ser exibido
    private boolean isShowControl() {
        return resource.hasIntensityControl()
                && !TextUtils.isEmpty(resource.getIntensityParam());
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

        // Aplica os efeitos visuais definidos nas propriedades da imagem
        AnimationEffectsHelper helper = new AnimationEffectsHelper(context, properties);
        helper.applyEffects(icon, state);

        if (this.isShowControl() && this.clicked) {
            this.clicked = false;
            ResourceIntensityControlDialog dialog = new ResourceIntensityControlDialog(context, resource, properties);
            dialog.show();
        }
    }

    /**
     * Retorna o contexto de relação com esta view
     *
     * @return Contexto
     */
    @Override
    public Context getContext() {
        return itemView.getContext();
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
        MenuItem menuEdit = menu.add(context.getString(R.string.edit));
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

        MenuItem menuDelete = menu.add(context.getString(R.string.delete));
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
