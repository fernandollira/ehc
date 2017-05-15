package aiec.br.ehc.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Gestor de preferências para facilitar a inclusão e recuperação de Shared Settings
 */
public class SharedPreferenceHelper {
    // constante com a chave para obtenção da preferência padrão de local
    public static final String PLACE_PREFERENCE_DEFAULT = "place_default";

    private static SharedPreferenceHelper instance;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    private SharedPreferenceHelper(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Retorna uma instância unica com base no contexto informado
     *
     * @param context   Contexto
     * @return
     */
    public static SharedPreferenceHelper from(Context context) {
        if(instance == null) {
            instance = new SharedPreferenceHelper(context);
        }

        return instance;
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param key       Chave da preferência
     *
     * @return  boolean
     */
    public boolean getBoolean(String key){
        return this.sharedPreferences.getBoolean(key, false);
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public String getStringOf(String key){
        return this.getStringOf(key, "");
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public String getStringOf(String key, String defaultValue){
        return this.sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public Boolean getBooleanOf(String key, Boolean defaultValue){
        return this.sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public Integer getIntegerOf(String key){
        return this.sharedPreferences.getInt(key, 0);
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public Float getFloatOf(String key){
        return this.sharedPreferences.getFloat(key, 0);
    }

    /**
     * Permite editar/salvar uma preferência com base na chave e valor
     * @param key       Nome da chave/nome da preferência
     * @param value     Valor
     */
    public void savePreference(String key, String value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Permite editar/salvar uma preferência com base na chave e valor
     * @param key       Nome da chave/nome da preferência
     * @param value     Valor
     */
    public void savePreference(String key, Boolean value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Permite editar/salvar uma preferência com base na chave e valor
     * @param key       Nome da chave/nome da preferência
     * @param value     Valor
     */
    public void savePreference(String key, Integer value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Permite editar/salvar uma preferência com base na chave e valor
     * @param key       Nome da chave/nome da preferência
     * @param value     Valor
     */
    public void savePreference(String key, Float value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Permite editar/salvar uma preferência com base na chave e valor
     * @param key       Nome da chave/nome da preferência
     * @param value     Valor
     */
    public void savePreference(String key, Long value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Retorna a peferência padrão para local
     * @return int
     */
    public int getPreferenceForDefaultPlace()
    {
        int placeId = this.getIntegerOf(PLACE_PREFERENCE_DEFAULT);
        return placeId;
    }
}
