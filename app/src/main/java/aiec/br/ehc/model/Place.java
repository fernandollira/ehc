package aiec.br.ehc.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import aiec.br.ehc.dao.EnvironmentDAO;
import aiec.br.ehc.dao.PlaceDAO;

/**
 * Modelo locais
 *
 * @author  Gilmar Soares <professorgilmagro@gmail.com>
 * @author  Ricardo Boreto <ricardoboreto@gmail.com>
 * @since   2017-05-07
 */
public class Place implements Parcelable {
    private Integer id;
    private String name;
    private String description;
    private String host;
    private Integer port;
    private String icon;
    private Date createdAt;
    private Date modificationAt;
    private String createdBy;
    private String modifiedBy;

    /**
     * Construtor público da classe
     */
    public Place() {

    }

    /**
     * Permite instanciar o objeto a partir de outro serializado
     * Este construtor é invocado pelo método createFromParcel do objeto CREATOR
     *
     * @param in
     */
    protected Place(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.host = in.readString();
        this.port = in.readInt();
        this.icon = in.readString();

        Long tmpDate = in.readLong();
        this.createdAt = tmpDate == -1 ? null : new Date(tmpDate);

        tmpDate = in.readLong();
        this.modificationAt = tmpDate == -1 ? null : new Date(tmpDate);

        this.createdBy = in.readString();
        this.modifiedBy = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public String toString() {
        return String.format("%s %s", this.name, this.description);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public void save(Context context) {
        PlaceDAO dao = new PlaceDAO(context);
        dao.save(this);
        dao.close();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(host);
        parcel.writeInt(port);
        parcel.writeString(icon);
        parcel.writeLong(createdAt != null ? createdAt.getTime() : -1);
        parcel.writeLong(modificationAt != null ? modificationAt.getTime() : -1);
        parcel.writeString(createdBy);
        parcel.writeString(modifiedBy);
    }

    /**
     * Retorna a quantidade de ambientes de relação com este objeto
     *
     * @return  total de ambientes
     */
    public Integer getRelatedEnvironmentCount(Context context)
    {
        EnvironmentDAO dao = new EnvironmentDAO(context);
        return Integer.valueOf(dao.getEnvironmentCountFromPlaceId(this.id));
    }
}
