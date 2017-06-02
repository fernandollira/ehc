package aiec.br.ehc.adapter;

import android.content.Context;

/**
 * Created by gilmar on 02/06/17.
 */

public interface IResourceView {
    /**
     * Aplica os efeitos visuais com base nas propriedades do recurso de relação com este objeto
     * @param state Estado atual
     */
    public void applyEffects(String state);

    /**
     * Retorna o contexto de relação com esta view
     *
     * @return Contexto
     */
    public Context getContext();
}
