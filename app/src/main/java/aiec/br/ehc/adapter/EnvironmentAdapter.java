package aiec.br.ehc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import aiec.br.ehc.R;
import aiec.br.ehc.converter.ImageConverter;
import aiec.br.ehc.model.Environment;

/**
 * Created by gilmar on 12/05/17.
 */
public class EnvironmentAdapter extends RecyclerView.Adapter<EnvironmentViewHolder> implements View.OnClickListener {
    private List<Environment> environments;
    private Context context;

    public EnvironmentAdapter(Context context, List<Environment> itemList) {
        this.environments = itemList;
        this.context = context;
    }

    @Override
    public EnvironmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_environment_item, null);
        RecyclerView.ViewHolder viewHolder = new EnvironmentViewHolder(layoutView);
        return (EnvironmentViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(EnvironmentViewHolder holder, int position) {
        Environment environment = environments.get(position);
        holder.name.setText(environment.getName());
        int resId = context.getResources().getIdentifier(environment.getIcon(), "drawable", context.getPackageName());
        holder.icon.setImageResource(resId);
        ImageConverter.toNegativeColor(holder.icon);
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
        Toast.makeText(view.getContext(), "Em desenvolvimento", Toast.LENGTH_SHORT).show();
    }
}