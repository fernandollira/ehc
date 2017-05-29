package aiec.br.ehc.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aiec.br.ehc.dao.EnvironmentDAO;
import aiec.br.ehc.dao.ParameterDAO;
import aiec.br.ehc.dao.PlaceDAO;
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
    private Environment environment;
    private Place place;

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

    /**
     * Retorna a URL de requisição para ativar/desativar o recurso
     *
     * @return URL de requisição
     */
    public URL getRequestURL(Context context) {
        try {
            String domain = this.getPlace(context).getHost().trim();
            Integer port = this.getPlace(context).getPort();
            String param = this.getEnvironment(context).getParameter().trim();
            String path = "";
            if (!param.isEmpty() && (param.contains("/") || !param.contains("="))) {
                path = param.startsWith("/") ? param : "/".concat(param);
            }

            String protocol = this.getPlace(context).getProtocol().toLowerCase();
            String requestUrl = String.format("%s://%s:%s%s", protocol, domain, port, path);
            return new URL(requestUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String getParamsQueryString(Context context)
    {
        List<String> params = this.getUrlParams(context);
        return TextUtils.join("&", params);
    }

    /**
     * Retorna os parâmetros de url com base nos parâmetros de ambiente e recurso
     * @param context   Contexto
     *
     * @return  array
     */
    public List<String> getUrlParams(Context context)
    {
        List<String> urlParams = new ArrayList<>();
        String param = this.getEnvironment(context).getParameter();
        if (param != null && !param.trim().isEmpty() && param.contains("=")) {
            try {
                urlParams.add(URLEncoder.encode(param.trim(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }

        for (Parameter parameter : getRelatedParameters(context)) {
            if (this.getState().equalsIgnoreCase(parameter.getAction())){
                try {
                    String name = URLEncoder.encode(parameter.getName().trim(), "UTF-8");
                    String value = URLEncoder.encode(parameter.getValue().trim(), "UTF-8");
                    urlParams.add(name.concat("=").concat(value));
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        }

        return urlParams;
    }

    /**
     * Obtém todos os parâmetros relacionados ao recurso
     *
     * @param context   Contexto
     * @return Lista de parâmetros
     */
    private List<Parameter> getRelatedParameters(Context context) {
        ParameterDAO dao = new ParameterDAO(context);
        return dao.getAllFromResource(this);
    }

    /**
     * Retorna uma instância do ambiente de relação com este objeto
     *
     * @return ambiente
     */
    public Environment getEnvironment(Context context) {
        if (this.environment == null || this.environmentId != environment.getId()) {
            EnvironmentDAO dao = new EnvironmentDAO(context);
            this.environment = dao.getById(this.environmentId);
        }

        return this.environment;
    }

    /**
     * Retorna uma instância do local de relação com este objeto
     *
     * @return local
     */
    public Place getPlace(Context context) {
        Environment environ = this.getEnvironment(context);
        if (this.place == null || environ.getPlaceId() != place.getId()) {
            PlaceDAO dao = new PlaceDAO(context);
            this.place = dao.getById(environ.getPlaceId());
        }

        return this.place;
    }
}
