package aiec.br.ehc.model;

import android.content.Context;

import java.util.Date;

import aiec.br.ehc.dao.ParameterDAO;
import aiec.br.ehc.dao.ResourceDAO;

/**
 * Modelo Recursos de ambientes
 *
 * @author  Gilmar Soares <professorgilmagro@gmail.com>
 * @author  Ricardo Boreto <ricardoboreto@gmail.com>
 * @since   2017-05-13
 */
public class Resource {
    private Integer id;
    private Integer environmentId;
    private String name;
    private String description;
    private String type;
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

    public Integer getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        ResourceDAO dao = new ResourceDAO(context);
        dao.save(this);
        dao.close();
    }

    /**
     * Retorna a quantidade de parâmetros de relação com este objeto
     *
     * @return  total de parâmetros
     */
    public Integer getRelatedParameterCount(Context context)
    {
        ParameterDAO dao = new ParameterDAO(context);
        return Integer.valueOf(dao.getParameterCountFromResourceId(this.id));
    }
}
