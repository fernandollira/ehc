package aiec.br.ehc.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import aiec.br.ehc.R;

/**
 * View Holder para cada item de ambiente
 */
public class EnvironmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name;
    public ImageView icon;

    public EnvironmentViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        name = (TextView)itemView.findViewById(R.id.environment_item_name);
        icon = (ImageView)itemView.findViewById(R.id.environment_item_icon);
    }

    @Override
    public void onClick(View view) {
        Snackbar.make(view, "Em desenvolvimento", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
