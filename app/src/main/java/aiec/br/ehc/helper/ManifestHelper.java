package aiec.br.ehc.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Helper para utilização de dados oriundos do Manifest
 */
public class ManifestHelper {
    private static ManifestHelper instance = null;
    private final Context context;

    private ManifestHelper(Context context) {
        this.context = context;
    }

    /**
     * Retorna uma instância unica com base no contexto informado
     *
     * @param context   Contexto
     * @return
     */
    public static ManifestHelper from(Context context) {
        if(instance == null) {
            instance = new ManifestHelper(context);
        }

        return instance;
    }

    public Object getMetadata(String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                Object value = appInfo.metaData.get(name);
                return value;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        return null;
    }
}
