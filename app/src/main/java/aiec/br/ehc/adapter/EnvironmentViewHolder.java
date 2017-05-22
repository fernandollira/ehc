package aiec.br.ehc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aiec.br.ehc.R;
import aiec.br.ehc.ResourceActivity;
import aiec.br.ehc.dao.EnvironmentDAO;
import aiec.br.ehc.dialog.EnvironmentEditDialog;
import aiec.br.ehc.model.Environment;

/**
 * View Holder para cada item de ambiente
 */
public class EnvironmentViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView name;
    public ImageView icon;
    public Environment environment;
    public CardView cardView;
    private EnvironmentAdapter adapter;

    public EnvironmentViewHolder(View itemView, EnvironmentAdapter adapter) {
        super(itemView);
        this.adapter = adapter;
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        name = (TextView)itemView.findViewById(R.id.environment_item_name);
        icon = (ImageView)itemView.findViewById(R.id.environment_item_icon);
        cardView = (CardView) itemView.findViewById(R.id.environment_card_view);
    }

    @Override
    public void onClick(View view) {
        Intent it = new Intent(view.getContext(), ResourceActivity.class);
        it.putExtra("EXTRA_ENVIRONMENT", this.environment);
        view.getContext().startActivity(it);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View view, final ContextMenu.ContextMenuInfo contextMenuInfo) {
        final Context context = view.getContext();
        MenuItem menuEdit = menu.add("Editar");
        menuEdit.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        EnvironmentEditDialog dialog = new EnvironmentEditDialog((Activity) context, adapter.place, environment);
                        dialog.show();
                        return false;
                    }
                }
        );

        MenuItem menuDelete = menu.add("Excluir");
        menuDelete.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        EnvironmentDAO dao = new EnvironmentDAO(context);
                        dao.delete(environment.getId());
                        adapter.environments.remove(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                        return false;
                    }
                }
        );
    }

    /**
     * Setter para definir o ambiente
     *
     * @param environment
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
