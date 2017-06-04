package aiec.br.ehc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import aiec.br.ehc.R;
import aiec.br.ehc.ResourceActivity;
import aiec.br.ehc.adapter.IResourceView;
import aiec.br.ehc.helper.AnimationEffectsHelper;
import aiec.br.ehc.helper.ResourceHelper;
import aiec.br.ehc.model.Resource;
import aiec.br.ehc.task.ResourceRequestTask;

/**
 * Cria uma dialog customizado para controle de intensidade
 */
public class ResourceIntensityControlDialog extends Dialog implements IResourceView, DialogInterface.OnDismissListener {
    private final Resource resource;
    private final Bundle properties;
    private ImageView imgResourceIcon;
    private SeekBar control;
    private TextView txtIntensity;

    public ResourceIntensityControlDialog(Context context, Resource resource, Bundle properties) {
        super(context, R.style.StyledDialog);
        this.resource = resource;
        this.properties = properties;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_dialog_intensity_view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.setCancelable(true);
        imgResourceIcon = (ImageView) findViewById(R.id.resource_dialog_intensity_icon);
        txtIntensity = (TextView) findViewById(R.id.resource_dialog_intensity_info);
        control = (SeekBar) findViewById(R.id.resource_dialog_intensity_value);
        control.setMax(resource.getMaxValue());
        control.incrementProgressBy(resource.getStepValue());
        control.setProgress(resource.getIntensityValue() / resource.getStepValue());
        control.setSecondaryProgress(resource.getIntensityValue());
        control.setEnabled(resource.getState().equals("on"));
        txtIntensity.setText(resource.getIntensityValue().toString());

        // aplica os efeitos na imagem
        this.applyEffects(resource.getState());

        this.updateControlColor();
        this.addOnResourceChangeState();
        this.addControlEvents();
    }

    private void updateControlColor() {
        Drawable thumb = getContext().getDrawable(R.drawable.holo_lamp);
        int color = Color.rgb(171, 171, 171);
        if (control.isEnabled()) {
            color = Color.rgb(255, 255, 255);
        }
        thumb.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        control.setThumb(thumb);
    }

    /**
     * Adiciona os eventos para o controlador de intensidade de recurso
     */
    private void addControlEvents()
    {
        final ResourceIntensityControlDialog self = this;
        control.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress / resource.getStepValue();
                progress = progress * resource.getStepValue();
                seekBar.setSecondaryProgress(progress);
                txtIntensity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                resource.setIntensityValue(seekBar.getSecondaryProgress());
                new ResourceRequestTask(self).execute(resource);
            }
        });
    }

    /**
     * Define os comportamentos para o click na imagem
     */
    public void addOnResourceChangeState() {
        final ResourceIntensityControlDialog self = this;
        imgResourceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newState = resource.getState().equals("on") ? "off" : "on";
                control.setEnabled(newState.equals("on"));

                // aplica os efeitos com base nas propriedades do recurso
                resource.setState(newState);
                new ResourceRequestTask(self).execute(resource);
            }
        });
    }

    /**
     * Aplica os efeitos visuais com base nas propriedades do recurso de relação com este objeto
     * @param state Estado atual
     */
    public void applyEffects(String state)
    {
        if (state.equals("off")) {
            this.dismiss();
        }

        if (properties.isEmpty()) {
            return;
        }

        String newIcon = properties.getString(state);
        imgResourceIcon.setImageResource(ResourceHelper.from(getContext()).getIdentifierFromDrawable(newIcon));
        this.updateControlColor();

        // Aplica os efeitos visuais definidos nas propriedades da imagem
        AnimationEffectsHelper helper = new AnimationEffectsHelper(getContext(), properties);
        helper.applyEffects(imgResourceIcon, state);
        txtIntensity.setText(resource.getIntensityValue().toString());
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        ((ResourceActivity) getContext()).fillResourceList();
    }
}
