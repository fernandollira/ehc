package aiec.br.ehc.parameter;

import android.os.Bundle;

/**
 * Created by gilmar on 26/05/17.
 */

public interface IParameterFragment {
    public void fillFields();
    public void saveParameters();
    public void setArguments(Bundle args);
    public String getType();
    public void applyEffects();
}
