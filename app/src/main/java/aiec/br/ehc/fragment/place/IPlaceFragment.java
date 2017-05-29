package aiec.br.ehc.fragment.place;

import android.os.Bundle;

/**
 * Interface para fragmentos de local
 */
public interface IPlaceFragment {
    public boolean isValid();
    public void fillPlace();
    public void setArguments(Bundle bundle);
}
