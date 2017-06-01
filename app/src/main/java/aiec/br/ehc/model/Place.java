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
public class Place extends BaseModel implements Parcelable {
    private static final String AUTHORIZATION_TYPE_CREDENTIAL = "credentials";
    private static final String AUTHORIZATION_TYPE_TOKEN = "token";
    private static final String CREDENTIAL_DEFAULT_FLAG = "Basic";
    private static final String TOKEN_DEFAULT_FLAG = "token";
    public static final String TOKEN_SEND_BY_URL = "url";
    public static final String TOKEN_SEND_BY_HEADERS = "headers";

    private String name;
    private String description;
    private String protocol;
    private String host;
    private Integer port;
    private String authorizationType;
    private String accessToken;
    private String userCredentials;
    private String tokenSendMethod;
    private String tokenFlag;
    private String credentialFlag;
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
        this.protocol = in.readString();
        this.host = in.readString();
        this.port = in.readInt();
        this.authorizationType = in.readString();
        this.accessToken = in.readString();
        this.userCredentials = in.readString();
        this.tokenSendMethod = in.readString();
        this.tokenFlag = in.readString();
        this.credentialFlag = in.readString();
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAuthorizationType() {
        if (authorizationType == null) {
            return AUTHORIZATION_TYPE_TOKEN;
        }

        return authorizationType;
    }

    public void setAuthorizationType(String authorizationType) {
        this.authorizationType = authorizationType;
    }

    public void setAuthorizationByCredentials() {
        this.authorizationType = AUTHORIZATION_TYPE_CREDENTIAL;
    }

    public void setAuthorizationByToken() {
        this.authorizationType = AUTHORIZATION_TYPE_TOKEN;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(String userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getTokenSendMethod() {
        if (tokenSendMethod == null) {
            return TOKEN_SEND_BY_HEADERS;
        }

        return tokenSendMethod;
    }

    public void setTokenSendMethod(String tokenSendMethod) {
        this.tokenSendMethod = tokenSendMethod;
    }

    public String getTokenFlag() {
        if (tokenFlag == null) {
            return TOKEN_DEFAULT_FLAG;
        }
        return tokenFlag;
    }

    public void setTokenFlag(String tokenFlag) {
        this.tokenFlag = tokenFlag;
    }

    public String getCredentialFlag() {
        if (credentialFlag == null || credentialFlag.isEmpty()) {
            return CREDENTIAL_DEFAULT_FLAG;
        }
        return credentialFlag;
    }

    public void setCredentialFlag(String credentialFlag) {
        this.credentialFlag = credentialFlag;
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

    @Override
    public void save(Context context) {
        PlaceDAO dao = new PlaceDAO(context);
        dao.save(this);
        dao.close();
    }

    @Override
    public void delete(Context context) {
        PlaceDAO dao = new PlaceDAO(context);
        dao.delete(this.id);
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
        parcel.writeString(protocol);
        parcel.writeString(host);
        parcel.writeInt(port);
        parcel.writeString(authorizationType);
        parcel.writeString(accessToken);
        parcel.writeString(userCredentials);
        parcel.writeString(tokenSendMethod);
        parcel.writeString(tokenFlag);
        parcel.writeString(credentialFlag);
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

    public boolean isSecurityProtocol()
    {
        return protocol != null && protocol.equalsIgnoreCase("https");
    }

    /**
     * verifica se o tipo de autorização é por Token de acesso
     *
     * @return boolean
     */
    public boolean isAuthorizationByToken()
    {
        return this.getAuthorizationType().equals(AUTHORIZATION_TYPE_TOKEN);
    }

    /**
     * verifica se o tipo de autorização é por Token de acesso
     *
     * @return boolean
     */
    public boolean isAuthorizationByCredentials()
    {
        return this.getAuthorizationType().equals(AUTHORIZATION_TYPE_CREDENTIAL);
    }

    /**
     * verifica se o tipo de envio do token é por meio da url via parâmetros
     * @return bool
     */
    public boolean isSendTokenByUrl()
    {
        return this.getTokenSendMethod().equals(TOKEN_SEND_BY_URL);
    }

    /**
     * verifica se o tipo de envio do token é por meio do cabeçalho http
     * @return bool
     */
    public boolean isSendTokenByHeaders()
    {
        return this.getTokenSendMethod().equals(TOKEN_SEND_BY_HEADERS);
    }
}
