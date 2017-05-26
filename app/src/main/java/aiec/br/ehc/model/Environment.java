package aiec.br.ehc.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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
public class Environment extends BaseModel implements Parcelable {
    private Integer placeId;
    private String name;
    private String description;
    private String parameter;
    private String icon;
    private Date createdAt;
    private Date modificationAt;
    private String createdBy;
    private String modifiedBy;

    public Environment() {}

    /**
     * Construtor para possibilitar a criação via parcel
     * @param in
     */
    protected Environment(Parcel in) {
        id = in.readInt();
        placeId = in.readInt();
        name = in.readString();
        description = in.readString();
        parameter = in.readString();
        icon = in.readString();
        createdBy = in.readString();
        modifiedBy = in.readString();

        Long tmpDate = in.readLong();
        this.createdAt = tmpDate == -1 ? null : new Date(tmpDate);

        tmpDate = in.readLong();
        this.modificationAt = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<Environment> CREATOR = new Creator<Environment>() {
        @Override
        public Environment createFromParcel(Parcel in) {
            return new Environment(in);
        }

        @Override
        public Environment[] newArray(int size) {
            return new Environment[size];
        }
    };

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

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
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
        EnvironmentDAO dao = new EnvironmentDAO(context);
        dao.save(this);
        dao.close();
    }

    @Override
    public void delete(Context context) {
        EnvironmentDAO dao = new EnvironmentDAO(context);
        dao.delete(this.id);
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

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(placeId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(parameter);
        dest.writeString(icon);
        dest.writeString(createdBy);
        dest.writeString(modifiedBy);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(modificationAt != null ? modificationAt.getTime() : -1);
    }
}
