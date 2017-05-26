package aiec.br.ehc.model;

import android.content.Context;

/**
 * Model generica para centralizar comportamentos comuns
 */
abstract class BaseModel {
    protected Integer id;

    /**
     * Verifica se o objeto é novo
     *
     * @return bool
     */
    public boolean isNew()
    {
        return this.id == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Facilitador para prover a persistência do objeto
     *
     * @param context
     */
    public abstract void save(Context context);

    /**
     * Facilitador para prover a persistência do objeto
     *
     * @param context
     */
    public abstract void delete(Context context);
}
