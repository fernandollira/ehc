package aiec.br.ehc.model;

import android.content.Context;

import java.util.Date;

import aiec.br.ehc.dao.EnvironmentDAO;
import aiec.br.ehc.dao.ResourceDAO;

/**
 * Modelo ambiente de local
 *
 * @author  Gilmar Soares <professorgilmagro@gmail.com>
 * @author  Ricardo Boreto <ricardoboreto@gmail.com>
 * @since   2017-05-12
 */
public class Environment {
    private Integer id;
    private Integer placeId;
    private String name;
    private String description;
    private String icon;
    private Date createdAt;
    private Date modificationAt;
    private String createdBy;
    private String modifiedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        EnvironmentDAO dao = new EnvironmentDAO(context);
        dao.save(this);
        dao.close();
    }

    /**
     * Retorna a quantidade de recursos de relação com este objeto
     *
     * @return  total de recursos
     */
    public Integer getRelatedResourceCount(Context context)
    {
        ResourceDAO dao = new ResourceDAO(context);
        return Integer.valueOf(dao.getResourceCountFromEnvironmentId(this.id));
    }
}
