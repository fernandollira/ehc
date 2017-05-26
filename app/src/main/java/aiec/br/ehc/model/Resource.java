package aiec.br.ehc.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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
public class Resource extends BaseModel implements Parcelable {
    private Integer environmentId;
    private String name;
    private String description;
    private String type;
    private String icon;
    private String method;
    private String state;
    private Integer minValue;
    private Integer maxValue;
    private Integer stepValue;
    private Date createdAt;
    private Date modificationAt;
    private String createdBy;
    private String modifiedBy;

    public Resource() {}

    protected Resource(Parcel in) {
        this.id = in.readInt();
        environmentId = in.readInt();
        name = in.readString();
        description = in.readString();
        icon = in.readString();
        type = in.readString();
        state = in.readString();
        method = in.readString();
        minValue = in.readInt();
        maxValue = in.readInt();
        stepValue = in.readInt();
        createdBy = in.readString();
        modifiedBy = in.readString();

        Long tmpDate = in.readLong();
        this.createdAt = tmpDate == -1 ? null : new Date(tmpDate);

        tmpDate = in.readLong();
        this.modificationAt = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getStepValue() {
        return stepValue;
    }

    public void setStepValue(Integer stepValue) {
        this.stepValue = stepValue;
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

    @Override
    public void save(Context context)
    {
        ResourceDAO dao = new ResourceDAO(context);
        dao.save(this);
        dao.close();
    }

    @Override
    public void delete(Context context)
    {
        ResourceDAO dao = new ResourceDAO(context);
        dao.delete(this.id);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(environmentId);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(icon);
        parcel.writeString(type);
        parcel.writeString(state);
        parcel.writeString(method);
        parcel.writeInt(minValue);
        parcel.writeInt(maxValue);
        parcel.writeInt(stepValue);
        parcel.writeString(createdBy);
        parcel.writeString(modifiedBy);
        parcel.writeLong(createdAt != null ? createdAt.getTime() : -1);
        parcel.writeLong(modificationAt != null ? modificationAt.getTime() : -1);
    }
}
