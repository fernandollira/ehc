package aiec.br.ehc.model;

import android.content.Context;
import java.util.Date;
import aiec.br.ehc.dao.ParameterDAO;

/**
 * Modelo de parâmetros de recursos
 *
 * @author  Gilmar Soares <professorgilmagro@gmail.com>
 * @author  Ricardo Boreto <ricardoboreto@gmail.com>
 * @since   2017-05-13
 */
public class Parameter {
    private Integer id;
    private Integer resourceId;
    private String name;
    private String value;
    private String uiType;
    private String icon;
    private Date createdAt;
    private Date modificationAt;
    private String createdBy;
    private String modifiedBy;

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String ui_type) {
        this.uiType = ui_type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModificationAt() {
        return modificationAt;
    }

    public void setModificationAt(Date modificationAt) {
        this.modificationAt = modificationAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Facilitador para prover a persistência do objeto
     *
     * @param context
     */
    public void save(Context context)
    {
        ParameterDAO dao = new ParameterDAO(context);
        dao.save(this);
        dao.close();
    }
}
